pipeline {
   agent any
     tools {
        jdk 'OpenJDK11'
        maven 'Maven'
    }
    
    environment {
       NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "http://172.20.0.5:8081"
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
       stage("Sonarqube Analysis") {
            steps {
                script {
                    withSonarQubeEnv('Sonarqube') {
                        sh "mvn sonar:sonar"
                    }
                }
            }
        }
        
        stage("Quality Gate") {
            steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube'
                }
            }

        }
        
        stage("Publish to Nexus Repository Manager") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
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

stage("Deploy Docker Image") {
    steps {
        script {
            def containerName = "${APP_NAME}-${IMAGE_TAG}"
            
            // Stop and remove the container if it exists
            try {
                sh "docker stop ${containerName}"
                sh "docker rm ${containerName}"
                echo "Docker container '${containerName}' stopped and removed."
            } catch (Exception e) {
                echo "Docker container '${containerName}' doesn't exist or couldn't be stopped/removed."
            }
            
            // Pull and run the Docker image
            docker.withRegistry('https://index.docker.io/v1/', DOCKER_PASS) {
                try {
                    sh "docker pull ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker run -d --name ${containerName} --network jenkins-maven-network --restart always ${IMAGE_NAME}:${IMAGE_TAG}"
                    echo "Docker container '${containerName}' started."
                } catch (Exception e) {
                    echo "Failed to pull or start the Docker container '${containerName}': ${e.message}"
                    currentBuild.result = 'FAILURE'
                }
            }
        }
    }
}

}
}
