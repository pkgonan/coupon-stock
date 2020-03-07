package couponsample.atomiclong.domain;

import com.google.common.collect.Lists;
import couponsample.common.domain.AbstractReactiveRedisValueRepository;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public class AtomicLongRepository extends AbstractReactiveRedisValueRepository<AtomicLong, String, Long> {

    public AtomicLongRepository(final ReactiveRedisTemplate<String, Long> reactiveRedisTemplate) {
        super(reactiveRedisTemplate);
    }

    @Override
    public <S extends AtomicLong> Mono<Boolean> save(S entity) {
        return reactiveValueOperations.set(entity.getId(), entity.getValue());
    }

    @Override
    public <S extends AtomicLong> Mono<Boolean> saveAll(Iterable<S> entities) {
        final Map<String, Long> values = streamSupport(entities).collect(Collectors.toMap(S::getId, S::getValue));
        return reactiveValueOperations.multiSet(values);
    }

    @Override
    public <S extends AtomicLong> Mono<Boolean> save(S entity, Duration duration) {
        return reactiveValueOperations.set(entity.getId(), entity.getValue(), duration);
    }

    @Override
    public Mono<AtomicLong> findById(String id) {
        return reactiveValueOperations.get(id).flatMap(value -> wrap(id, value));
    }

    @Override
    public Flux<AtomicLong> findAllById(Iterable<String> ids) {
        final List<String> idList = Lists.newArrayList(ids);
        return reactiveValueOperations.multiGet(idList)
                .flatMap(values -> Mono.justOrEmpty(IntStream.range(0, idList.size())
                        .mapToObj(i -> AtomicLong.of(idList.get(i), values.get(i)))
                        .collect(Collectors.toList())))
                .flatMapMany(Flux::fromIterable);
    }

    private Mono<AtomicLong> wrap(final String id, final Long value) {
        return Mono.justOrEmpty(AtomicLong.of(id, value));
    }

    private <S extends AtomicLong> Stream<S> streamSupport(final Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false);
    }
}