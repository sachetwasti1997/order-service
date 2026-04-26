package com.sachet.order_service.config.kafka.consumer;

import com.sachet.ProductDto;
import com.sachet.order_service.config.EnvironmentConfiguration;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private ConsumerFactory<String, Object> setUpKafkaProperties(EnvironmentConfiguration environmentConfiguration) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environmentConfiguration.getKafkaConfiguration().getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, environmentConfiguration.getKafkaConfiguration().getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put("schema.registry.url", environmentConfiguration.getKafkaConfiguration().getSchemaRegistryUrl());
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        return new DefaultKafkaConsumerFactory<>(props);
    }

//    @Primary
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> productListeners(@Autowired EnvironmentConfiguration environmentConfiguration) {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(setUpKafkaProperties(environmentConfiguration));
//        return factory;
//    }

}
