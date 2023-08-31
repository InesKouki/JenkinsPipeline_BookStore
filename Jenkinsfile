pipeline {
   agent any
     tools {
        jdk 'OpenJDK11'
        maven 'Maven'
    }
    
    environment {
       NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "172.20.0.5:8081"
        NEXUS_REPOSITORY = "JenkinsNexus"
        NEXUS_CREDENTIAL_ID = "NEXUS_CRED"

        APP_NAME = "jenkins-pipeline-bookstore"
        RELEASE = "1.0.0"
        DOCKER_USER = "ineskouki"
        DOCKER_PASS = "DOCKER_CRED"
        IMAGE_NAME = "${DOCKER_USER}/${APP_NAME}"
        IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
       
    }
    stages {
            stage("Cleanup Workspace"){
            steps {
                cleanWs()
            }

        }
        stage('Checkout from SCM') {
            steps {
              git branch: 'main', url: 'https://github.com/InesKouki/JenkinsPipeline_BookStore.git'
            }
        }
       stage("Build Application"){
            steps {
                sh "mvn clean package"
            }
        }
        stage("Test Application"){
            steps {
                sh "mvn test"
            }
        }
       

       stage("Build & Push Docker Image") {
    steps {
        script {
            docker.withRegistry('https://index.docker.io/v1/', DOCKER_PASS) {
                def docker_image = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                docker_image.push()
                docker_image.push('latest')
            }
        }
    }
}

stage("Deploy to Minikube") {
            steps {
                script {
                    sh 'kubectl apply -f deployment.yml'
                }
            }
        }

}
}
