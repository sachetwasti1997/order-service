package com.sachet.order_service.config.kafka;

import com.sachet.ProductDto;
import com.sachet.order_service.config.EnvironmentConfiguration;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private Map<String, Object> setUpKafkaProperties(EnvironmentConfiguration environmentConfiguration) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environmentConfiguration.getKafkaConfiguration().getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, environmentConfiguration.getKafkaConfiguration().getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroDeserializer.class);
        props.put("schema.registry.url", environmentConfiguration.getKafkaConfiguration().getSchemaRegistryUrl());
        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductDto> productListeners(@Autowired EnvironmentConfiguration environmentConfiguration) {
        ConcurrentKafkaListenerContainerFactory<String, ProductDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> props = setUpKafkaProperties(environmentConfiguration);
        DefaultKafkaConsumerFactory<String, ProductDto> kafkaConsumerFactory =
                new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new AvroDeserializer<>(ProductDto.class));
        factory.setConsumerFactory(kafkaConsumerFactory);
        factory.setConcurrency(3);
        return factory;
    }

}
