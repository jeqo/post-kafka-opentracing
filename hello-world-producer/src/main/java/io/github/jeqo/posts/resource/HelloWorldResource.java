package io.github.jeqo.posts.resource;

import io.github.jeqo.posts.infrastructure.KafkaHelloWorldProducer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Hello HTTP Endpoint.
 */
@Path("hello")
public class HelloWorldResource {
  private final KafkaHelloWorldProducer producer;

  public HelloWorldResource(KafkaHelloWorldProducer producer) {
    this.producer = producer;
  }

  @GET
  @Path("{name}")
  public Response sayHi(@PathParam("name") final String name) {
    producer.send(name);
    return Response.accepted().build();
  }
}
