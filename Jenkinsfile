pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'MAVEN_HOME'
        SONAR = 'SonarLocal'
        TOMCAT_USER = "admin"
        TOMCAT_PASS = "admin"
        TOMCAT_URL = "http://localhost:8080"
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/DiegoAM13/PYG.git'
            }
        }

        stage('Build Maven') {
            steps {
                bat '"%MAVEN_HOME%/bin/mvn" clean package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(SONAR) {
                    bat '"%MAVEN_HOME%/bin/mvn" sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Deploy to Test') {
            steps {
                deployToTomcat("test")
            }
        }

        stage('Deploy to Prod') {
            when {
                branch 'main'
            }
            steps {
                deployToTomcat("prod")
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
    curl -u ${env.TOMCAT_USER}:${env.TOMCAT_PASS} -T target/*.war "${env.TOMCAT_URL}/manager/text/deploy?path=/${envName}&update=true"
    """
}
