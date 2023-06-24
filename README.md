# Microservices

## build packages
mvn clean package -DskipTests

## update packages version
mvn versions:set -DnewVersion=0.0.2-SNAPSHOT

# Docker
## register the image
docker tag monitor:0.0.1-SNAPSHOT daasalbion/monitor:0.0.1-SNAPSHOT
docker push daasalbion/monitor:0.0.1-SNAPSHOT

## remove dangling images
docker rmi $(docker images -f dangling=true -q)

## create de kubernetes files from docker-compose.yml
kompose convert

# Kubernetes

## apply deployment
kubectl apply -f deployment.yaml

## delete deployment
kubectl delete -f deployment.yaml

## delete service
kubectl delete svc gateway-service

## list all services
kubectl get service -o wide

## expose service
kubectl expose deployment monitor-service --type=LoadBalancer --name=monitor-service-exposed

## connect to a pod
kubectl exec --stdin --tty registry-service-7d9d7ccf6d-z9m5q -- /bin/bash

# MiniKube

## start
minikube start

## dashboard
minikube dashboard

## tunnel
minikube tunnel