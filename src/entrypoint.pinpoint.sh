#!/bin/sh

echo "[INFO] Starting RESOURCE App with Pinpoint Agent..."
echo "PROFILE: $PINPOINT_PROFILE"
echo "AGENT PATH: $PINPOINT_AGENT_PATH"

java \
  -Dkotlinx.coroutines.debug=on \
  -jar \
  -javaagent:${PINPOINT_AGENT_PATH}/pinpoint-bootstrap.jar \
  -Dpinpoint.profiler.profiles.active=release \
  -Dpinpoint.applicationName=crm-${PINPOINT_PROFILE} \
  app.jar
