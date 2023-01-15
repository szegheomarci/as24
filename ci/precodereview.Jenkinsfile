pipeline {
    agent any

    tools {
        maven 'maven internal 3.8'
    }

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
        stage('Build') {
            steps {
                mvn package
            }
        }
    }
}
