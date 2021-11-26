JENKINS_KEY = '/deploy/jenkins'
DIR = '/home/jenkins/deploy/secretsanta/'

ENVIRONMENT = 'undefined'
PROFILE = 'undefined'
HOST = 'airsoftware-api'
USER = 'jenkins'

DOCKER_REPO = 'airsoftware/'
DOCKER_IMG = 'secretsanta'
DOCKER_TAG = 'latest'
HOST_PORT = '8089'
DOCKER_PORT = '8080'
DOCKER_USR = 'airsoftware'
DOCKER_PASS_PATH_SRC = '/deploy/docker_password'
DOCKER_PASS_PATH_DST = '/home/jenkins/deploy/password'
DOCKER_TZ = 'America/Mexico_City'
DOCKER_ARGS = '-v /tmp/storage:/tmp/storage -v /storage:/storage --log-opt max-size=10m --log-opt max-file=5'

VALIDATE_URL = 'http://localhost:8089/health'

pipeline {

    agent any

    stages {

        stage('Configure') {
            steps {
                script {
                    echo "Executing pipeline in branch: ${env.BRANCH_NAME}"
                    if (env.BRANCH_NAME == 'master') {
                        ENVIRONMENT = 'PROD'
                        PROFILE = 'prod'
                    }
                    echo "Environment: ${ENVIRONMENT}"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    if (ENVIRONMENT != 'undefined') {
                        sh "./mvnw clean install -DskipTests"
                        sh "docker image prune --force"
                        sh "cat ${DOCKER_PASS_PATH_SRC} | docker login --username ${DOCKER_USR} --password-stdin"
                        sh "docker build -t ${DOCKER_IMG} --label ${DOCKER_IMG} ."
                        sh "docker tag ${DOCKER_IMG}:${DOCKER_TAG} ${DOCKER_REPO}${DOCKER_IMG}:${DOCKER_TAG}"
                        sh "docker push ${DOCKER_REPO}${DOCKER_IMG}:${DOCKER_TAG}"
                    }
                }
            }
        }

        stage('Update scripts') {
            steps {
                script {
                    if (ENVIRONMENT != 'undefined') {
                        sh "scp -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ${DOCKER_PASS_PATH_SRC} ${USER}@${HOST}:${DOCKER_PASS_PATH_DST}"
                        sh "scp -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ci/*.sh ${USER}@${HOST}:${DIR}"
                    }
                }
            }
        }

        stage('Refresh Service') {
            steps {
                script {
                    if (ENVIRONMENT != 'undefined') {
                        sh "ssh -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ${USER}@${HOST} 'cat ${DOCKER_PASS_PATH_DST} | docker login --username ${DOCKER_USR} --password-stdin'"
                        sh "ssh -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ${USER}@${HOST} 'docker pull ${DOCKER_REPO}${DOCKER_IMG}:${DOCKER_TAG}'"
                    }
                }
            }
        }

        stage('Stop Service') {
            steps {
                script {
                    if (ENVIRONMENT != 'undefined') {
                        sh "ssh -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ${USER}@${HOST} 'cd ${DIR} && ./stop.sh \"${DOCKER_IMG}\"'"
                    }
                }
            }
        }

        stage('Start Service') {
            steps {
                script {
                    if (ENVIRONMENT != 'undefined') {
                        sh "ssh -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ${USER}@${HOST} 'cd ${DIR} && ./run.sh \"${DOCKER_IMG}\" \"${HOST_PORT}\" \"${DOCKER_PORT}\" \"${PROFILE}\" \"${DOCKER_TZ}\" \"${DOCKER_REPO}\" \"${DOCKER_TAG}\" \"${DOCKER_ARGS}\"'"
                    }
                }
            }
        }

        stage('Validate Service') {
            steps {
                script {
                    if (ENVIRONMENT != 'undefined') {
                        sh "ssh -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ${USER}@${HOST} 'cd ${DIR} && ./validate.sh \"${DOCKER_IMG}\" \"${VALIDATE_URL}\"'"
                    }
                }
            }
        }

        stage('Clean Service') {
            steps {
                script {
                    if (ENVIRONMENT != 'undefined') {
                        sh "ssh -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ${USER}@${HOST} 'cd ${DIR} && ./clean.sh \"${DOCKER_IMG}\"'"
                    }
                }
            }
        }

        stage('Post Steps') {
            steps {
                script {
                    if (ENVIRONMENT != 'undefined') {
                        sh "ssh -i ${JENKINS_KEY} -o StrictHostKeyChecking=no ${USER}@${HOST} 'cd ${DIR} && ./post_steps.sh'"
                    }
                }
            }
        }

    }

}
