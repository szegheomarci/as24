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
                mvn compile assembly:single
            }
        }
    }
}
