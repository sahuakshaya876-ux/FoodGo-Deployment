
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

        stage('4. Trivy Filesystem Scan') {
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

        stage('5. Docker Build') {
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

        stage('6. Trivy Docker Image Scan') {
            steps {
                sh '''
                    TMPDIR=/var/lib/jenkins/trivy-tmp trivy image \
                      --exit-code 1 \
                      --severity HIGH,CRITICAL \
                      --no-progress \
                      ${BACKEND_IMAGE}:${IMAGE_TAG}

                    TMPDIR=/var/lib/jenkins/trivy-tmp trivy image \
                      --exit-code 1 \
                      --severity HIGH,CRITICAL \
                      --no-progress \
                      ${FRONTEND_IMAGE}:${IMAGE_TAG}
                '''
            }
        }

        stage('7. Docker Hub Login') {
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

        stage('8. Push Images to Docker Hub') {
            steps {
                sh '''
                    docker push ${BACKEND_IMAGE}:${IMAGE_TAG}
                    docker push ${FRONTEND_IMAGE}:${IMAGE_TAG}
                '''
            }
        }

        stage('9. Ansible Deployment to Minikube') {
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

        stage('10. Kubernetes Rollout Status') {
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

    stage('11. Health Check') {
    steps {
        sh '''
            set -e

            echo "Waiting for a ready backend pod..."

            for i in $(seq 1 30); do
                BACKEND_POD=$(kubectl get pods \
                  -n foodgo \
                  -l app=foodgo-backend \
                  -o jsonpath='{range .items[?(@.status.phase=="Running")]}{.metadata.name}{"\\n"}{end}' \
                  | while read pod; do
                      [ -z "$pod" ] && continue

                      READY=$(kubectl get pod "$pod" -n foodgo \
                        -o jsonpath='{.status.conditions[?(@.type=="Ready")].status}')

                      DELETING=$(kubectl get pod "$pod" -n foodgo \
                        -o jsonpath='{.metadata.deletionTimestamp}')

                      if [ "$READY" = "True" ] && [ -z "$DELETING" ]; then
                          echo "$pod"
                          break
                      fi
                    done)

                if [ -n "$BACKEND_POD" ]; then
                    echo "Checking ready backend pod: $BACKEND_POD"
                    break
                fi

                echo "No ready backend pod yet. Waiting..."
                sleep 5
            done

            if [ -z "$BACKEND_POD" ]; then
                echo "ERROR: No ready backend pod found."
                kubectl get pods -n foodgo -l app=foodgo-backend -o wide
                exit 1
            fi

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
