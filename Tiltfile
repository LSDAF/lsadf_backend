# Ensure version is valid
version_settings(constraint=">=0.33.22")

# Enable Kubernetes
allow_k8s_contexts('minikube')

# Build the Docker image
docker_build(
    'lsdaf-backend',
    '.',
    dockerfile='docker/Dockerfile',
    live_update=[
        sync('.', '/app'),
        run('mvn package -DskipTests', trigger=['pom.xml', 'src/']),
    ]
)

# Deploy to Kubernetes
k8s_yaml(['k8s/deployment.yaml', 'k8s/service.yaml'])

# Port forward the service
k8s_resource(
    'lsdaf-backend',
    port_forwards='8080:8080',
    labels=['backend']
)