package couponsample.stock.domain;

import couponsample.atomiclong.domain.AtomicLong;
import couponsample.atomiclong.domain.AtomicLongRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
class ReactiveStockOperatorImpl implements ReactiveStockOperator {

    private final AtomicLongRepository repository;

    ReactiveStockOperatorImpl(final AtomicLongRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Boolean> set(final String key, final long value) {
        return repository.save(AtomicLong.of(key, value));
    }

    @Override
    public Mono<Boolean> remove(final String key) {
        return repository.delete(key);
    }

    @Override
    public Mono<Void> increase(final String key) {
        return repository.increase(key, 1L).then();
    }

    @Override
    public Mono<Void> increase(final String key, final long value) {
        return repository.increase(key, value).then();
    }

    @Override
    public Mono<Long> increaseAndGet(final String key) {
        return repository.increase(key, 1L);
    }

    @Override
    public Mono<Long> increaseAndGet(final String key, final long value) {
        return repository.increase(key, value);
    }

    @Override
    public Mono<Void> decrease(final String key) {
        return repository.decrease(key, 1L).then();
    }

    @Override
    public Mono<Void> decrease(final String key, final long value) {
        return repository.decrease(key, value).then();
    }

    @Override
    public Mono<Long> decreaseAndGet(final String key) {
        return repository.decrease(key, 1L);
    }

    @Override
    public Mono<Long> decreaseAndGet(final String key, final long value) {
        return repository.decrease(key, value);
    }

    @Override
    public Mono<AtomicLong> get(final String key) {
        return repository.findById(key);
    }

    @Override
    public Flux<AtomicLong> get(final Iterable<String> keys) {
        return repository.findAllById(keys);
    }
}