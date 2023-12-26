pipeline {
    agent any

    tools {
        maven 'Maven - Jenkins internal'
    }
    parameters {
        string(name: "DROP_VERSION", defaultValue: "", trim: true, description: "Dropped version to release")
    }

    environment {
        IMAGE_NAME = "szegheomarci/carads"
        RELEASE_VERSION = params.DROP_VERSION.substring(0, params.DROP_VERSION.indexOf('-'))
    }

    stages {
        stage('Pull image') {
            steps {
                echo "Pulling ${IMAGE_NAME}:${params.DROP_VERSION} from ghcr."
                script {
                    docker.withRegistry('https://ghcr.io/', 'szegheomarci-github') {
                        docker.image("${IMAGE_NAME}:${params.DROP_VERSION}").pull()
                    }
                    sh("docker tag ghcr.io/${IMAGE_NAME}:${params.DROP_VERSION} ${IMAGE_NAME}:${RELEASE_VERSION}")
                }
            }
        }
        stage('Push image') {
            steps {
                script {
                    echo "Pushing ${IMAGE_NAME}:${RELEASE_VERSION} to dockerhub"
                    docker.withRegistry('https://registry-1.docker.io/v2/', 'szegheomarci-dockerhub') {
                        docker.image("${IMAGE_NAME}:${RELEASE_VERSION}").push()
                    }
                }
            }
        }
        stage('Clone repository') {
            steps {
                script {
                    git branch: 'main',
                        credentialsId: 'gerrit_user',
                        url: 'ssh://szm-Jenkins@review.gerrithub.io:29418/szegheomarci/car-ads'
                    sh "git config user.name 'Jenkins'"
                    sh "git config user.email 'jenkins@szegheomarci.com'"
                }
            }
        }
        stage('Tag released commit') {
            steps {
                script {
                    sh "git reset --hard tags/carAds-v${params.DROP_VERSION}"
                    // Tag the commit
                    sh "git tag -a ${RELEASE_VERSION} -m 'Released version ${RELEASE_VERSION}'"

                    // Push the tag to the remote repository
                    sshagent(['gerrit_user']) {
                        sh("git push origin ${RELEASE_VERSION}")
                    }
                }
            }
        }
        stage('Increment version') {
            steps {
                script {
                    sh "git reset --hard origin/main"
                    sh "mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit"
                    sh "git add . && git commit -m 'Automatic version increase.'"
                    sshagent(['gerrit_user']) {
                        sh("git push origin HEAD:refs/heads/main")
                    }
                }
            }
        }
        stage('Tag incremented version') {
            steps {
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    def artifactId = pom.artifactId
                    def projectVersion = pom.version
                    def tagVersion = artifactId + "-v" + projectVersion + "-0"

                    // Tag the commit
                    sh "git tag -a ${tagVersion} -m 'Version ${tagVersion}'"

                    // Push the tag to the remote repository
                    sshagent(['gerrit_user']) {
                        sh("git push origin ${tagVersion}")
                    }
                }
            }
        }
    }
}