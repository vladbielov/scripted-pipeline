properties([
    parameters([
        string(defaultValue: '', description: 'Input node IP', name: 'SSHNODE', trim: true)
        ])
    ])

node {
    withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
        stage("yum update") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum update -y "
        }
        stage("Install git") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum install git -y"
        }
        stage("Install java") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum install java-1.8.0-openjdk -y"
        }
        stage("Install maven") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum install maven -y"
        }
        stage("install spring-petclinic") {
                sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }" 
                git clone "https://github.com/spring-projects/spring-petclinic.git"
                cd spring-petclinic
                ./mvnw package
                java -jar target/*.jar
            
        }
    }
}
