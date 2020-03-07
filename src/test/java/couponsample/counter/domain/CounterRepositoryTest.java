package couponsample.counter.domain;

import com.google.common.collect.Lists;
import couponsample.test.RedisTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

@Import({CounterRepository.class})
class CounterRepositoryTest extends RedisTest {

    @Autowired
    private CounterRepository repository;

    @Test
    void save() {
        Mono<Boolean> save = repository.save(Counter.of("save", 1L));

        StepVerifier.create(save)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void saveWithDuration() {
        Mono<Boolean> save = repository.save(Counter.of("saveWithDuration", 1L), Duration.ofMillis(1L));

        StepVerifier.create(save)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void saveAll() {
        List<Counter> counters = Lists.newArrayList(Counter.of("saveAll-1", 1L), Counter.of("saveAll-2", 1L));
        Mono<Boolean> save = repository.saveAll(counters);

        StepVerifier.create(save)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void delete() {
        repository.save(Counter.of("delete", 1L)).subscribe();

        Mono<Boolean> delete = repository.delete("delete");
        StepVerifier.create(delete)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void deleteById() {
        repository.save(Counter.of("deleteById", 1L)).subscribe();

        Mono<Long> delete = repository.deleteById("deleteById");
        StepVerifier.create(delete)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void deleteAllById() {
        repository.save(Counter.of("deleteAllById", 1L)).subscribe();

        Mono<Long> delete = repository.deleteAllById(Lists.newArrayList("deleteAllById"));
        StepVerifier.create(delete)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void findById() {
        Counter counter = Counter.of("findById", 1L);
        repository.save(counter).subscribe();

        StepVerifier.create(repository.findById("findById"))
                .expectNext(counter)
                .verifyComplete();
    }

    @Test
    void findAllById() {
        Counter counter1 = Counter.of("findAllById-1", 1L);
        Counter counter2 = Counter.of("findAllById-2", 1L);
        List<Counter> counters = Lists.newArrayList(counter1, counter2);
        repository.saveAll(counters).subscribe();

        StepVerifier.create(repository.findAllById(Lists.newArrayList("findAllById-1", "findAllById-2")))
                .expectNext(counter1)
                .expectNext(counter2)
                .verifyComplete();
    }

    @Test
    void increase() {
        Counter counter = Counter.of("increase", 1L);
        repository.save(counter).subscribe();

        StepVerifier.create(repository.increase("increase", 1L))
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    void decrease() {
        Counter counter = Counter.of("decrease", 1L);
        repository.save(counter).subscribe();

        StepVerifier.create(repository.decrease("decrease", 1L))
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void expire() {
        Counter counter = Counter.of("expire", 1L);
        repository.save(counter).subscribe();

        StepVerifier.create(repository.expireById("expire", Duration.ofMillis(1L)))
                .expectNext(true)
                .verifyComplete();
    }
}