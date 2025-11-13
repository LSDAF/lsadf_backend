.PHONY: install install-docker install-no-env admin-install javadoc

install: clean
	@mvn install -DskipTests -U -fae -Dcopy-env -DskipSurefireReport -T 1C

install-no-env: clean
	@mvn install -DskipTests -U -fae -DskipSurefireReport

install-docker: clean
	@mvn install -DskipTests -U -fae -Dcopy-env -DskipSurefireReport -Pproduction

admin-install:
	@npm --prefix ./lsadf_admin install

javadoc:
	@mvn javadoc:aggregate