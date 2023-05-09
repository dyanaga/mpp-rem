pipeline {
  agent any
  environment {
    CONTAINER = 'rem-be'
    DOCKER_IMAGE = 'rem-be:latest'
  }
  parameters {
    string(name: 'image_version', defaultValue: 'undefined')
  }

  stages {
    stage('Initialize') {
      steps {
        sh '''mvn clean'''
      }
    }

    stage('Build') {
      steps {
        sh 'mvn install -Dspring.profiles.active=test'
      }
    }

    stage('Deployment') {
      steps {
        script {
            def docker_image = "docker.olahistvan.com/${env.DOCKER_USER}/${env.DOCKER_IMAGE}:${env.image_version}"
            if (env.BRANCH_NAME == 'main') {
                sh "sudo docker-compose up -d ${env.CONTAINER}"
            } else {
                def userInput = false
                try {
                    timeout(time: 5, unit: 'MINUTES') {
                        userInput = input(message: 'Deploy app?', ok: 'Save',
                                                parameters: [booleanParam(defaultValue: false,
                                                description: 'This is not main branch, so it will not be automatically deployed, but would you like it to be?',
                                                name: 'Yes, deploy it.')])
                    }
                    echo "User selected ${userInput}"
                } catch(org.jenkinsci.plugins.workflow.steps.FlowInterruptedException err) {
                    echo "FlowInterruptedException caught"
                    echo "Timed out. Defaulting to ${userInput}"
                }
                catch(hudson.AbortException err) {
                    echo "AbortException caught"
                    userInput = false
                    echo "Aborted by: [${user}]"
                }
                echo "Selected deployment option: ${userInput}"

                if(userInput) {
                    sh "sudo docker-compose up -d ${env.CONTAINER}"
                }
            }
        }
      }
    }

  }
}