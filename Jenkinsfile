pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'JDK_HOME'
    }

    environment {
        SONAR = 'SonarLocal'
        TOMCAT_USER = "admin"
        TOMCAT_PASS = "admin"
        TOMCAT_URL = "http://localhost:8080"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/DiegoAM13/PYG.git'
            }
        }

        stage('Build Maven') {
            steps {
                bat "mvn clean package"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(SONAR) {
                    bat "mvn sonar:sonar"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Deploy to Test') {
            steps {
                script {
                    deployToTomcat("test")
                }
            }
        }

        stage('Deploy to Prod') {
            when {
                branch 'main'
            }
            steps {
                script {
                    deployToTomcat("prod")
                }
            }
        }
    }

    post {
        success {
            emailext(
                subject: "✔ Deploy Exitoso",
                body: "El deploy en Producción fue exitoso",
                to: "diegoalejandromejiagiraldo5@gmail.com"
            )
        }
        failure {
            emailext(
                subject: "❌ Deploy Falló",
                body: "El pipeline falló. Revisa Sonar o el Build",
                to: "diegoalejandromejiagiraldo5@gmail.com"
            )
        }
    }
}

def deployToTomcat(envName) {
    bat """
    curl -u %TOMCAT_USER%:%TOMCAT_PASS% ^
     -T target/*.war ^
     "%TOMCAT_URL%/manager/text/deploy?path=/${envName}&update=true"
    """
}
