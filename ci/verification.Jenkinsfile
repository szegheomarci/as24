pipeline {
    agent any

    tools {
        maven 'Maven - Jenkins internal'
    }

    environment {
        ROOTDIR = "/var/jenkins_home/workspace/car-ads_verification"
    }

    stages {
        stage('Init') {
            steps {
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    env.projectVersion = pom.version
                    echo "Build version: ${env.projectVersion}-verif${env.BUILD_NUMBER}"
                    env.dockerId = "szegheomarci/carads:" + env.projectVersion + "-" + env.BUILD_NUMBER
                }
            }
        }
        stage('Build jar') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Build docker image') {
            steps {
                sh "docker build -t ${env.dockerId} ."
            }
        }
        stage('Test docker image') {
            steps {
                sh "mkdir -p ci/test_data/out"
                sh """docker run \
                    -v ${env.ROOTDIR}/ci/test_data/config:/config ${env.dockerId} \
                    -v ${env.ROOTDIR}/ci/test_data/out:/out"""
                sh """diff \$(ls ci/test_data/out/*.txt) \
                            ci/test_data/config/result.txt"""
            }
        }
    }/*
    post {
        always {
            script {
                cleanWs()
                // Check if the Docker container is running
                def isContainerRunning = sh(script: "docker inspect -f {{.State.Running}} ${env.dockerId}", returnStatus: true) == 0

                // Stop the Docker container if it's running
                if (isContainerRunning) {
                    echo "Stopping ${env.dockerId} container"
                    sh "docker ps -q --filter ancestor=${env.dockerId} | xargs docker stop"
                }   */
                /*// Remove the Docker container
                echo "Deleting ${env.dockerId} container"
                sh "docker ps -a | grep '${env.dockerId}' | awk '{print \$1}' | xargs docker rm"*/
                // Remove the Docker image
         /*       echo "Deleting ${env.dockerId} image"
                sh "docker images | grep \$(echo '${env.dockerId}' | sed 's|:|\\\\\\s*|') | awk '{print \$3}' | xargs docker rmi -f"
            }
        }
    }*/
}
