# Microservices

## build packages
mvn package -DskipTests

# Docker
## register the image
docker tag monitor:0.0.1-SNAPSHOT daasalbion/monitor:0.0.1-SNAPSHOT
docker push daasalbion/monitor:0.0.1-SNAPSHOT

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

# MiniKube
## dashboard
minikube dashboard

## tunnel
minikube tunnel