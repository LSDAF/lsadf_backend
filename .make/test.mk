.PHONY: test test-unit test-bdd report

test:
	@mvn verify -DskipSurefireReport

test-unit:
	@mvn test -fae

test-bdd:
	@mvn package failsafe:verify -DskipSurefireReport -fae

report:
	@mvn surefire-report:report-only -DcucumberReport