pipeline {
    agent any

    environment {
        // Define your Docker registry credentials ID from Jenkins credentials
        DOCKER_CREDENTIALS = credentials('nexus')
        // Define your Nexus Docker repository URL
        NEXUS_DOCKER_REPO = 'http://192.168.0.160:9081/repository/docker-hosted'
    }

    tools {
        maven 'Maven - Jenkins internal'
    }

    stages {
        stage('Read project version') {
            steps {
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    def projectVersion = pom.version
                    echo "Project version: ${projectVersion}"
                    env.projectVersion = projectVersion
                }
            }
        }
        stage('Build jar') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Build docker image') {
            steps {
                echo "docker version: ${projectVersion}-${env.BUILD_NUMBER}"
                sh "docker build -t carads:0.3-${env.BUILD_NUMBER} ."
            }
        }
        stage('Push Docker image to Nexus') {
            steps {
                script {
                    docker.withRegistry(NEXUS_DOCKER_REPO, 'nexus', insecureRegistry: true) {
                        docker.image("carads:0.3-${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }
    }
}
