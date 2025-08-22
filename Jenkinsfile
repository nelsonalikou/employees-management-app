pipeline {
    agent any

    environment {
        BACKEND_DIR = 'backend'
        IMAGE_NAME = 'employees-management-app'
        CONTAINER_NAME = 'employees-app'
        SERVER_PORT = '8085'
    }

    stages {
        stage("Verify Tooling") {
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
            environment {
                SERVER_PORT = '8081'
            }
            steps {
                echo 'Building backend...'
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
                echo 'Running unit tests...'
                dir("${env.BACKEND_DIR}") {
                    sh './mvnw test'
                }
            }
        }

        stage("Build Docker Image") {
            steps {
                echo "🔨 Building Docker image..."
                sh "docker build -t ${env.IMAGE_NAME}:${env.BUILD_NUMBER} ${env.BACKEND_DIR}"
                sh "docker tag ${env.IMAGE_NAME}:${env.BUILD_NUMBER} ${env.IMAGE_NAME}:latest"
            }
        }

        stage("Run Container") {
            steps {
                echo "🚀 Running container..."
                sh "docker stop ${env.CONTAINER_NAME} || true"
                sh "docker rm ${env.CONTAINER_NAME} || true"
                sh "docker run -d -p ${env.SERVER_PORT}:${env.SERVER_PORT} --name ${env.CONTAINER_NAME} -e SERVER_PORT=${env.SERVER_PORT} ${env.IMAGE_NAME}:latest"
            }
        }

        stage("Test API") {
            steps {
                echo "🧪 Testing API..."
                sh "curl -f http://localhost:${env.SERVER_PORT}/employees | jq"
            }
        }
    }

    post {
        always {
            echo "🧹 Cleaning up Docker resources..."
            sh "docker stop ${env.CONTAINER_NAME} || true"
            sh "docker rm ${env.CONTAINER_NAME} || true"
            sh "docker rmi ${env.IMAGE_NAME}:${env.BUILD_NUMBER} || true"
            sh "docker rmi ${env.IMAGE_NAME}:latest || true"
        }
    }
}
