lint-check:
	@mvn spotless:check

lint:
	@mvn spotless:apply

license-check:
	@mvn license:check

license:
	@mvn license:format