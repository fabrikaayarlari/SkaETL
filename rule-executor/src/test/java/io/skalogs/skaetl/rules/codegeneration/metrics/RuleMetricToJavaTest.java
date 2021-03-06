package io.skalogs.skaetl.rules.codegeneration.metrics;

import io.skalogs.skaetl.rules.codegeneration.SyntaxErrorListener;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleMetricToJavaTest {

    @Test
    public void checkJavaClassName() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT COUNT(*) FROM mytopic WINDOW TUMBLING(5 MINUTES) TO KAFKA targettopic";
        RuleCode rule = ruleToJava.convert("my simple rule", dsl);
        assertThat(rule.getName()).isEqualTo("MySimpleRule");
        assertThat(rule.getRuleClassName()).isEqualTo("io.skalogs.skaetl.metrics.generated.MySimpleRule");
    }


    @Test
    public void functionNoArg() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT COUNT(*) FROM mytopic WINDOW TUMBLING(5 MINUTES) TO KAFKA targettopic";
        RuleCode rule = ruleToJava.convert("My_Count_Rule", dsl);
        assertThat(rule)
                .isEqualTo(new RuleCode("MyCountRule",
                        dsl,
                        "io.skalogs.skaetl.metrics.generated.MyCountRule",
                        "package io.skalogs.skaetl.metrics.generated;\n" +
                                "\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;\n" +
                                "import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;\n" +
                                "import io.skalogs.skaetl.domain.ProcessMetric;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.Keys;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.MetricResult;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import javax.annotation.Generated;\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import static io.skalogs.skaetl.domain.RetentionLevel.*;\n" +
                                "\n" +
                                "import org.apache.kafka.streams.kstream.*;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlMetric\")\n" +
                                "public class MyCountRule extends GenericMetricProcessor {\n" +
                                "    public MyCountRule(ProcessMetric processMetric) {\n" +
                                "        super(processMetric, \"mytopic\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected AggregateFunction aggInitializer() {\n" +
                                "        return aggFunction(\"COUNT\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {\n" +
                                "        return aggregateTumblingWindow(kGroupedStream,5,MINUTES);\n" +
                                "    }\n" +
                                "}"));
    }

    @Test
    public void min() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) TO KAFKA targettopic";
        RuleCode rule = ruleToJava.convert("My_Min_Rule", dsl);
        assertThat(rule)
                .isEqualTo(new RuleCode("MyMinRule",
                        dsl,
                        "io.skalogs.skaetl.metrics.generated.MyMinRule",
                        "package io.skalogs.skaetl.metrics.generated;\n" +
                                "\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;\n" +
                                "import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;\n" +
                                "import io.skalogs.skaetl.domain.ProcessMetric;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.Keys;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.MetricResult;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import javax.annotation.Generated;\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import static io.skalogs.skaetl.domain.RetentionLevel.*;\n" +
                                "\n" +
                                "import org.apache.kafka.streams.kstream.*;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlMetric\")\n" +
                                "public class MyMinRule extends GenericMetricProcessor {\n" +
                                "    public MyMinRule(ProcessMetric processMetric) {\n" +
                                "        super(processMetric, \"mytopic\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected AggregateFunction aggInitializer() {\n" +
                                "        return aggFunction(\"MIN\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {\n" +
                                "        return aggregateTumblingWindow(kGroupedStream,5,MINUTES);\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected JsonNode mapValues(JsonNode value) {\n" +
                                "        return value.path(\"duration\");\n" +
                                "    }\n" +
                                "}"));
    }



    @Test
    public void filterWithFilter() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) WHERE type = \"something\"";
        RuleCode rule = ruleToJava.convert("My_Min_Rule", dsl);
        assertThat(rule)
                .isEqualTo(new RuleCode("MyMinRule",
                        dsl,
                        "io.skalogs.skaetl.metrics.generated.MyMinRule",
                        "package io.skalogs.skaetl.metrics.generated;\n" +
                                "\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;\n" +
                                "import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;\n" +
                                "import io.skalogs.skaetl.domain.ProcessMetric;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.Keys;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.MetricResult;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import javax.annotation.Generated;\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import static io.skalogs.skaetl.domain.RetentionLevel.*;\n" +
                                "\n" +
                                "import org.apache.kafka.streams.kstream.*;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlMetric\")\n" +
                                "public class MyMinRule extends GenericMetricProcessor {\n" +
                                "    public MyMinRule(ProcessMetric processMetric) {\n" +
                                "        super(processMetric, \"mytopic\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected AggregateFunction aggInitializer() {\n" +
                                "        return aggFunction(\"MIN\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {\n" +
                                "        return aggregateTumblingWindow(kGroupedStream,5,MINUTES);\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected JsonNode mapValues(JsonNode value) {\n" +
                                "        return value.path(\"duration\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected boolean filter(String key, JsonNode jsonValue) {\n" +
                                "        return isEqualTo(get(jsonValue,\"type\"),\"something\");\n" +
                                "    }\n" +
                                "}"));
    }


    @Test
    public void groupBy() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) GROUP BY type TO KAFKA targettopic";
        RuleCode rule = ruleToJava.convert("My_Min_Rule", dsl);
        assertThat(rule)
                .isEqualTo(new RuleCode("MyMinRule",
                        dsl,
                        "io.skalogs.skaetl.metrics.generated.MyMinRule",
                        "package io.skalogs.skaetl.metrics.generated;\n" +
                                "\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;\n" +
                                "import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;\n" +
                                "import io.skalogs.skaetl.domain.ProcessMetric;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.Keys;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.MetricResult;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import javax.annotation.Generated;\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import static io.skalogs.skaetl.domain.RetentionLevel.*;\n" +
                                "\n" +
                                "import org.apache.kafka.streams.kstream.*;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlMetric\")\n" +
                                "public class MyMinRule extends GenericMetricProcessor {\n" +
                                "    public MyMinRule(ProcessMetric processMetric) {\n" +
                                "        super(processMetric, \"mytopic\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected AggregateFunction aggInitializer() {\n" +
                                "        return aggFunction(\"MIN\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {\n" +
                                "        return aggregateTumblingWindow(kGroupedStream,5,MINUTES);\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected JsonNode mapValues(JsonNode value) {\n" +
                                "        return value.path(\"duration\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected boolean filterKey(String key, JsonNode value) {\n" +
                                "        return value.hasNonNull(\"type\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected Keys selectKey(String key, JsonNode value) {\n" +
                                "        Keys keys = super.selectKey(key,value);\n" +
                                "        keys.addKey(\"type\", value.get(\"type\").asText());\n" +
                                "        return keys;\n" +
                                "    }\n" +
                                "}"));
    }

    @Test
    public void having() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) HAVING result > 10 TO KAFKA targettopic";
        RuleCode rule = ruleToJava.convert("My_Min_Rule", dsl);
        assertThat(rule)
                .isEqualTo(new RuleCode("MyMinRule",
                        dsl,
                        "io.skalogs.skaetl.metrics.generated.MyMinRule",
                        "package io.skalogs.skaetl.metrics.generated;\n" +
                                "\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;\n" +
                                "import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;\n" +
                                "import io.skalogs.skaetl.domain.ProcessMetric;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.Keys;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.MetricResult;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import javax.annotation.Generated;\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import static io.skalogs.skaetl.domain.RetentionLevel.*;\n" +
                                "\n" +
                                "import org.apache.kafka.streams.kstream.*;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlMetric\")\n" +
                                "public class MyMinRule extends GenericMetricProcessor {\n" +
                                "    public MyMinRule(ProcessMetric processMetric) {\n" +
                                "        super(processMetric, \"mytopic\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected AggregateFunction aggInitializer() {\n" +
                                "        return aggFunction(\"MIN\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {\n" +
                                "        return aggregateTumblingWindow(kGroupedStream,5,MINUTES);\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected JsonNode mapValues(JsonNode value) {\n" +
                                "        return value.path(\"duration\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected boolean having(Windowed<Keys> keys, Double result) {\n" +
                                "        return result > 10;\n" +
                                "    }\n" +
                                "}"));
    }


    @Test
    public void join() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) JOIN mytopic2 ON (userFromA, userFromB)  WINDOWED BY 10 MINUTES TO KAFKA targettopic";
        RuleCode rule = ruleToJava.convert("My_Min_Rule", dsl);
        assertThat(rule)
                .isEqualTo(new RuleCode("MyMinRule",
                        dsl,
                        "io.skalogs.skaetl.metrics.generated.MyMinRule",
                        "package io.skalogs.skaetl.metrics.generated;\n" +
                                "\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;\n" +
                                "import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;\n" +
                                "import io.skalogs.skaetl.domain.ProcessMetric;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.Keys;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.MetricResult;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import javax.annotation.Generated;\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import static io.skalogs.skaetl.domain.RetentionLevel.*;\n" +
                                "\n" +
                                "import org.apache.kafka.streams.kstream.*;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlMetric\")\n" +
                                "public class MyMinRule extends GenericMetricProcessor {\n" +
                                "    public MyMinRule(ProcessMetric processMetric) {\n" +
                                "        super(processMetric, \"mytopic\", \"mytopic2\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected AggregateFunction aggInitializer() {\n" +
                                "        return aggFunction(\"MIN\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {\n" +
                                "        return aggregateTumblingWindow(kGroupedStream,5,MINUTES);\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected JsonNode mapValues(JsonNode value) {\n" +
                                "        return value.path(\"duration\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected Keys selectKey(String key, JsonNode value) {\n" +
                                "        Keys keys = super.selectKey(key,value);\n" +
                                "        keys.addKey(\"userFromA = userFromB\", value.get(\"userFromA\").asText());\n" +
                                "        return keys;\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected Keys selectKeyJoin(String key, JsonNode value) {\n" +
                                "        Keys keys = super.selectKey(key,value);\n" +
                                "        keys.addKey(\"userFromA = userFromB\", value.get(\"userFromB\").asText());\n" +
                                "        return keys;\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected JoinWindows joinWindow() {\n" +
                                "        return JoinWindows.of(MINUTES.toMillis(10));\n" +
                                "    }\n" +
                                "}"));
    }

    @Test
    public void joinWithWhereClause() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) JOIN mytopic2 ON (userFromA, userFromB) WHERE ageDuCapitaine >= 42 WINDOWED BY 10 MINUTES TO KAFKA targettopic";
        RuleCode rule = ruleToJava.convert("My_Min_Rule", dsl);
        assertThat(rule)
                .isEqualTo(new RuleCode("MyMinRule",
                        dsl,
                        "io.skalogs.skaetl.metrics.generated.MyMinRule",
                        "package io.skalogs.skaetl.metrics.generated;\n" +
                                "\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;\n" +
                                "import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;\n" +
                                "import io.skalogs.skaetl.domain.ProcessMetric;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.Keys;\n" +
                                "import io.skalogs.skaetl.rules.metrics.domain.MetricResult;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import javax.annotation.Generated;\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import static io.skalogs.skaetl.domain.RetentionLevel.*;\n" +
                                "\n" +
                                "import org.apache.kafka.streams.kstream.*;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlMetric\")\n" +
                                "public class MyMinRule extends GenericMetricProcessor {\n" +
                                "    public MyMinRule(ProcessMetric processMetric) {\n" +
                                "        super(processMetric, \"mytopic\", \"mytopic2\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected AggregateFunction aggInitializer() {\n" +
                                "        return aggFunction(\"MIN\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {\n" +
                                "        return aggregateTumblingWindow(kGroupedStream,5,MINUTES);\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected JsonNode mapValues(JsonNode value) {\n" +
                                "        return value.path(\"duration\");\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected Keys selectKey(String key, JsonNode value) {\n" +
                                "        Keys keys = super.selectKey(key,value);\n" +
                                "        keys.addKey(\"userFromA = userFromB\", value.get(\"userFromA\").asText());\n" +
                                "        return keys;\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected Keys selectKeyJoin(String key, JsonNode value) {\n" +
                                "        Keys keys = super.selectKey(key,value);\n" +
                                "        keys.addKey(\"userFromA = userFromB\", value.get(\"userFromB\").asText());\n" +
                                "        return keys;\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected JoinWindows joinWindow() {\n" +
                                "        return JoinWindows.of(MINUTES.toMillis(10));\n" +
                                "    }\n" +
                                "    \n" +
                                "    @Override\n" +
                                "    protected boolean filterJoin(String key, JsonNode jsonNode) {\n" +
                                "        return true;\n" +
                                "    }\n" +
                                "}"));
    }

    @Test(expected = SyntaxErrorListener.SyntaxException.class)
    public void wrongSyntax() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava();
        String dsl = "SELECT MIN(duration) GROUP BY type TO targettopic FROM mytopic ";
        ruleToJava.convert("MyMinRule", dsl);
    }

}