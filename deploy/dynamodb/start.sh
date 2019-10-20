#!/usr/bin/env sh
docker run \
  -d \
  -p 8000:8000 \
  -v /tmp/dynamodblocal:/var/db \
  --name dynamodb \
  amazon/dynamodb-local \
  -jar /home/dynamodblocal/DynamoDBLocal.jar -dbPath /var/db/