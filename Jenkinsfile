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
            def app_container = docker.image("${IMAGE_NAME}:${IMAGE_TAG}")
            
            // Run the container with port mapping
            def container_id = app_container.run("-p 8082:8080 -d")
            
            // Print the container ID for reference
            echo "Docker container ID: ${container_id}"
        }
    }
}


}
}
