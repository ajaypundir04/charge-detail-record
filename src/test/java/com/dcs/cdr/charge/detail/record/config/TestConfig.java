package com.dcs.cdr.charge.detail.record.config;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Collections;

@Profile("test")
@Configuration
public class TestConfig {

    private static RedisServer redisServer;
    private static MongodExecutable mongodExecutable;

    static {
        try {
            startRedisAndMongo();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                stopRedisAndMongo();
            }));
        } catch (IOException e) {
            throw new RuntimeException("Failed to start embedded MongoDB and Redis servers", e);
        }
    }

    @Bean
    public SimpleMongoClientDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/cdr");
    }

    @Bean
    public MongoTemplate mongoTemplate(SimpleMongoClientDatabaseFactory simpleMongoClientDatabaseFactory) {
        return new MongoTemplate(simpleMongoClientDatabaseFactory);
    }

    @Bean
    public SimpleCacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Cache cache = new ConcurrentMapCache("chargeDetailRecords");
        cacheManager.setCaches(Collections.singletonList(cache));
        return cacheManager;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    private static void startRedisAndMongo() throws IOException {
        ImmutableMongodConfig mongodConfig = ImmutableMongodConfig.builder()
                .version(Version.V4_0_12)
                .net(new Net("localhost", 27017, false))
                .build();
        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();

        if (redisServer == null || !redisServer.isActive()) {
            redisServer = new RedisServer(6379);
            redisServer.start();
        }
    }

    private static void stopRedisAndMongo() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }
}
