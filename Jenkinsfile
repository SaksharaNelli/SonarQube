// Declarative pipeline: build -> test -> SonarQube analysis -> quality gate -> package
// Names used below must match what you configure in Jenkins:
//   Manage Jenkins > Tools   : JDK named 'JDK17', Maven named 'Maven3'
//   Manage Jenkins > System  : SonarQube server named 'SonarQube'

// Runs a command on Windows (bat) or Linux/macOS (sh).
def run(String cmd) {
    if (isUnix()) {
        sh cmd
    } else {
        bat cmd
    }
}

pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                run 'mvn -B clean verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    run 'mvn -B sonar:sonar'
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

        stage('Package') {
            steps {
                run 'mvn -B package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Pipeline passed: code met the Quality Gate.'
        }
        failure {
            echo 'Pipeline failed: check the test results or the SonarQube Quality Gate.'
        }
    }
}
