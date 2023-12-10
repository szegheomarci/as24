pipeline {
    agent any

    parameters {
        string(name: "DROP_VERSION", defaultValue: "", trim: true, description: "Dropped version to release")
    }

    environment {
        IMAGE_NAME = "szegheomarci/carads"
        RELEASE_VERSION = params.DROP_VERSION.substring(0, params.DROP_VERSION.indexOf('-'))
    }

    stages {
        stage('Pull image') {
            steps {
                echo "Pulling ${IMAGE_NAME}:${params.DROP_VERSION} from ghcr."
                script {
                    docker.withRegistry('https://ghcr.io/', 'szegheomarci-github') {
                        docker.image("${IMAGE_NAME}:${params.DROP_VERSION}").pull()
                    }
                    sh("docker tag ghcr.io/${IMAGE_NAME}:${params.DROP_VERSION} ${IMAGE_NAME}:${RELEASE_VERSION}")
                }
            }
        }
        stage('Push image') {
            steps {
                script {
                    echo "Pushing ${IMAGE_NAME}:${RELEASE_VERSION} to dockerhub"
                    docker.withRegistry('https://registry-1.docker.io/v2/', 'szegheomarci-dockerhub') {
                        docker.image("${IMAGE_NAME}:${RELEASE_VERSION}").push()
                    }
                }
            }
        }
    }
}