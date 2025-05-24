.PHONY: build build-admin build-admin-dev build-api build-api-dev

build:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

build-api: install
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

build-api-dev: install
	COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml build

build-admin: install-docker admin-install
	COMPOSE_PROFILES=backend_admin docker-compose --env-file env/env.properties -f dc-local.yml build --build-arg BRANCH_NAME=$BRANCH_NAME

build-admin-dev: install-docker admin-install
	COMPOSE_PROFILES=backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml build
