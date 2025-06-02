pipeline {
    agent { label 'lenovo_agent' }

    stages {
        stage('Test') {
            steps {
                bat 'mvn test -Dgroups=smoke'
            }
        }
    }

    post {
        always {
            allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
        }
    }
}
