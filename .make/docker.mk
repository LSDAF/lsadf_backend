.PHONY: up updev down logs dbup dbdown moitorup monitordown upadmin upadmindev

GIT_BRANCH := $(shell git rev-parse --abbrev-ref HEAD | tr '/' '-')

up:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db,backend,monitoring docker-compose --env-file env/env.properties -f dc-local.yml up -d

updev:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db,backend_dev,monitoring docker-compose --env-file env/env.properties -f dc-local.yml up -d

down:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db,backend,monitoring,backend_dev,backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml down

logs:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db,backend,backend_dev,backend_admin_dev,monitoring docker-compose --env-file env/env.properties -f dc-local.yml logs -f

dbup:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db docker-compose --env-file env/env.properties -f dc-local.yml up -d

dbdown:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db docker-compose --env-file env/env.properties -f dc-local.yml down

monitorup:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db,monitoring docker-compose --env-file env/env.properties -f dc-local.yml up -d

monitordown:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db,monitoring docker-compose --env-file env/env.properties -f dc-local.yml down

upadmin:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend_admin docker-compose --env-file env/env.properties -f dc-local.yml up -d

upadmindev:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml up -d
