package io.github.jeqo.posts.infrastructure;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Collections;

/**
 *
 */
public final class KafkaGreetingsHandler implements Runnable {

  static final String GREETINGS_TOPIC = "greetings-topic";
  private final Consumer<String, String> kafkaConsumer;

  public KafkaGreetingsHandler(Consumer<String, String> kafkaConsumer) {
    this.kafkaConsumer = kafkaConsumer;
  }

  public void run() {
    kafkaConsumer.subscribe(Collections.singletonList(GREETINGS_TOPIC));

    try {
      while (true) {
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Long.MAX_VALUE);

        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
          System.out.println(consumerRecord.value());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
