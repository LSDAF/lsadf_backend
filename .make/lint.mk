.PHONY: lint-check lint license-check license

lint-check:
	@mvn spotless:check

lint:
	@mvn spotless:apply

license-check:
	@mvn license:check

license:
	@mvn license:format