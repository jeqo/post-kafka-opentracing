package io.github.jeqo.posts;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import io.github.jeqo.posts.infrastructure.KafkaGreetingsHandler;
import io.opentracing.Tracer;
import io.opentracing.contrib.kafka.TracingKafkaConsumer;
import io.opentracing.util.GlobalTracer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Dropwizard Application.
 */
public final class HelloWorldConsumerApp extends Application<HelloWorldConsumerConfig> {

  public static void main(String[] args) throws Exception {
    new HelloWorldConsumerApp().run(args);
  }

  @Override
  public String getName() {
    return "hello-world-consumer";
  }

  public void run(HelloWorldConsumerConfig config, Environment environment)
      throws Exception {
    //Instantiate and register Tracer
    final Tracer tracer =
        new com.uber.jaeger.Configuration(
            getName(),
            new com.uber.jaeger.Configuration.SamplerConfiguration("const", 1),
            new com.uber.jaeger.Configuration.ReporterConfiguration(
                true,  // logSpans
                "localhost",
                6831,
                1000,   // flush interval in milliseconds
                10000)  /*max buffered Spans*/)
            .getTracer();
    GlobalTracer.register(tracer);

    //Kafka Consumer Configuration
    final Properties consumerConfigs = new Properties();
    consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, getName());
    consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerConfigs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    //Instantiate Kafka Consumer
    final KafkaConsumer<String, String> kafkaConsumer =
        new KafkaConsumer<String, String>(
            consumerConfigs,
            new StringDeserializer(),
            new StringDeserializer());
    final TracingKafkaConsumer<String, String> tracingKafkaConsumer =
        new TracingKafkaConsumer<>(kafkaConsumer, tracer);

    //Define Runnable Handler
    final KafkaGreetingsHandler greetingsHandler =
        new KafkaGreetingsHandler(tracingKafkaConsumer);

    //Submit runnable to Executor Service
    final ExecutorService executorService =
        environment.lifecycle().executorService("kafka-consumer").build();
    executorService.submit(greetingsHandler);
  }
}
