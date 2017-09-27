#!/usr/bin/env bash

KAFKA_VERSION=0.11.0.0

wget http://www-eu.apache.org/dist/kafka/0.11.0.0/kafka_2.11-${KAFKA_VERSION}.tgz

tar -xzf kafka*.tgz

rm kafka*.tgz*