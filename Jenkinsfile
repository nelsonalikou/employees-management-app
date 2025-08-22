pipeline {
    agent any

    environment {
        BACKEND_DIR = 'backend'
        IMAGE_NAME = 'employees-management-app'
        CONTAINER_NAME = 'employees-app'
        SERVER_PORT = '8085'
    }

    stages {
        stage("verify tooling") {
            steps {
                sh '''
                docker version
                docker info
                docker-compose version
                curl --version
                jq --version
                '''
            }
        }

        stage('Build Backend') {
            steps {
                environment {
                    SERVER_PORT = '8081'
                }
                echo 'Building...'
                dir("${env.BACKEND_DIR}") {
                    sh '''
                    chmod +x mvnw
                    ./mvnw clean package -DskipTests
                    '''
                }
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Testing...'
                sh 'cd backend && ./mvnw test'
            }
        }

        stage("Build Docker Image") {
            steps {
                script {
                    echo "🔨 Building Docker image..."
                    // Build a fresh image with unique tag per Jenkins build
                    def appImage = docker.build("${IMAGE_NAME}:${env.BUILD_NUMBER}", "${BACKEND_DIR}")
                    // Also tag it as latest for convenience
                    sh "docker tag ${IMAGE_NAME}:${env.BUILD_NUMBER} ${IMAGE_NAME}:latest"
                }
            }
        }

        stage("Run Container") {
            steps {
                script {
                    echo "🚀 Starting container..."
                    def appImage = docker.image("${IMAGE_NAME}:latest")
                    // Run in detached mode, mapped port 8085, and passing the SERVER_PORT env var
                    appImage.run("-d -p ${SERVER_PORT}:${SERVER_PORT} --name ${CONTAINER_NAME} -e SERVER_PORT=${SERVER_PORT}")
                }
            }
        }

        stage("Test API") {
            steps {
                script {
                    echo "🧪 Running API test..."
                    try {
                        sh "curl -f http://localhost:${SERVER_PORT}/employees | jq"
                    } catch (Exception e) {
                        error("❌ API endpoint returned a non-200 status code.")
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                echo "🧹 Cleaning up only Jenkins-related Docker resources..."
                // Stop & remove the container if it exists
                sh "docker stop ${CONTAINER_NAME} || true"
                sh "docker rm ${CONTAINER_NAME} || true"

                // Remove ONLY the images we built
                sh "docker rmi ${IMAGE_NAME}:${env.BUILD_NUMBER} || true"
                sh "docker rmi ${IMAGE_NAME}:latest || true"
            }
        }
    }
}