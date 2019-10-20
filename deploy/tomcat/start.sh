#!/usr/bin/env sh
docker run \
  -d \
  -v "$(pwd)":/usr/local/tomcat/webapps/hockey-smash \
  -v /Users/imeikle/.aws:/root/.aws \
  -p 8080:8080 \
  -p 5000:5000 \
  --link dynamodb \
  --env AWS_PROFILE="jutul" \
  --env HOCKEY_SMASH_LOCAL_DB=true \
  --env JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,address=5000,server=y,suspend=n" \
  --name tomcat \
  tomcat:8.5