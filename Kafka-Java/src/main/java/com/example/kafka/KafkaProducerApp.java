package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerApp {

    public static void main(String[] args) {
        System.out.println("Starting Kafka Producer...");

        // Setup properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create the producer
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            // Create a producer record
            ProducerRecord<String, String> record = new ProducerRecord<>("test_topic", "hello_java", "Hello from Java!");

            // Send data - asynchronous
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.printf("Successfully sent message to topic: %s, partition: %d, offset: %d%n",
                            metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    System.err.println("Error while producing: " + exception.getMessage());
                }
            });

            // Flush and close is handled by try-with-resources
            producer.flush();
        } catch (Exception e) {
            System.err.println("Producer failed: " + e.getMessage());
        }
    }
}
