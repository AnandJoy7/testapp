pipeline { 
    agent any 

    environment { 
        GITHUB_CREDENTIALS_ID = 'github-credentials'  // GitHub Credentials ID 
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-credentials'  // DockerHub Credentials ID 
        DOCKER_IMAGE = 'anandjoy7/testapp:latest'  // Docker image name 
    } 

    stages { 
        stage('Clone Repository') { 
            steps { 
                git credentialsId: "${env.GITHUB_CREDENTIALS_ID}", url: 'https://github.com/AnandJoy7/testapp.git' 
            } 
        } 
        stage('Build Docker Image') { 
            steps { 
                script { 
                    docker.build("${env.DOCKER_IMAGE}") 
                } 
            } 
        } 
        stage('Push Docker Image') { 
            steps { 
                script { 
                    withDockerRegistry(credentialsId: "${env.DOCKERHUB_CREDENTIALS_ID}", url: 'https://index.docker.io/v1/') { 
                        docker.image("${env.DOCKER_IMAGE}").push('latest') 
                    } 
                } 
            } 
        } 
    } 

    post { 
        always { 
            cleanWs() 
        } 
    } 
}
