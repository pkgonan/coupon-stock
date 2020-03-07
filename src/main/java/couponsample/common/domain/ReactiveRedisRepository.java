package couponsample.common.domain;

import reactor.core.publisher.Mono;

import java.time.Duration;

public interface ReactiveRedisRepository<T, ID> extends ReactiveRepository<T, ID> {

    <S extends T> Mono<Boolean> save(S entity, Duration duration);

    Mono<Boolean> expireById(ID id, Duration duration);

}

