package kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import util.TrackedLogger;

import java.util.Properties;

/**
 * @author sanketkorgaonkar
 */
public class Transmitter {
    private Producer<String, byte[]> earthChannel    = null;
    private TrackedLogger            logger          = new TrackedLogger(Transmitter.class);
    private Properties               kafkaProperties = null;

    public Transmitter(Properties comsConfig) {
        kafkaProperties = comsConfig;
        kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        earthChannel = new KafkaProducer<>(kafkaProperties);
    }

    public void transmitMessage(byte[] message) {
        earthChannel.send(
                new ProducerRecord<>(kafkaProperties.getProperty("destination.topic"), message));
        logger.info("Sending information packet to " + kafkaProperties.getProperty("destination.topic"));
    }
}

