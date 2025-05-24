.PHONY: clean prune purge purgedb purgelogs

clean:
	@mvn clean

prune:
	@docker system prune -a -f
	@docker image prune -f

purge: purgedb purgelogs

purgedb:
	@rm -rf docker/data/postgres/*
	@echo "Postgres data purged"

purgelogs:
	@rm -rf docker/data/lsadf_backend_data/*
	@rm -rf docker/data/lsadf_backend_dev_data/*
	@echo "Backend logs purged"