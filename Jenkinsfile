pipeline {
    agent any

    tools {
        maven 'Maven-3'
        jdk 'JDK-21'
    }

    environment {
        DOCKERHUB_USER = 'akshayasahh'
        BACKEND_IMAGE  = 'akshayasahh/foodgo-backend'
        FRONTEND_IMAGE = 'akshayasahh/foodgo-frontend'
        IMAGE_TAG      = "${env.BUILD_NUMBER}"
        SONAR_HOST_URL = 'http://localhost:9000'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '15'))
        disableConcurrentBuilds()
    }

    stages {

        stage('1. Checkout') {
            steps {
                checkout scm
            }
        }

        stage('2. Maven Clean Package') {
            steps {
                dir('backend') {
                    sh 'mvn -B clean package -DskipTests'
                }
            }
        }

        stage('3. Unit Tests') {
            steps {
                dir('backend') {
                    sh 'mvn -B test'
                }
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('4. SonarQube Analysis') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQubeServer') {
                        withCredentials([
                            string(
                                credentialsId: 'sonarqube-token',
                                variable: 'SONAR_TOKEN'
                            )
                        ]) {
                            sh '''
                                mvn -B sonar:sonar \
                                  -Dsonar.host.url=$SONAR_HOST_URL \
                                  -Dsonar.token=$SONAR_TOKEN
                            '''
                        }
                    }
                }
            }
        }

        stage('5. SonarQube Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('6. Trivy Filesystem Scan') {
            steps {
                sh '''
                    trivy fs \
                      --exit-code 1 \
                      --severity HIGH,CRITICAL \
                      --no-progress \
                      backend/ || {
                        echo "Trivy found HIGH/CRITICAL vulnerabilities in the backend filesystem"
                        exit 1
                    }
                '''
            }
        }

        stage('7. Docker Build') {
            steps {
                sh '''
                    docker build \
                      -t ${BACKEND_IMAGE}:${IMAGE_TAG} \
                      backend/

                    docker build \
                      -t ${FRONTEND_IMAGE}:${IMAGE_TAG} \
                      frontend/
                '''
            }
        }

        stage('8. Trivy Docker Image Scan') {
            steps {
                sh '''
                    trivy image \
                      --exit-code 1 \
                      --severity HIGH,CRITICAL \
                      --no-progress \
                      ${BACKEND_IMAGE}:${IMAGE_TAG}

                    trivy image \
                      --exit-code 1 \
                      --severity HIGH,CRITICAL \
                      --no-progress \
                      ${FRONTEND_IMAGE}:${IMAGE_TAG}
                '''
            }
        }

        stage('9. Docker Hub Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | \
                        docker login \
                          -u "$DOCKER_USERNAME" \
                          --password-stdin
                    '''
                }
            }
        }

        stage('10. Push Images to Docker Hub') {
            steps {
                sh '''
                    docker push ${BACKEND_IMAGE}:${IMAGE_TAG}
                    docker push ${FRONTEND_IMAGE}:${IMAGE_TAG}
                '''
            }
        }

        stage('11. Ansible Deployment to Minikube') {
            steps {
                dir('ansible') {
                    sh '''
                        ansible-playbook \
                          -i inventory \
                          deploy.yml \
                          -e image_tag=${IMAGE_TAG}
                    '''
                }
            }
        }

        stage('12. Kubernetes Rollout Status') {
            steps {
                sh '''
                    kubectl rollout status \
                      deployment/foodgo-backend \
                      -n foodgo \
                      --timeout=180s

                    kubectl rollout status \
                      deployment/foodgo-frontend \
                      -n foodgo \
                      --timeout=180s
                '''
            }
        }

        stage('13. Health Check') {
            steps {
                sh '''
                    set -e

                    BACKEND_POD=$(kubectl get pods \
                      -n foodgo \
                      -l app=foodgo-backend \
                      -o jsonpath="{.items[0].metadata.name}")

                    kubectl exec \
                      -n foodgo \
                      "$BACKEND_POD" \
                      -- wget -qO- \
                      http://localhost:8080/actuator/health

                    echo ""
                    echo "Backend health check passed."
                '''
            }
        }
    }

    post {
        success {
            echo "FoodGo build #${env.BUILD_NUMBER} deployed successfully to Minikube."
        }

        failure {
            echo "FoodGo build #${env.BUILD_NUMBER} failed. Check the stage logs above."
        }

        always {
            sh 'docker image prune -f || true'
        }
    }
}