package com.zepox.EcommerceWebApp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableCaching
public class RedisConfig {
    private final ObjectMapper objectMapper;
    public RedisConfig(@Qualifier("objectMapperForRedis") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // Create serializer with the configured ObjectMapper
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        // Configure cache with serializers
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                );

        Map<String, RedisCacheConfiguration> cacheConfigurations  = new HashMap<>();

//        User Related Caching TTL
        cacheConfigurations.put("USER_MYSELF", config.entryTtl(Duration.ofMinutes(10)));

        cacheConfigurations.put("ALL_USERS", config.entryTtl(Duration.ofMinutes(10)));

        cacheConfigurations.put("USER_BY_ID",config.entryTtl(Duration.ofMinutes(10)));

        cacheConfigurations.put("USER_SEARCH_BY_ID",config.entryTtl(Duration.ofMinutes(10)));

        cacheConfigurations.put("USER_SEARCH_BY_NAME",config.entryTtl(Duration.ofMinutes(10)));

//        Product Related Caching TTL
        cacheConfigurations.put("PRODUCT_LIST",config.entryTtl(Duration.ofMinutes(10)));

        cacheConfigurations.put("PRODUCT",config.entryTtl(Duration.ofMinutes(5)));

        cacheConfigurations.put("PRODUCT_SEARCH",config.entryTtl(Duration.ofSeconds(30)));

        cacheConfigurations.put("ALL_PRODUCTS",config.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
