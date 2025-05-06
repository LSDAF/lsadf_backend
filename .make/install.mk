install: clean
	@mvn install -DskipTests -U -fae -Dcopy-env -DskipSurefireReport

install-no-env: clean
	@mvn install -DskipTests -U -fae -DskipSurefireReport

install-docker: clean
	@mvn install -DskipTests -U -fae -Dcopy-env -DskipSurefireReport -Pproduction

admin-install:
	@npm --prefix ./lsadf_admin install

javadoc:
	@mvn javadoc:aggregate