package com.sachet.order_service;

import com.github.sonus21.rqueue.spring.EnableRqueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableCaching
@EnableRqueue
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		// Configure your Redis server details here
		return new LettuceConnectionFactory("redis-0.redis.default.svc.cluster.local", 6379);
	}

}
