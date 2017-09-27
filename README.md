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

The scenario will be a simple Hello World Producer/Consumer. The producer
side will send a "greeting" event to "greetings-topic", and the consumer
side will poll events and print out to console.

## Technologies

* **Dropwizard**: Framework to run Web Services.

* **Apache Kafka**: Event Log.

* **OpenTracing and Jaeger**: API and instrumentation for distributed tracing.

## Implementation

---
Branch: steps/step-01
---

Let's assume we have a simple application that produce `greetings` events
and another application that consumes and print those events.

To execute this applications let's first start a local version of Kafka:

### Install and Run Kafka

First, execute the script to install Kafka:

```bash
cd kafka/
./install-kafka.sh
```

Once it is installed, start Zookeeper and then one Kafka broker:

```bash
./start-zookeeper.sh
...
[2017-09-27 22:09:26,634] INFO binding to port 0.0.0.0/0.0.0.0:2181 (org.apache.zookeeper.server.NIOServerCnxnFactory)
...

```

```bash
./start-broker.sh
...
[2017-09-27 22:09:33,814] INFO Registered broker 0 at path /brokers/ids/0 with addresses: EndPoint(jeqo-Oryx-Pro,9092,ListenerName(PLAINTEXT),PLAINTEXT) (kafka.utils.ZkUtils)
...
[2017-09-27 22:09:33,817] INFO [Kafka Server 0], started (kafka.server.KafkaServer)
...
```

### Run Applications

To get started, let's start the producer side and test it:

