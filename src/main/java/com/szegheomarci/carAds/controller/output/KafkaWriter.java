package com.szegheomarci.carAds.controller.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szegheomarci.carAds.model.Car;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.yaml.snakeyaml.Yaml;

import java.text.SimpleDateFormat;
import java.util.*;

public class KafkaWriter extends OutputWriter {
    private final Properties properties;
    private String topic;

    public KafkaWriter(String outputconfig) {
        super(outputconfig);
        String kafkaConfig = yaml.dump(config.get("config"));
        this.properties = collectProperties(kafkaConfig);
    }

    @Override
    public void generateOutput(ArrayList<Car> resultset) {
        // Create the Kafka producer instance
        Producer<String, String> producer = new KafkaProducer<>(properties);

        // Create a map to hold metadata and the list of cars
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("date", new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        resultMap.put("results", resultset);

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();


        try {
            // Create a message to send
            String key = "key2";
            String value = "Hello, Kafka!";

            // Send the message to the topic
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            ProducerRecord<String, String> otherRecord = new ProducerRecord<>(topic, objectMapper.writeValueAsString(resultMap));
            System.out.println("sending...");
            producer.send(record);
            producer.send(otherRecord);

            System.out.println("Message sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the producer
            producer.close();
        }
    }

    private Properties collectProperties(String kafkaConfig) {
        // Read configuration
        Yaml yaml = new Yaml();
        Map<String, Object> configSource = yaml.load(kafkaConfig);
        String bootstrapServers = (String) configSource.get("bootstrapServers");
        String username = (String) configSource.get("username");
        String password = (String) configSource.get("password");
        topic = (String) configSource.get("topic");

        String keySerializer = (String) configSource.getOrDefault("keySerializer", StringSerializer.class.getName());
        String valueSerializer = (String) configSource.getOrDefault("valueSerializer", StringSerializer.class.getName());
        String securityProtocol = (String) configSource.getOrDefault("securityProtocol", "SASL_SSL");
        String saslMechanism = (String) configSource.getOrDefault("securityProtocol", "SCRAM-SHA-256");
        String jaasTemplate = (String) configSource.getOrDefault("jaasTemplate", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";");

        String saslJaasConfig  = String.format(jaasTemplate, username, password);

        // Create object to return
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("key.serializer", keySerializer);
        properties.put("value.serializer", valueSerializer);
        properties.put("security.protocol", securityProtocol);
        properties.put("sasl.mechanism", saslMechanism);
        properties.put("sasl.jaas.config", saslJaasConfig);
        properties.put("enable.idempotence", "false");

        return properties;
    }

}
