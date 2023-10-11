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
                sh "docker build -t carads:${projectVersion}-${env.BUILD_NUMBER} ."
            }
        }
    }
}
