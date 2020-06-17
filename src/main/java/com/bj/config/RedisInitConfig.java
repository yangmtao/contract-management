package com.bj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 *
 */
@Configuration
public class RedisInitConfig {
  

    @Bean
    public JedisPool redisPoolFactory(@Value("${spring.redis.host}") String host,
                                      @Value("${spring.redis.password}") String password,
                                      @Value("${spring.redis.port}")  int port,
                                      @Value("${user.redis.timeout}") int timeout,
                                      @Value("${user.redis.max-idle}") int maxIdle,
                                      @Value("${user.redis.max-wait}") long maxWaitMillis,
                                      @Value("${user.redis.min-idle}") int minIdle,
                                      @Value("${user.redis.max-total}") int maxTotal
    ) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
            jedisPoolConfig.setMinIdle(minIdle);
            jedisPoolConfig.setMaxTotal(maxTotal);
            JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
            return jedisPool;
    }

}
