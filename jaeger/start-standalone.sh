#!/usr/bin/env bash
./jaeger/standalone --span-storage.type=memory --query.static-files=jaeger-ui-build/build/
