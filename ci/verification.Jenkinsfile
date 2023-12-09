pipeline {
    agent any

    tools {
        maven 'Maven - Jenkins internal'
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
                sh 'mvn package'
            }
        }
        stage('Build docker image') {
            steps {
                sh "docker build -t ${env.dockerId} ."
            }
        }
        stage('Tag on Success') {
            when {
                expression { currentBuild.resultIsBetterOrEqualTo('SUCCESS') }
            }
            steps {
                script {
                    sh 'cat .git/config'

                    sh 'git reset --hard HEAD^'
                    
                    sh("git config user.name 'Jenkins'")
                    sh("git config user.email 'jenkins@szegheomarci.com'")

                    // Tag the commit
                    sh "git tag -a ${env.projectVersion} -m 'Version ${env.projectVersion}'"

                    // Push the tag to the remote repository
                    //sh "git push origin ${env.buildVersion}"

                /*
                    // Create a directory for tagging
                    dir('tagging_workspace') {
                        // Initialize Git in the new directory
                        sh 'git init'

                        // Tag the commit
                        sh "git tag -a ${versionNumber} -m 'Version ${versionNumber}'"

                        // Push the tag to the remote repository
                        sh "git push origin ${versionNumber}"
                    }*/
                }
            }
        }
    }
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
                }
                /*// Remove the Docker container
                echo "Deleting ${env.dockerId} container"
                sh "docker ps -a | grep '${env.dockerId}' | awk '{print \$1}' | xargs docker rm"*/
                // Remove the Docker image
                echo "Deleting ${env.dockerId} image"
                sh "docker images | grep \$(echo '${env.dockerId}' | sed 's|:|\\\\\\s*|') | awk '{print \$3}' | xargs docker rmi -f"
            }
        }
    }
}
