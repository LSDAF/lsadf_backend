
helmu:
	helm uninstall lsadf


helmup:
	helm upgrade --install lsadf lsadf-k8s/ -f lsadf-k8s/values.yml -f lsadf-k8s/values-local.yml -f lsadf-k8s/values-secret.yml