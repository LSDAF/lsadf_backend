# Ensure version is valid
version_settings(constraint=">=0.33.22")

profiles = os.getenv("TILT_PROFILES", "").split()


# Read service selection from env or CLI args
allowed_services = str(os.getenv("SERVICES", "all")).split(",")

def is_enabled(name):
    return "all" in allowed_services or name in allowed_services

# Postgres
if is_enabled("postgres"):
    #local_resource(
    #    name="postgres",
    #    cmd="helm upgrade --install postgres /Users/louissantucci/Documents/projects/lsadf_infra/apps/postgres",

    #    deps=["/Users/louissantucci/Documents/projects/lsadf_infra/apps/postgres"]
    #)
    postgres_helm = helm(
    "/Users/louissantucci/Documents/projects/lsadf_infra/apps/postgres",
    name="postgres",
    namespace="postgres",
    values=["/Users/louissantucci/Documents/projects/lsadf_infra/apps/postgres/values.yml"])
    k8s_yaml(postgres_helm)

# API1
if is_enabled("api1"):
    k8s_yaml("services/api1/k8s.yaml")
    k8s_resource("api1", port_forwards=8000)

# API2
if is_enabled("api2"):
    k8s_yaml("services/api2/k8s.yaml")
    k8s_resource("api2", port_forwards=8001)