// FoodGo CI/CD Pipeline
//
// Expected Jenkins credentials (configured in Jenkins, never in this file):
//   - 'aws-credentials'      : AWS access key/secret (or use an instance profile)
//   - 'sonarqube-token'      : SonarQube authentication token
//   Jenkins tools configured : 'Maven-3', 'JDK-21' (names must match below,
//                              or update the `tools` block to match your setup)
//
// Expected Jenkins plugins: Pipeline, Git, SonarQube Scanner, Docker Pipeline,
//                           AWS Steps / Credentials Binding, Ansible.

pipeline {
    agent any

    tools {
        maven 'Maven-3'
        jdk 'JDK-21'
    }

    environment {
        AWS_REGION        = 'ap-south-1'
        ECR_REPO_BACKEND  = 'foodgo-backend'
        ECR_REPO_FRONTEND = 'foodgo-frontend'
        ECR_REGISTRY      = credentials('ecr-registry-url') // e.g. 123456789012.dkr.ecr.ap-south-1.amazonaws.com
        EKS_CLUSTER_NAME  = 'foodgo-cluster'
        IMAGE_TAG         = "${env.BUILD_NUMBER}"
        SONAR_HOST_URL    = 'http://localhost:9000'
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
                        withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                            sh """
                                mvn -B sonar:sonar \
                                  -Dsonar.host.url=${SONAR_HOST_URL} \
                                  -Dsonar.login=${SONAR_TOKEN}
                            """
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
                    trivy fs --exit-code 1 --severity HIGH,CRITICAL --no-progress backend/ || \
                    (echo "Trivy found HIGH/CRITICAL vulnerabilities in the filesystem scan" && exit 1)
                '''
            }
        }

        stage('7. Docker Build') {
            steps {
                dir('backend') {
                    sh "docker build -t ${ECR_REPO_BACKEND}:${IMAGE_TAG} ."
                }
                dir('frontend') {
                    sh "docker build -t ${ECR_REPO_FRONTEND}:${IMAGE_TAG} ."
                }
            }
        }

        stage('8. Trivy Docker Image Scan') {
            steps {
                sh """
                    trivy image --exit-code 1 --severity HIGH,CRITICAL --no-progress ${ECR_REPO_BACKEND}:${IMAGE_TAG} || \
                    (echo "Trivy found HIGH/CRITICAL vulnerabilities in the backend image" && exit 1)
                """
                sh """
                    trivy image --exit-code 1 --severity HIGH,CRITICAL --no-progress ${ECR_REPO_FRONTEND}:${IMAGE_TAG} || \
                    (echo "Trivy found HIGH/CRITICAL vulnerabilities in the frontend image" && exit 1)
                """
            }
        }

        stage('9. AWS ECR Login') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-credentials'
                ]]) {
                    sh """
                        aws ecr get-login-password --region ${AWS_REGION} | \
                        docker login --username AWS --password-stdin ${ECR_REGISTRY}
                    """
                }
            }
        }

        stage('10. Docker Push to ECR') {
            steps {
                sh """
                    docker tag ${ECR_REPO_BACKEND}:${IMAGE_TAG} ${ECR_REGISTRY}/${ECR_REPO_BACKEND}:${IMAGE_TAG}
                    docker push ${ECR_REGISTRY}/${ECR_REPO_BACKEND}:${IMAGE_TAG}

                    docker tag ${ECR_REPO_FRONTEND}:${IMAGE_TAG} ${ECR_REGISTRY}/${ECR_REPO_FRONTEND}:${IMAGE_TAG}
                    docker push ${ECR_REGISTRY}/${ECR_REPO_FRONTEND}:${IMAGE_TAG}
                """
            }
        }

        stage('11. Ansible Deployment') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-credentials'
                ]]) {
                    dir('ansible') {
                        sh """
                            ansible-playbook -i inventory deploy.yml \
                              -e image_tag=${IMAGE_TAG} \
                              -e ecr_registry=${ECR_REGISTRY} \
                              -e aws_region=${AWS_REGION} \
                              -e eks_cluster_name=${EKS_CLUSTER_NAME}
                        """
                    }
                }
            }
        }

        stage('12. Kubernetes Rollout Status') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-credentials'
                ]]) {
                    sh """
                        aws eks update-kubeconfig --name ${EKS_CLUSTER_NAME} --region ${AWS_REGION}
                        kubectl rollout status deployment/foodgo-backend -n foodgo --timeout=180s
                        kubectl rollout status deployment/foodgo-frontend -n foodgo --timeout=180s
                    """
                }
            }
        }

        stage('13. Health Check') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-credentials'
                ]]) {
                    sh '''
                        set -e
                        BACKEND_POD=$(kubectl get pods -n foodgo -l app=foodgo-backend -o jsonpath="{.items[0].metadata.name}")
                        kubectl exec -n foodgo "$BACKEND_POD" -- wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"'
                        echo "Backend health check passed."
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "FoodGo build #${env.BUILD_NUMBER} deployed successfully to EKS cluster ${EKS_CLUSTER_NAME}."
        }
        failure {
            echo "FoodGo build #${env.BUILD_NUMBER} failed. Check the stage logs above for details."
        }
        always {
            sh 'docker image prune -f || true'
        }
    }
}
