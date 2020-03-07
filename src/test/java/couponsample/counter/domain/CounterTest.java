package couponsample.counter.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CounterTest {

    @Test
    void isGreaterThanZeroOneValue() {
        Assertions.assertTrue(Counter.of("test", 1L).isGreaterThanZero());
    }

    @Test
    void isGreaterThanZeroZeroValue() {
        Assertions.assertFalse(Counter.of("test", 0L).isGreaterThanZero());
    }

    @Test
    void isGreaterThanZeroNullValue() {
        Assertions.assertFalse(Counter.of("test", null).isGreaterThanZero());
    }

}