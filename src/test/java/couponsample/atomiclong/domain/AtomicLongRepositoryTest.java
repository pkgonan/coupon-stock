package couponsample.atomiclong.domain;

import com.google.common.collect.Lists;
import couponsample.test.RedisTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

@Import({AtomicLongRepository.class})
class AtomicLongRepositoryTest extends RedisTest {

    @Autowired
    private AtomicLongRepository repository;

    @Test
    void save() {
        Mono<Boolean> save = repository.save(AtomicLong.of("save", 1L));

        StepVerifier.create(save)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void saveWithDuration() {
        Mono<Boolean> save = repository.save(AtomicLong.of("saveWithDuration", 1L), Duration.ofMillis(1L));

        StepVerifier.create(save)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void saveAll() {
        List<AtomicLong> counters = Lists.newArrayList(AtomicLong.of("saveAll-1", 1L), AtomicLong.of("saveAll-2", 1L));
        Mono<Boolean> save = repository.saveAll(counters);

        StepVerifier.create(save)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void delete() {
        repository.save(AtomicLong.of("delete", 1L)).subscribe();

        Mono<Boolean> delete = repository.delete("delete");
        StepVerifier.create(delete)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void deleteById() {
        repository.save(AtomicLong.of("deleteById", 1L)).subscribe();

        Mono<Long> delete = repository.deleteById("deleteById");
        StepVerifier.create(delete)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void deleteAllById() {
        repository.save(AtomicLong.of("deleteAllById", 1L)).subscribe();

        Mono<Long> delete = repository.deleteAllById(Lists.newArrayList("deleteAllById"));
        StepVerifier.create(delete)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void findById() {
        AtomicLong counter = AtomicLong.of("findById", 1L);
        repository.save(counter).subscribe();

        StepVerifier.create(repository.findById("findById"))
                .expectNext(counter)
                .verifyComplete();
    }

    @Test
    void findAllById() {
        AtomicLong counter1 = AtomicLong.of("findAllById-1", 1L);
        AtomicLong counter2 = AtomicLong.of("findAllById-2", 1L);
        List<AtomicLong> counters = Lists.newArrayList(counter1, counter2);
        repository.saveAll(counters).subscribe();

        StepVerifier.create(repository.findAllById(Lists.newArrayList("findAllById-1", "findAllById-2")))
                .expectNext(counter1)
                .expectNext(counter2)
                .verifyComplete();
    }

    @Test
    void increase() {
        AtomicLong counter = AtomicLong.of("increase", 1L);
        repository.save(counter).subscribe();

        StepVerifier.create(repository.increase("increase", 1L))
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    void decrease() {
        AtomicLong counter = AtomicLong.of("decrease", 1L);
        repository.save(counter).subscribe();

        StepVerifier.create(repository.decrease("decrease", 1L))
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void expire() {
        AtomicLong counter = AtomicLong.of("expire", 1L);
        repository.save(counter).subscribe();

        StepVerifier.create(repository.expireById("expire", Duration.ofMillis(1L)))
                .expectNext(true)
                .verifyComplete();
    }
}