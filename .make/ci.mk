GIT_BRANCH := $(shell git rev-parse --abbrev-ref HEAD | tr '/' '-')

clean-ci:
	@mvn clean -q

install-ci: clean-ci
	@mvn install -DskipTests -U -fae -DskipSurefireReport -q --batch-mode --no-transfer-progress

test-ci:
	@mvn verify --batch-mode --no-transfer-progress -Dci -fae -DskipSurefireReport

build-ci:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml --progress quiet build

build-ci-ghcr: build-api-ghcr
	

build-dev-ci: install-ci
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml --progress quiet build

build-admin-dev-ci: install-ci
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml --progress quiet build

build-api-ghcr: install-no-env
	docker buildx build --platform linux/amd64,linux/arm64 -t ghcr.io/lsdaf/lsadf_api:latest -f docker/Dockerfile .
