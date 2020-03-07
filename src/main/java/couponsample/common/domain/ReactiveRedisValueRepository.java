package couponsample.common.domain;

import reactor.core.publisher.Mono;

public interface ReactiveRedisValueRepository<T, ID> extends ReactiveRedisRepository<T, ID> {

    Mono<Long> increase(ID id, long delta);

    Mono<Long> decrease(ID id, long delta);

    Mono<Boolean> delete(ID id);

}


