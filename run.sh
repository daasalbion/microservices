#!/bin/sh

# registry
cd ./registry/
docker-compose stop
docker-compose rm -f
docker-compose up --build -d
cd ../

# gateway
cd ./gateway/
docker-compose stop
docker-compose rm -f
docker-compose up --build -d
cd ../

# monitor
cd ./monitor/
docker-compose stop
docker-compose rm -f
docker-compose up --build -d
cd ../

# kafka
cd ./kafka/
docker-compose stop
docker-compose rm -f
docker-compose up --build -d
cd ../

# user-service
cd ./user-service/
docker-compose stop
docker-compose rm -f
docker-compose up --build -d
cd ../

# identity-service
cd ./identity-service/
docker-compose stop
docker-compose rm -f
docker-compose up --build -d
cd ../

# config-server
cd ./config-server/
docker-compose stop
docker-compose rm -f
docker-compose up --build -d
cd ../

# prometheus
cd ./prometheus/
docker-compose stop
docker-compose rm -f
docker-compose up --build -d

