def version = "1.0.${env.BUILD_NUMBER}"

pipeline {
    agent any

    tools {
        maven 'maven internal 3.8'
    }

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                echo "${env.BUILD_NUMBER}"
            }
        }
        stage('Build jar') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Build docker image') {
            steps {
                sh 'docker build -t carads:${version} .'
            }
        }
    }
}
