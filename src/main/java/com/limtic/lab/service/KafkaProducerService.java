package com.limtic.lab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "notifications";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper; // for JSON serialization


    public void sendMessage(Object payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(TOPIC, message);
            System.out.println("✅ Sent JSON message to Kafka -> " + message);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to serialize Kafka message", e);
        }
    }
}
