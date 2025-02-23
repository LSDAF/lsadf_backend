

miniup: secret ministart helmup

minidown:
	helm uninstall lsadf


miniload:
		minikube image load quay.io/keycloak/keycloak:26.0.0-arm64
        minikube image load redis/redisinsight:2.66-arm64
        minikube image load lsadf/lsadf-api-dev-arm64:latest

ministart:
	minikube start --driver=docker --container-runtime=containerd --cpus=2 --memory=2048 --disk-size=5g --addons ingress
	minikube addons enable metrics-server
	make build-api-dev build-keycloak build-redisinsight
	make miniload


ministop:
	minikube stop

minidelete: ministop
	minikube delete --all --purge


monitorup: ministart helmup