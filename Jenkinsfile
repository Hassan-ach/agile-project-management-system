pipeline {
    agent any

    triggers {
        githubPush()
    }

    tools {
        jdk 'jdk-21'
        maven 'Maven-3.9'
    }

    environment {
        DB_NAME = 'agile_db'
        DB_USER = 'admin'
        DB_PASSWORD = '1234'
        DB_PORT = '5432'
        DB_HOST = 'localhost'
        JWT_SECRET_KEY = 'fc8153c3bfca60f62af67835b8118e9710af4e065cbab2b24f3ffdd3193e1465'
    }


    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('DB Setup') {
            steps {
                sh 'docker compose up -d'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo Deploying application...'
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded'
        }
        failure {
            echo 'Pipeline failed'
        }
    }
}
