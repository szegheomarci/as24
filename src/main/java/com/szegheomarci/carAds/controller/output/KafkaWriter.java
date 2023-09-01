package com.szegheomarci.carAds.controller.output;

import com.szegheomarci.carAds.model.Car;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import java.util.ArrayList;

public class KafkaWriter extends OutputWriter {

    public KafkaWriter(String outputconfig) {
        super(outputconfig);
    }

    @Override
    public void generateOutput(ArrayList<Car> resultset) {

        String brokers = "dory.srvs.cloudkafka.com:9094";
        String username = "dbetpols";
        String password = "3AUQkXCP5qHyP0IVa1vdc1vG8V2RzXJL";
        String topic = "dbetpols-proba";

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        String serializer = StringSerializer.class.getName();
        //props = new Properties();
        //  props.put("bootstrap.servers", brokers);
        //props.put("group.id", username + "-consumer");
        //props.put("enable.auto.commit", "true");
        //props.put("auto.commit.interval.ms", "1000");
        //props.put("auto.offset.reset", "earliest");
        //props.put("session.timeout.ms", "30000");
        //props.put("key.deserializer", deserializer);
        //props.put("value.deserializer", deserializer);
        // Configure the Kafka producer properties
        Properties properties = new Properties();
        properties.put("logging.level.org.apache.kafka","WARN");
        properties.put("bootstrap.servers", brokers);
        //properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("key.serializer", serializer);
        properties.put("value.serializer", serializer);
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("sasl.jaas.config", jaasCfg);

       // Add SASL authentication properties
        //properties.put("security.protocol", "SASL_SSL");
        //properties.put("sasl.mechanism", "SCRAM-SHA-512");

        //properties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"dbetpols\" password=\"3AUQkXCP5qHyP0IVa1vdc1vG8V2RzXJL\";");

        //properties.put("sasl.jaas.config", org.apache.kafka.common.security.plain.PlainLoginModule.class.getName() + " required username=\"dbetpols\" password=\"3AUQkXCP5qHyP0IVa1vdc1vG8V2RzXJL\";");

        // Create the Kafka producer instance
        Producer<String, String> producer = new KafkaProducer<>(properties);


        try {
            // Create a message to send
            String key = "key2";
            String value = "Hello, Kafka!";

            // Send the message to the topic
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            ProducerRecord<String, String> otherRecord = new ProducerRecord<>(topic,"message");
            System.out.println("sending...");
            producer.send(record);
            //producer.send(otherRecord);

            System.out.println("Message sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the producer
            producer.close();
        }
    }

}
