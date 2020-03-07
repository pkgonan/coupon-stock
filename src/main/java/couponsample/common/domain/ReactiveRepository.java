package couponsample.common.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveRepository<T, ID> {

    <S extends T> Mono<Boolean> save(S entity);

    <S extends T> Mono<Boolean> saveAll(Iterable<S> entities);

    Mono<T> findById(ID id);

    Flux<T> findAllById(Iterable<ID> ids);

    Mono<Long> deleteById(ID id);

    Mono<Long> deleteAllById(Iterable<ID> ids);

}

