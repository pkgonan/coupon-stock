package couponsample.stock.domain;

import couponsample.counter.domain.Counter;
import couponsample.stock.exception.StockHandleFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Component
public final class StockHandler {

    private final ReactiveStockOperator operator;

    StockHandler(final ReactiveStockOperator operator) {
        this.operator = operator;
    }

    public final boolean tryDecrease(final String key) {
        return tryDecrease(key, 1L);
    }

    public final void safeIncrease(final String key) {
        safeIncrease(key, 1L);
    }

    public final void safeDecrease(final String key) {
        safeDecrease(key, 1L);
    }

    public final boolean tryDecrease(final String key, final long value) {
        final Consumer<Boolean> doOnSuccessConsumer = (safe) -> {
            if (Boolean.FALSE.equals(safe)) {
                increase(key, value);
            }
        };

        return operator.decreaseAndGet(key, value)
                .flatMap(val -> Mono.justOrEmpty(isSafeValue(val)))
                .doOnSuccess(doOnSuccessConsumer)
                .blockOptional().orElse(false);
    }

    public final void safeIncrease(final String key, final long value) {
        final boolean success = operator.increaseAndGet(key, value)
                .hasElement()
                .onErrorReturn(false)
                .blockOptional().orElse(false);

        if (!success) {
            throw new StockHandleFailureException();
        }
    }

    public final void safeDecrease(final String key, final long value) {
        final boolean success = operator.decreaseAndGet(key, value)
                .hasElement()
                .onErrorReturn(false)
                .blockOptional().orElse(false);

        if (!success) {
            throw new StockHandleFailureException();
        }
    }

    public final void increase(final String key) {
        increase(key, 1L);
    }

    public final void decrease(final String key) {
        decrease(key, 1L);
    }

    public final void increase(final String key, final long value) {
        operator.increase(key, value)
                .doOnError(e->log.error("Failure to increase stock. key : {}, value : {}", key, value))
                .subscribe();
    }

    public final void decrease(final String key, final long value) {
        operator.decrease(key, value)
                .doOnError(e->log.error("Failure to decrease stock. key : {}, value : {}", key, value))
                .subscribe();
    }

    public final void safeSet(final String key, final long value) {
        final boolean success = operator.set(key, value)
                .onErrorReturn(false)
                .blockOptional().orElse(false);

        if (!success) {
            throw new StockHandleFailureException();
        }
    }

    public final void safeRemove(final String key) {
        final boolean success = operator.remove(key)
                .onErrorReturn(false)
                .blockOptional().orElse(false);

        if (!success) {
            throw new StockHandleFailureException();
        }
    }

    public final void set(final String key, final long value) {
        operator.set(key, value)
                .doOnError(e->log.error("Failure to set stock. key : {}, value : {}", key, value))
                .subscribe();
    }

    public final void remove(final String key) {
        operator.remove(key)
                .doOnError(e->log.error("Failure to remove stock. key : {}}", key))
                .subscribe();
    }

    public final boolean isIn(final String key) {
        return getCurrent(key)
                .flatMap(stockCounter -> Mono.justOrEmpty(stockCounter.isGreaterThanZero()))
                .blockOptional().orElseGet(() -> {
                    log.error("Stock key is not exist in redis. {}", key);
                    return false;
                });
    }

    public final boolean isNotIn(final String key) {
        return !isIn(key);
    }

    public final Optional<Long> getCurrentRemain(final String key) {
        return getCurrent(key)
                .flatMap(stockCounter -> Mono.justOrEmpty(stockCounter.getValue()))
                .blockOptional();
    }

    private Mono<Counter> getCurrent(final String key) {
        return operator.get(key);
    }

    private static boolean isSafeValue(final long value) {
        return 0L <= value;
    }
}