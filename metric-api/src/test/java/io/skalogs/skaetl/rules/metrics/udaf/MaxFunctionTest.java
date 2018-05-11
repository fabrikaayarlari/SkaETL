package io.skalogs.skaetl.rules.metrics.udaf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MaxFunctionTest {

    @Test
    public void shouldReturnNaNWhenNoValue() {
        MaxFunction maxFunction =new MaxFunction();
        assertThat(maxFunction.compute()).isEqualTo(Double.NaN);
    }


    @Test
    public void shouldReturnMax() {
        MaxFunction maxFunction =new MaxFunction();
        maxFunction.addValue(100);
        maxFunction.addValue(3);
        assertThat(maxFunction.compute()).isEqualTo(100);
    }

}