pipeline {
    agent any
    environment {
        BACKEND_DIR = 'backend'
    }

    stages {
        stage("verify tooling") {
            steps {
                sh '''
                docker version
                docker info
                docker compose version
                curl --version
                jq --version 
                '''
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                dir("${env.BACKEND_DIR}") {
                    sh '''
                    chmod +x mvnw
                    ./mvnw clean package -DskipTests
                    '''
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                sh 'cd backend && ./mvnw test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                sh 'docker compose up -d --no-color --wait'
                sh 'docker compose ps'
            }
        }
        stage('Run tests against the container') {
            steps {
                script {
                    try {
                        sh 'curl -f http://localhost:8081/employees | jq'
                    } catch (Exception e) {
                        error('API endpoint returned a non-200 status code.')
                    }
                }
            }
        }
    }
    // Cleanup
    post {
        always {
            sh 'docker compose ps'
        }
    }
}