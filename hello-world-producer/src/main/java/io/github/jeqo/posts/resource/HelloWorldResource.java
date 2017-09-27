package io.github.jeqo.posts.resource;

import io.github.jeqo.posts.infrastructure.KafkaHelloWorldProducer;
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.contrib.dropwizard.DropWizardTracer;
import io.opentracing.contrib.dropwizard.Trace;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * Hello HTTP Endpoint.
 */
@Path("hello")
public class HelloWorldResource {
  private final DropWizardTracer dropWizardTracer;
  private final KafkaHelloWorldProducer producer;

  public HelloWorldResource(final DropWizardTracer dropWizardTracer,
                            final KafkaHelloWorldProducer producer) {
    this.dropWizardTracer = dropWizardTracer;
    this.producer = producer;
  }

  @GET
  @Path("{name}")
  @Trace
  public Response sayHi(@Context final Request request,
                        @PathParam("name") final String name) {
    final Span span = dropWizardTracer.getSpan(request);

    try (ActiveSpan ignored =
             dropWizardTracer.getTracer()
                 .buildSpan("sayHi")
                 .asChildOf(span)
                 .withTag("user", name)
                 .startActive()) {
      producer.send(name);
      return Response.accepted("done.").build();
    }
  }
}
