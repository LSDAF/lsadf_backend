.PHONY: install-pre-commit

install-pre-commit:
	pre-commit clean
	pre-commit install