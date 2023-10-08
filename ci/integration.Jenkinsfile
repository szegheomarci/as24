def version = "1.0.${env.BUILD_NUMBER}"

pipeline {
    agent any

    tools {
        maven 'Maven - Jenkins internal'
    }

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                echo "build number: ${env.BUILD_NUMBER}"
            }
        }
        stage('fail test') {
            steps {
                sh 'exit -1'
            }
        }
    }
}
