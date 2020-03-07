package couponsample.stock.domain;

import couponsample.atomiclong.domain.AtomicLong;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveStockOperator {

    Mono<Boolean> set(String key, long total);

    Mono<Boolean> remove(String key);

    Mono<Void> increase(String key);

    Mono<Void> increase(String key, long value);

    Mono<Long> increaseAndGet(String key);

    Mono<Long> increaseAndGet(String key, long value);

    Mono<Void> decrease(String key);

    Mono<Void> decrease(String key, long value);

    Mono<Long> decreaseAndGet(String key);

    Mono<Long> decreaseAndGet(String key, long value);

    Mono<AtomicLong> get(String key);

    Flux<AtomicLong> get(Iterable<String> keys);

}