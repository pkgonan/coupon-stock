package couponsample.common.domain;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;

public abstract class AbstractReactiveRedisValueRepository<T, ID, V> extends AbstractReactiveRedisRepository<T, ID, V>
        implements ReactiveRedisValueRepository<T, ID> {

    protected final ReactiveValueOperations<ID, V> reactiveValueOperations;

    public AbstractReactiveRedisValueRepository(final ReactiveRedisTemplate<ID, V> reactiveRedisTemplate) {
        super(reactiveRedisTemplate);
        this.reactiveValueOperations = reactiveRedisTemplate.opsForValue();
    }

    @Override
    public Mono<Long> increase(final ID id, final long delta) {
        return reactiveValueOperations.increment(id, delta);
    }

    @Override
    public Mono<Long> decrease(final ID id, final long delta) {
        return reactiveValueOperations.decrement(id, delta);
    }

    @Override
    public Mono<Boolean> delete(final ID id) {
        return reactiveValueOperations.delete(id);
    }
}



