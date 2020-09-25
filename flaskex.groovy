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
        stage("Install epel-release") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum install epel-release -y"
        }
        stage("Install python") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum install python-pip -y"
        }
        stage("Install update pip") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } pip install --update pip"
        }
        stage("Install clone Flaskex") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } git clone https://github.com/anfederico/Flaskex"
        }
        stage("pip requirements") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } pip install -r /root/Flaskex/requirements.tx"
        }
        stage("pip update") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } pip install --upgrade pip"
        }
        stage("run app") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } python /root/Flaskex/app.py"
        }
    }
}
