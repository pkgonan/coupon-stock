package couponsample.common.domain;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public abstract class AbstractReactiveRedisRepository<T, ID, V> implements ReactiveRedisRepository<T, ID> {

    protected final ReactiveRedisTemplate<ID, V> reactiveRedisTemplate;

    public AbstractReactiveRedisRepository(final ReactiveRedisTemplate<ID, V> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<Long> deleteById(ID id) {
        return reactiveRedisTemplate.delete(id);
    }

    @Override
    public Mono<Long> deleteAllById(Iterable<ID> ids) {
        return reactiveRedisTemplate.delete(Flux.fromIterable(ids));
    }

    @Override
    public Mono<Boolean> expireById(ID id, Duration duration) {
        return reactiveRedisTemplate.expire(id, duration);
    }
}



