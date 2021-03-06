package io.skalogs.skaetl.rules.metrics.processor;

import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.service.SnmpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.streams.processor.AbstractProcessor;

@AllArgsConstructor
@Slf4j
public class MetricsSnmpProcessor extends AbstractProcessor<Keys, MetricResult> {

    private SnmpService snmpService;

    @Override
    public void process(Keys key, MetricResult value) {

        String v = StringUtils.join(new String[]{value.getRuleName(), " triggered with value ", value.getResult().toString()});

        try {

            snmpService.send(v);

        } catch (Exception ex) {
            log.error("Exception during snmp sending {}", ex.getMessage());
            ex.printStackTrace();
        }

    }
}
