pipeline {
    agent any
    options {
    timeout(time: 1, unit: 'HOURS') // set timeout 1 hour
    }

    environment {

        TIME_ZONE = 'Asia/Seoul'

        //github
        TARGET_BRANCH = 'main'
        REPOSITORY_URL= 'https://github.com/TEAM-FLOWBIT/USER_SERVICE_MSA.git'

        //docker-hub
        registryCredential = 'docker-hub'

        CONTAINER_NAME = 'flowbit-userservice'
        IMAGE_NAME = 'kimminwoo1234/flowbit-userservice'
    }



    stages {


        stage('init') {
            steps {
                echo 'init stage'
                deleteDir()
            }
            post {
                success {
                    echo 'success init in pipeline'
                }
                failure {
                    slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                    error 'fail init in pipeline'
                }
            }
        }

        stage('Prepare') {
            steps {
                echo 'Cloning Repository'
                git branch: 'main',
                    url: 'https://github.com/TEAM-FLOWBIT/USER_SERVICE_MSA.git'
            }
            post {
                success {
                    echo 'Successfully Cloned Repository'
                }
                failure {
                    slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                    error 'This pipeline stops here...'
                }
            }
        }
      // 일단은 merge 하기전에 테스트통과함으로 테스트없이 빌드
        stage('Build Maven') {
            steps {
                echo 'Build Gradle'

                dir('.'){
                    sh '''
                        pwd
                        cd /var/jenkins_home/workspace/flowbit-userservice
                        ./mvnw clean package -DskipTests

                    '''
                }
            }
            post {
                failure {
                    slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                    error 'This pipeline stops here...'
                }
            }
        }

        // 도커 이미지를 만든다. build number로  latest 태그 부여한다.
        stage('Build Docker') {
            steps {
                echo 'Build Docker'
                sh """
                    cd /var/jenkins_home/workspace/flowbit-userservice
                    docker build -t $IMAGE_NAME:latest .
                """
            }
            post {
                failure {
                    slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                    error 'This pipeline stops here...'
                }
            }
        }


     // 빌드넘버 latest
        stage('Push Docker') {
            steps {
                echo 'Push Docker'
                script {

                    docker.withRegistry('', registryCredential) {
                        docker.image("${IMAGE_NAME}:latest").push()
                    }
                }
            }
            post {
                failure {
                    slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                    error 'This pipeline stops here...'
                }
            }
        }



    stage('rm container and rm images') {
            steps {
                echo 'rm container stage'
                sh '''
                docker rm -f $CONTAINER_NAME
                docker image prune -f --filter "label=${IMAGE_NAME}"
                '''
            }
            post {
                success {
                    echo 'success rm container in pipeline'
                }
                failure {
                    slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                    error 'fail rm container in pipeline'
                }
            }
    }

    stage('Docker run') {
            steps {
                echo 'Pull Docker Image & Docker Image Run'
                sh """
                    docker run -d \
                    --name ${CONTAINER_NAME} \
                    --restart always \
                    --network likelion_default \
                    -e SPRING_DATASOURCE_PASSWORD=${env.SPRING_DATASOURCE_PASSWORD} \
                    --link db-mysql \
                    --link discovery-service \
                    --link apigateway-service \
                    --link rabbitmq \
                    --link config-server \
                    ${IMAGE_NAME}:latest
                """


            }
            post {
                    failure {
                      echo 'Docker Run failure !'
                    }
                    success {
                      echo 'Docker Run Success !'
                    }
            }
        }



    stage('Clean Up Docker Images on Jenkins Server') {
        steps {
            echo 'Cleaning up unused Docker images on Jenkins server'

            // Clean up unused Docker images, including those created within the last hour
            sh "docker image prune -f --all --filter \"until=1m\""
        }
    }




}

    post {
        success {
            slackSend (channel: '#backend', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend (channel: '#backend', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
}