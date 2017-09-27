#!/usr/bin/env bash
wget https://github.com/sysco-middleware/jaeger/releases/download/v0.7.0-sysco-2/jaeger_0.7.0-sysco-2_linux_amd64.tar.gz
mkdir -p jaeger_0.7.0
tar -xzf jaeger_0.7.0-sysco-2_linux_amd64.tar.gz -C jaeger_0.7.0
rm jaeger_0.7.0-sysco-2_linux_amd64.tar.gz*

wget https://github.com/sysco-middleware/jaeger/releases/download/v0.7.0-sysco-2/jaeger-ui.tar.gz
tar -xzf jaeger-ui.tar.gz
rm jaeger-ui.tar.gz*