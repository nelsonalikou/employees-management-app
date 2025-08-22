pipeline {
    agent any

    environment {
        BACKEND_DIR = 'backend'
        IMAGE_NAME = 'employees-management-app'
        CONTAINER_NAME = 'employees-app'
        SERVER_PORT = '8085'
        BUILD_NUMBER = "123"
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
                sh "docker-compose up --build -d"
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
            sh "docker ps"
            sh "docker-compose down"
        }
    }
}
