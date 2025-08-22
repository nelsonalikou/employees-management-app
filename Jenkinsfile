pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                echo "Building..."
                dir('backend') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
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
                sh 'docker-compose up -d --build'
            }
        }
    }
}