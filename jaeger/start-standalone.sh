#!/usr/bin/env bash
./jaeger_0.7.0/standalone --span-storage.type=memory --query.static-files=jaeger-ui-build/build/
