package com.sachet.order_service.config.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaConfiguration {
    private String bootstrapServers;
    private String groupId;
    private String schemaRegistryUrl;
}
