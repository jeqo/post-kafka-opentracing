package io.github.jeqo.posts.infrastructure;

import io.opentracing.ActiveSpan;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.util.GlobalTracer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Collections;

/**
 *
 */
public final class KafkaGreetingsHandler implements Runnable {

  private static final String GREETINGS_TOPIC = "greetings-topic";
  private final Consumer<String, String> kafkaConsumer;
  private final Tracer tracer;

  public KafkaGreetingsHandler(Consumer<String, String> kafkaConsumer) {
    this.kafkaConsumer = kafkaConsumer;
    tracer = GlobalTracer.get();
  }

  public void run() {
    kafkaConsumer.subscribe(Collections.singletonList(GREETINGS_TOPIC));

    try {
      while (true) {
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Long.MAX_VALUE);

        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
          final SpanContext context =
              TracingKafkaUtils.extractSpanContext(consumerRecord.headers(), tracer);
          try (ActiveSpan ignored =
                   tracer.buildSpan("consumption")
                       .withTag("user", consumerRecord.key())
                       .asChildOf(context)
                       .startActive()) {
            System.out.println(consumerRecord.value());

            kafkaConsumer.commitSync();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
