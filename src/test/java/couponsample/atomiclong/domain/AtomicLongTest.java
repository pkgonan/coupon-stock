package couponsample.atomiclong.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AtomicLongTest {

    @Test
    void isGreaterThanZeroOneValue() {
        Assertions.assertTrue(AtomicLong.of("test", 1L).isGreaterThanZero());
    }

    @Test
    void isGreaterThanZeroZeroValue() {
        Assertions.assertFalse(AtomicLong.of("test", 0L).isGreaterThanZero());
    }

    @Test
    void isGreaterThanZeroNullValue() {
        Assertions.assertFalse(AtomicLong.of("test", null).isGreaterThanZero());
    }

}