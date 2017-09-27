# Post: Tracing Kafka Applications

Tracing is one of the hardest time in integration or microservice development: 
knowing how a request impact your different components, and if your components
have behave as expected. 

This could be fairly easy if we have monolith where we have one database and
with some queries or checking one log file you can validate everything went 
well. 

Once you introduce distributed components and asynchronous communication 
this starts to get more complex and tedious.

OpenTracing (http://opentracing.io/) offers a way to solve this bringing a 
vendor neutral API to be able to create `spans` that represent the meaningful
tasks that form part of a `trace`.

## Use-Case

In this post we will see how to instrument Java applications that communicates 
using Apache Kafka as Event Log, and how visualize traces and analyze latency.

## Technologies

* **Dropwizard**: Framework to run Web Services.

* **Apache Kafka**: Event Log.

* **OpenTracing and Jaeger**: API and instrumentation for distributed tracing.

## Implementation


