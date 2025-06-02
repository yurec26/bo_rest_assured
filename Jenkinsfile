pipeline {
    agent { label 'lenovo_agent' }
    stages {
        stage('Test') {
            steps {
                sh 'mvn test -Dgroups=smoke'
            }
        }
    }
}
