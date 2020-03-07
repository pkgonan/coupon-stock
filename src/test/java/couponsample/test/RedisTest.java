package couponsample.test;

import couponsample.config.RedisConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisCluster;

import java.util.Arrays;

@Import({RedisConfig.class})
@ActiveProfiles("test")
@DataRedisTest
public abstract class RedisTest {

    private static RedisCluster redisCluster;
    private static int PORT = 6379;

    @BeforeAll
    static void beforeAll() {
        redisCluster = RedisCluster.builder()
                .sentinelCount(0)
                .serverPorts(Arrays.asList(PORT))
                .replicationGroup("master", 0)
                .build();
        redisCluster.start();
    }

    @AfterAll
    static void afterAll() {
        if (redisCluster != null) {
            redisCluster.stop();
        }
    }
}