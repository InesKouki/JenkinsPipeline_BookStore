pipeline {
   agent any
     tools {
        jdk 'OpenJDK11'
        maven 'Maven'
    }
    
    environment {
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
                    docker.withRegistry('',DOCKER_PASS) {
                        docker_image = docker.build "${IMAGE_NAME}"
                    }

                    docker.withRegistry('',DOCKER_PASS) {
                        docker_image.push("${IMAGE_TAG}")
                        docker_image.push('latest')
                    }
                }
            }
}

stage("Deploy Docker Image") {
    steps {
        script {
            def containerName = "${APP_NAME}-${BUILD_NUMBER}"
            
            // Stop and remove the container if it exists
            try {
                sh "docker stop ${containerName}"
                sh "docker rm ${containerName}"
                echo "Docker container '${containerName}' stopped and removed."
            } catch (Exception e) {
                echo "Docker container '${containerName}' doesn't exist or couldn't be stopped/removed."
            }
            
            // Pull and run the Docker image
            try {
                sh "docker pull ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker run -d --name ${containerName} ${IMAGE_NAME}:${IMAGE_TAG}"
                echo "Docker container '${containerName}' started."
            } catch (Exception e) {
                echo "Failed to pull or start the Docker container '${containerName}'."
            }
        }
    }
}





}
}
