package com.project.date.config;

import com.project.date.config.handler.CacheKey;
import com.project.date.repository.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableCaching
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(Integer.parseInt(redisPort));
        redisStandaloneConfiguration.setPassword(redisPassword);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "cacheManager")
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // null value ????????????
                .entryTtl(Duration.ofSeconds(CacheKey.DEFAULT_EXPIRE_SEC)) // ????????? ?????? ???????????? ??????
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())); // redis ?????? ????????? ??????????????? StringSeriallizer??? ??????

// ???????????? default ???????????? ??????
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(CacheKey.ARTICLE, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.ARTICLE_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.ARTICLES, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.ARTICLE_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.SEARCHPOP, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.ARTICLE_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.SERACH, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.ARTICLE_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.ARTICLEPOP, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.ARTICLE_EXPIRE_SEC)));
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory).cacheDefaults(configuration)
                .withInitialCacheConfigurations(cacheConfigurations).build();
    }


    /**
     * redis pub/sub ???????????? ???????????? listener ??????
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter,
            ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // ????????? topic??? Repository??? ????????? ?????? ?????? channelTopic??? DI??? ?????? ?????????????????? ??????
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    /**
     * ???????????????????????? ????????? redisTemplate ??????
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

    /**
     * ?????? ???????????? ???????????? subscriber ?????? ??????
     * ?????? ???????????? ?????? ????????? RedisSubscriber ???????????? ??????????????? ?????????
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }


    /**
     * ?????? Topic ????????? ?????? Bean ??????
     * ???????????? ????????? ????????? ?????? ?????? ????????? ?????????
     */
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }
}
