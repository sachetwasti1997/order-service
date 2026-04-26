package com.sachet.order_service.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class AvroDeserializer <T extends SpecificRecordBase> implements Deserializer<T> {

    protected final Class<T> targetType;

    public AvroDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //do nothing
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        T returnObject = null;

        try{
            if (data != null) {
                DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(targetType.getDeclaredConstructor().newInstance().getSchema());
                Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
                returnObject = (T) datumReader.read(null, decoder);
                log.info("Deserialized data {}", returnObject.toString());
            }
        } catch (Exception e) {
            log.warn("Unable to deserialize");
        }

        return returnObject;
    }

    @Override
    public void close() {
        // do nothing
    }
}
