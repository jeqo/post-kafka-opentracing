package io.github.jeqo.posts.infrastructure;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka Producer for Greetings Topic.
 */
public class KafkaHelloWorldProducer {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaHelloWorldProducer.class);
  private static final String GREETINGS_TOPIC = "greetings-topic";

  private final Producer<String, String> kafkaProducer;

  public KafkaHelloWorldProducer(Producer<String, String> kafkaProducer) {
    this.kafkaProducer = kafkaProducer;
  }

  /**
   * Send Greeting for a name to a Topic.
   *
   * @param name Person name to say hi
   */
  public void send(String name) {
    final String greeting = "Hi " + name;
    //Prepare Producer Record to send to the Cluster
    final ProducerRecord<String, String> producerRecord =
        new ProducerRecord<>(
            GREETINGS_TOPIC,
            name,
            greeting);
    //Send Record and add a callback to receive metadata
    kafkaProducer.send(
        producerRecord,
        (metadata, exception) -> {
          if (exception == null) {
            LOGGER.info("Record producer => Topic: {} Partition: {} Offset: {}",
                metadata.topic(), metadata.partition(), metadata.offset());
          } else {
            LOGGER.error(exception.getMessage());
          }
        });
  }
}
