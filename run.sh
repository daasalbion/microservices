#!/bin/sh

# registry
cd ./registry/
docker-compose up -d
cd ../

# gateway
cd ./gateway/
docker-compose up -d
cd ../

# monitor
cd ./monitor/
docker-compose up -d
cd ../

# kafka
cd ./kafka/
docker-compose up -d
cd ../

# user-service
cd ./user-service/
docker-compose up -d
cd ../

# identity-service
cd ./identity-service/
docker-compose up -d
cd ../

# config-server
cd ./config-server/
docker-compose up -d

