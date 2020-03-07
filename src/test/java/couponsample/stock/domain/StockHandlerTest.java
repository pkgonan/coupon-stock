package couponsample.stock.domain;

import couponsample.counter.domain.Counter;
import couponsample.stock.exception.StockHandleFailureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.mockito.Mockito.*;

class StockHandlerTest {

    private ReactiveStockOperator operator;
    private StockHandler handler;

    @BeforeEach
    void before() {
        operator = mock(ReactiveStockOperator.class);
        handler = new StockHandler(operator);
    }

    @Test
    void tryDecrease_success() {
        doReturn(Mono.just(0L)).when(operator).decreaseAndGet("tryDecrease", 1L);

        boolean tryDecrease = handler.tryDecrease("tryDecrease", 1L);
        Assertions.assertTrue(tryDecrease);

        verify(operator, times(1)).decreaseAndGet("tryDecrease", 1L);
    }

    @Test
    void tryDecrease_failure_out_of_stock_recovery() {
        doReturn(Mono.just(-1L)).when(operator).decreaseAndGet("tryDecrease", 1L);
        doReturn(Mono.empty()).when(operator).increase("tryDecrease", 1L);

        boolean tryDecrease = handler.tryDecrease("tryDecrease", 1L);
        Assertions.assertFalse(tryDecrease);

        /** Verify recovery **/
        verify(operator, times(1)).decreaseAndGet("tryDecrease", 1L);
        verify(operator, times(1)).increase("tryDecrease", 1L);
    }

    @Test
    void safeIncrease_success() {
        doReturn(Mono.justOrEmpty(1L)).when(operator).increaseAndGet("safeIncrease", 1L);

        handler.safeIncrease("safeIncrease", 1L);

        verify(operator, times(1)).increaseAndGet("safeIncrease", 1L);
    }

    @Test
    void safeIncrease_failure() {
        doReturn(Mono.justOrEmpty(null)).when(operator).increaseAndGet("safeIncrease", 1L);

        Assertions.assertThrows(StockHandleFailureException.class, () -> handler.safeIncrease("safeIncrease", 1L));
        verify(operator, times(1)).increaseAndGet("safeIncrease", 1L);
    }

    @Test
    void safeDecrease_success() {
        doReturn(Mono.justOrEmpty(1L)).when(operator).decreaseAndGet("safeDecrease", 1L);

        handler.safeDecrease("safeDecrease", 1L);

        verify(operator, times(1)).decreaseAndGet("safeDecrease", 1L);
    }

    @Test
    void safeDecrease_failure() {
        doReturn(Mono.justOrEmpty(null)).when(operator).decreaseAndGet("safeDecrease", 1L);

        Assertions.assertThrows(StockHandleFailureException.class, () -> handler.safeDecrease("safeDecrease", 1L));

        verify(operator, times(1)).decreaseAndGet("safeDecrease", 1L);
    }

    @Test
    void increase_key() {
        doReturn(Mono.empty()).when(operator).increase("increase", 1L);

        handler.increase("increase", 1L);

        verify(operator, times(1)).increase("increase", 1L);
    }

    @Test
    void decrease_key() {
        doReturn(Mono.empty()).when(operator).decrease("decrease", 1L);

        handler.decrease("decrease", 1L);

        verify(operator, times(1)).decrease("decrease", 1L);
    }

    @Test
    void increase_key_value() {
        doReturn(Mono.empty()).when(operator).increase("increase", 10L);

        handler.increase("increase", 10L);

        verify(operator, times(1)).increase("increase", 10L);
    }

    @Test
    void decrease_key_value() {
        doReturn(Mono.empty()).when(operator).decrease("decrease", 10L);

        handler.decrease("decrease", 10L);

        verify(operator, times(1)).decrease("decrease", 10L);
    }

    @Test
    void safeSet_success() {
        doReturn(Mono.justOrEmpty(Boolean.TRUE)).when(operator).set("safeSet", 1L);

        handler.safeSet("safeSet", 1L);

        verify(operator, times(1)).set("safeSet", 1L);
    }

    @Test
    void safeSet_failure() {
        doReturn(Mono.justOrEmpty(Boolean.FALSE)).when(operator).set("safeSet", 1L);

        Assertions.assertThrows(StockHandleFailureException.class, () -> handler.safeSet("safeSet", 1L));

        verify(operator, times(1)).set("safeSet", 1L);
    }

    @Test
    void safeRemove_success() {
        doReturn(Mono.justOrEmpty(Boolean.TRUE)).when(operator).set("safeRemove", 1L);

        handler.safeSet("safeRemove", 1L);

        verify(operator, times(1)).set("safeRemove", 1L);
    }

    @Test
    void safeRemove_failure() {
        doReturn(Mono.justOrEmpty(Boolean.FALSE)).when(operator).set("safeRemove", 1L);

        Assertions.assertThrows(StockHandleFailureException.class, () -> handler.safeSet("safeRemove", 1L));

        verify(operator, times(1)).set("safeRemove", 1L);
    }

    @Test
    void set() {
        doReturn(Mono.justOrEmpty(Boolean.TRUE)).when(operator).set("set", 1L);

        handler.set("set", 1L);

        verify(operator, times(1)).set("set", 1L);
    }

    @Test
    void remove() {
        doReturn(Mono.justOrEmpty(Boolean.TRUE)).when(operator).remove("set");

        handler.remove("set");

        verify(operator, times(1)).remove("set");
    }

    @Test
    void isIn_true_positive_value() {
        Counter counter = Counter.of("isIn", 1L);
        doReturn(Mono.just(counter)).when(operator).get("isIn");

        boolean isIn = handler.isIn("isIn");
        Assertions.assertTrue(isIn);

        verify(operator, times(1)).get("isIn");
    }

    @Test
    void isIn_false_zero_value() {
        Counter counter = Counter.of("isIn", 0L);
        doReturn(Mono.just(counter)).when(operator).get("isIn");

        boolean isIn = handler.isIn("isIn");
        Assertions.assertFalse(isIn);

        verify(operator, times(1)).get("isIn");
    }

    @Test
    void isIn_false_negative_value() {
        Counter counter = Counter.of("isIn", -1L);
        doReturn(Mono.just(counter)).when(operator).get("isIn");

        boolean isIn = handler.isIn("isIn");
        Assertions.assertFalse(isIn);

        verify(operator, times(1)).get("isIn");
    }

    @Test
    void isIn_false_null_value() {
        Counter counter = Counter.of("isIn", null);
        doReturn(Mono.just(counter)).when(operator).get("isIn");

        boolean isIn = handler.isIn("isIn");
        Assertions.assertFalse(isIn);

        verify(operator, times(1)).get("isIn");
    }

    @Test
    void getCurrentRemain() {
        Counter counter = Counter.of("getCurrentRemain", 1L);
        doReturn(Mono.just(counter)).when(operator).get("getCurrentRemain");

        Optional<Long> optionalCurrentRemain = handler.getCurrentRemain("getCurrentRemain");

        Assertions.assertTrue(optionalCurrentRemain.isPresent());
        Assertions.assertEquals(1L, optionalCurrentRemain.get());

        verify(operator, times(1)).get("getCurrentRemain");
    }
}