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
        EMAIL = "diegoalejandromejiagiraldo5@gmail.com"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/DiegoAM13/PYG.git'
            }
        }

        stage('Build & Test') {
            steps {
                bat "mvn clean package"
            }
        }

        stage('Deploy to Test') {
            steps {
                script {
                    deployToTomcat("test")
                }
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
                timeout(time: 3, unit: 'MINUTES') {
                    script {
                        def gate = waitForQualityGate abortPipeline: true

                        if (gate.status != 'OK') {
                            echo "⚠️ Sonar: Código malo"
                            emailext(
                                subject: "❌ Código Rechazado por Sonar",
                                body: "Sonar ha detectado problemas. No se hará deploy a producción.",
                                to: EMAIL
                            )
                            error "SonarQube no aprobó el código"
                        } else {
                            echo "✔ Código OK, se continúa"
                        }
                    }
                }
            }
        }

        

       stage('Deploy to Production') {
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
                to: EMAIL
            )
        }
        failure {
            emailext(
                subject: "❌ Deploy Falló",
                body: "El pipeline falló. Revisa Sonar o el Build",
                to: EMAIL
            )
        }
    }
}

def deployToTomcat(envName) {
    bat """
    for %%f in (target\\*.war) do (
        echo Deploying: %%f
        curl -u %TOMCAT_USER%:%TOMCAT_PASS% ^
         -T %%f ^
         "%TOMCAT_URL%/manager/text/deploy?path=/${envName}&update=true"
    )
    """
}
