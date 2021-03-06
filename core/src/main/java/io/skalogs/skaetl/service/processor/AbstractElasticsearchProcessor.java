package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.prometheus.client.Counter;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.MDC;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.contains;

@AllArgsConstructor
@Slf4j
@Getter
public abstract class AbstractElasticsearchProcessor<K, V> extends AbstractOutputProcessor<K, V> {

    private final ESBuffer esBuffer;
    private final ESErrorRetryWriter esErrorRetryWriter;
    private static final Counter esWriteEs = Counter.build()
            .name("skaetl_nb_write_es_common")
            .help("count nb elements to write into ES")
            .labelNames("processConsumerName", "project", "type")
            .register();

    protected void processToElasticsearch(Date date, String project, String type, RetentionLevel retentionLevel, String valueAsString) {
        processToElasticsearch(date, project, type, retentionLevel, valueAsString, null);

    }

    protected void processToElasticsearch(Date date, String project, String type, RetentionLevel retentionLevel, String valueAsString, String id) {
        esWriteEs.labels(getApplicationId() != null ? getApplicationId() : "forRetryApplication", project, type).inc();
        esBuffer.add(date, project, type, retentionLevel, valueAsString, id);
        if (esBuffer.needFlush()) {
            log.info("{} Flushing {}", context().applicationId(), esBuffer.values().size());
            try {
                BulkResponse bulkItemResponses = esBuffer.flush();
                if (bulkItemResponses != null && bulkItemResponses.hasFailures()) {
                    //parse result for check if error or not
                    parseResultErrors(bulkItemResponses);
                }
            } catch (Exception e) {
                parseErrorsTechnical();
            } finally {
                esBuffer.reset();
            }
        }
    }

    private void parseErrorsTechnical() {
        //send all value into topic retry
        esBuffer
                .values()
                .stream()
                .forEach(itemRaw -> esErrorRetryWriter.sendToRetryTopic(getApplicationId() != null ? getApplicationId() : "forRetryApplication", itemRaw));

    }

    protected void parseResultErrors(BulkResponse bulkItemResponses) {
        for (BulkItemResponse bir : bulkItemResponses) {
            MDC.put("item_error", bir.getFailureMessage());
            log.info("EsError {} ", bir.getFailureMessage());
            MDC.remove("item_error");
            if (bir.isFailed() && isRetryable(bir)) {
                routeToNextTopic(bir, false);
            } else {
                routeToNextTopic(bir, true);
            }
        }
    }

    private void routeToNextTopic(BulkItemResponse bulkItemResponse, boolean isErrorTopic) {
        String itemRaw = esBuffer.getItem(bulkItemResponse.getItemId());
        log.debug("target bir is failed {} msg fail {} itemRaw {}", bulkItemResponse.isFailed(), bulkItemResponse.getFailureMessage(), itemRaw);
        if (itemRaw == null) {
            produceErrorToKafka(ValidateData.builder()
                    .timestamp(new Date())
                    .type("ERROR_PARSING")
                    .message("Failure parsing after send for itemId" + bulkItemResponse.getItemId())
                    .statusCode(StatusCode.parsing_invalid_after_send)
                    .success(false)
                    .value("Failure parsing after send" + bulkItemResponse.getFailureMessage()).build());
        } else if (isErrorTopic) {
            produceErrorToKafka(bulkItemResponse.getFailureMessage(), itemRaw);
        } else {
            esErrorRetryWriter.sendToRetryTopic(getApplicationId(), itemRaw);
        }
    }

    private void produceErrorToKafka(String messageFailure, String value) {
        ISO8601DateFormat df = new ISO8601DateFormat();
        ErrorData error = ErrorData.builder()
                .errorReason(StatusCode.error_after_send_es.name())
                .errorMessage(messageFailure)
                .message(value)
                .timestamp(df.format(new Date()))
                .build();
        esErrorRetryWriter.sendToErrorTopic(getApplicationId(), error);
    }


    private void produceErrorToKafka(ValidateData validateData) {
        esErrorRetryWriter.sendToErrorTopic(getApplicationId(), validateData);
    }

    public boolean isRetryable(BulkItemResponse bir) {
        return bir.getType().equals("elasticsearch_http_ko")
                || contains(bir.getFailureMessage(), "java.net");
    }

    public String getApplicationId() {
        return context().applicationId();
    }

    @Override
    public boolean support(TypeOutput typeOutput) {
        return typeOutput == TypeOutput.ELASTICSEARCH;
    }
}