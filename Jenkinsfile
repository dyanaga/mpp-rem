pipeline {
  agent any

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

    stage('Dockerizing') {
      steps {
           sh "docker build -t rem_be ."
        }
    }

    stage('Deployment') {
      steps {
        script {
            if (env.BRANCH_NAME == 'main') {
                sh "kubectl rollout restart deployment rem-be-deployment"
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
                    sh "kubectl rollout restart deployment rem-be-deployment"
                }
            }
        }
      }
    }

  }
}