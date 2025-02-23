build:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

build-api: install
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

build-keycloak:
	docker pull quay.io/keycloak/keycloak:26.0.0 --platform linux/arm64
	docker tag quay.io/keycloak/keycloak:26.0.0 quay.io/keycloak/keycloak:26.0.0-arm64

build-redisinsight:
	docker pull redis/redisinsight:2.66 --platform linux/arm64
	docker tag redis/redisinsight:2.66 redis/redisinsight:2.66-arm64
	minikube image load redis/redisinsight:2.66-arm64

build-api-dev: install
	#COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml build
	docker build -t lsadf/lsadf-api-dev-arm64:latest -f docker/Dockerfile-dev . --build-arg module_folder=lsadf_api --platform=linux/arm64/v8
	minikube image load lsadf/lsadf-api-dev-arm64:latest

build-admin: install-docker admin-install
	COMPOSE_PROFILES=backend_admin docker-compose --env-file env/env.properties -f dc-local.yml build --build-arg BRANCH_NAME=$BRANCH_NAME

build-admin-dev: install-docker admin-install
	COMPOSE_PROFILES=backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml build

build-api-dev-ghcr: install-no-env
	docker build -t ghcr.io/lsdaf/lsdaf_api-dev:latest -f docker/Dockerfile-dev . --build-arg module_folder=lsadf_api
