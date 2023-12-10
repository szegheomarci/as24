pipeline {
    agent any

    parameters {
        string(name: "DROP_VERSION", defaultValue: "", trim: true, description: "Dropped version to release")
    }

    environment {
        IMAGE_NAME = "szegheomarci/carads"
    }

    stages {
        stage('Pull image') {
            steps {
                echo "Pulling ${IMAGE_NAME}:${parameters.DROP_VERSION} from ghcr."
                script {
                    docker.withRegistry('https://ghcr.io/', 'szegheomarci-github') {
                        docker.image("${IMAGE_NAME}:${parameters.DROP_VERSION}").pull()
                    }
                }
            }
        }
    }
}
