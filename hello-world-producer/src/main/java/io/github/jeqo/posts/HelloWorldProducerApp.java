package io.github.jeqo.posts;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import io.github.jeqo.posts.infrastructure.KafkaHelloWorldProducer;
import io.github.jeqo.posts.resource.HelloWorldResource;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;


/**
 * Dropwizard Application.
 */
public final class HelloWorldProducerApp extends Application<HelloWorldProducerConfig> {

  public static void main(String[] args) throws Exception {
    new HelloWorldProducerApp().run(args);
  }

  @Override
  public String getName() {
    return "hello-world-producer";
  }

  /**
   * Execute dropwizard application.
   *
   * @param config      Dropwizard configuration.
   * @param environment Dropwizard environment.
   * @throws Exception exception at runtime.
   */
  public void run(HelloWorldProducerConfig config, Environment environment)
      throws Exception {
    //Define Kafka Producer Properties
    final Properties producerProperties = new Properties();
    producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    producerProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    //Instantiate Kafka Producer
    final Producer<String, String> kafkaProducer =
        new KafkaProducer<>(producerProperties, new StringSerializer(), new StringSerializer());

    //Inject dependencies and register endpoint
    final KafkaHelloWorldProducer kafkaHelloWorldProducer =
        new KafkaHelloWorldProducer(kafkaProducer);
    final HelloWorldResource helloWorldResource = new HelloWorldResource(kafkaHelloWorldProducer);
    environment.jersey().register(helloWorldResource);
  }
}
