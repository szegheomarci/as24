pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
        stage('Build') {
            steps {
                mvn --version
                mvn compile assembly:single
            }
        }
    }
}
