pipeline {
    agent any

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
                sh "docker build -t szegheomarci/carads:0.4-${env.BUILD_NUMBER} ."
            }
        }
        stage('Push Docker image to repository') {
            steps {
                script {
                    docker.withRegistry('https://ghcr.io/', 'szegheomarci-github') {
                        docker.image("szegheomarci/carads:0.4-${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }
    }
}
