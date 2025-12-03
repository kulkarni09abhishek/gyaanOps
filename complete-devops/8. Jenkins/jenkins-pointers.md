# 🚀 Jenkins Pipeline as Code
## 📌 1. Introduction to Jenkins Pipeline as Code
Jenkins Pipeline is a suite of plugins that let you define CI/CD as code using a Jenkinsfile.
### ✅ Why Pipeline as Code
- Version-controlled pipeline
- Reproducibility
- Code review via PRs
- Scalability (shared libraries)
- Resilient / restartable pipelines
- Auditability

### 🧱 Two Pipeline Types
| Type                     | Syntax                  | Usage                |
| ------------------------ | ----------------------- | -------------------- |
| **Declarative Pipeline** | opinionated, structured | recommended for most |
| **Scripted Pipeline**    | pure Groovy, flexible   | complex logic        |


## 📌 2. Declarative Pipeline Structure
```groovy
pipeline {
  agent any

  environment {
    APP_ENV = "prod"
  }

  options {
    timeout(time: 30, unit: 'MINUTES')
    skipStagesAfterUnstable()
  }

  parameters {
    string(name: 'BRANCH', defaultValue: 'main')
    booleanParam(name: 'RUN_TESTS', defaultValue: true)
  }

  triggers {
    cron('H/15 * * * *')
  }

  tools {
    maven 'Maven-3.8.1'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build') {
      steps { sh 'mvn package -DskipTests' }
    }

    stage('Test') {
      when { expression { params.RUN_TESTS == true } }
      steps { sh 'mvn test' }
    }

    stage('Deploy') {
      when { branch 'main' }
      steps { sh './deploy.sh' }
    }
  }

  post {
    always { echo 'Pipeline Finished' }
    success { echo 'Build OK!' }
    failure { mail to: 'team@company.com', subject: 'Failed!' }
  }
}
```


## 📌 3. Scripted Pipeline Structure
```groovy
node {
  stage('Checkout') {
    checkout scm
  }

  stage('Build') {
    sh 'mvn package'
  }

  stage('Test') {
    sh 'mvn test'
  }
}
```
### When to Use Scripted
- Dynamic runtime logic
- Complex branching loops
- Need full Groovy control

## 📌 4. Agents in Jenkins Pipeline
### Types of Agents
```groovy
agent any
agent none
agent { label 'docker' }
agent { docker { image 'maven:3.8.1' args '-v /tmp:/tmp' } }
```

### Running each stage on different agents
```groovy
stage('Test') {
  agent { label 'test-node' }
  steps { sh 'pytest' }
}
```

## 📌 5. Environment Variables
### Global Environment Block
```groovy
environment {
  DB_USER = "root"
  DB_PASS = credentials('db-password')
}
```

### Accessing Environment Variables
```groovy
echo "USER = ${DB_USER}"
```

## 📌 6. Stages & Steps
### Basic Stage
```groovy
stage('Build') {
  steps { sh 'mvn package' }
}
```

## Parallel Stages
```groovy
stage('Parallel Tests') {
  parallel {
    unit: { sh 'pytest tests/unit' }
    integration: { sh 'pytest tests/integration' }
  }
}
```


## 📌 7. When Conditions
```groovy
stage('Deploy') {
  when {
    branch 'main'
  }
  steps { sh './deploy.sh' }
}
```

### Other when Types
- expression
- environment
- equals
- not
- anyOf, allOf


## 📌 8. Post Actions
```groovy
post {
  always { echo 'Always runs' }
  success { echo 'Build Successful!' }
  failure { echo 'Build Failed!' }
  unstable { echo 'Unstable build' }
}
```

## 📌 9. Parameters in Pipeline
```groovy
parameters {
  string(name: 'TAG', defaultValue: 'latest')
  booleanParam(name: 'RUN_TESTS', defaultValue: true)
  choice(name: 'ENV', choices: ['dev', 'qa', 'prod'])
}
```

## 📌 10. Credentials
### Using Username/Password
```groovy
withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
  sh "docker login -u $USER -p $PASS"
}
```

### Using Secret Text
```groovy
withCredentials([string(credentialsId: 'token', variable: 'TOKEN')]) {
  sh "curl -H \"Authorization: Bearer $TOKEN\" ..."
}
```

## 📌 11. Artifacts & Workspace
### Archiving Artifacts
```groovy
archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
```

### Stashing Files (move between agents)
```groovy
stash includes: 'src/**', name: 'source'
unstash 'source'
```

## 📌 12. Docker with Jenkins Pipeline
### Build Docker Image
```groovy
stage('Docker Build') {
  steps {
    sh 'docker build -t myapp:latest .'
  }
}
```

### Using Docker Agent
```groovy
agent {
  docker { image 'node:18' }
}
```

## 📌 13. Shared Libraries (Advanced but critical for interview)
### Add in Jenkins:
```Manage Jenkins → Configure System → Global Pipeline Libraries```

Use in Jenkinsfile
```groovy
@Library('my-shared-lib') _
common.sayHello("Nancy")
```

Folder Structure
```bash
vars/hello.groovy
src/org/company/utils.groovy
```

## 📌 14. Retry, Timeout, Timestamps
```groovy
retry(3) { sh 'flake-job' }
timeout(time: 5, unit: 'MINUTES') { sh './deploy.sh' }
timestamps { sh 'echo hello' }
```

## 📌 15. Error Handling
```groovy
try {
  sh 'deploy'
} catch (err) {
  echo "Failed: ${err}"
}
```

## 📌 16. Matrix Pipelines
```groovy
matrix {
  axes {
    axis { name 'OS'; values 'ubuntu', 'centos' }
    axis { name 'VERSION'; values '8', '11' }
  }
  stages {
    stage('Test') {
      steps { sh 'run-tests.sh' }
    }
  }
}
```

## 📌 17. Multibranch Pipelines
### Key points:
- Automatically scans branches
- PRs automatically get pipelines
- Usually placed in GitHub/Bitbucket
- Uses Jenkinsfile from each branch

## 📌 18. Blue Ocean UI
- Modern visualization of pipelines
- Great for interview if mentioned
- Easier understanding of parallel stages

---

# Jenkins Shared Library Example

📁 1. Folder Structure
```vbnet
jenkins-shared-lib/
│
├── vars/
│   └── sayHello.groovy
│
└── README.md
```

📌 2. vars/sayHello.groovy
Every file in vars/ becomes a global step.
✔ Must contain a function named call()
✔ This function can be used directly in Jenkinsfile

📄 sayHello.groovy
```groovy
def call(String name = "World") {
    echo "Hello, ${name} from Shared Library!"
}
```

📌 3. Jenkinsfile Example
Use the shared library in any Jenkins pipeline:
```groovy
@Library('my-shared-lib') _

pipeline {
    agent any

    stages {
        stage('Test Shared Library') {
            steps {
                sayHello("Nancy")
            }
        }
    }
}
```

### @Library('my-shared-lib') — Annotation
- @Library is a Jenkins Pipeline annotation.
- You place it at the top of your Jenkinsfile (before pipeline block).
- 'my-shared-lib' is the name you configured in:
```Jenkins → Manage Jenkins → Global Pipeline Libraries```
- “Jenkins allows multiple @Library annotations. There is no hard limit. You can load many shared libraries, and Jenkins merges all their global steps. The only caution is name conflicts.”
When Jenkins sees:
```groovy
@Library('my-shared-lib')
```

it will:
✔ Clone the shared library repository
✔ Load all code inside vars/ and src/
✔ Make global functions available in your pipeline

🔍 Why the Underscore _ ?
```groovy
@Library('my-shared-lib') _
```
The underscore is required when loading a library using the annotation.
It means:
```"Load the library without assigning it to a variable."```

Why underscore?
In Groovy, _ is treated as an unused variable placeholder.
It satisfies the syntax requirement of the annotation but you don’t use it anywhere.

If you try without _:
❌ Jenkins will throw a syntax error.

🔍 Alternative: Load and Use it as a Variable
Instead of _, you can load the library into a variable:
```groovy
def lib = library('my-shared-lib')
```


## Top Jenkins Environment Variables (Most Important)
✔️ Build & Job Information
| Variable              | Description                              |
| --------------------- | ---------------------------------------- |
| **`BUILD_NUMBER`**    | Jenkins build number (e.g., 15).         |
| **`BUILD_ID`**        | Legacy build ID (not recommended).       |
| **`BUILD_TAG`**       | Unique tag combining job + build number. |
| **`BUILD_URL`**       | URL of the build in Jenkins UI.          |
| **`JOB_NAME`**        | Name of the Jenkins job.                 |
| **`JOB_BASE_NAME`**   | Last part of job name (without folder).  |
| **`EXECUTOR_NUMBER`** | Executor assigned to the build.          |


✔️ Git / SCM Variables
| Variable                  | Description                      |
| ------------------------- | -------------------------------- |
| **`GIT_COMMIT`**          | Full commit hash being built.    |
| **`GIT_PREVIOUS_COMMIT`** | Previous commit built.           |
| **`GIT_BRANCH`**          | Git branch name (format varies). |
| **`GIT_URL`**             | Repo URL.                        |


In multibranch pipeline:
- Use env.GIT_BRANCH
- Or Jenkins’ improved variables:
    - env.BRANCH_NAME
    - env.CHANGE_ID (for PRs)


✔️ Pipeline-Specific
| Variable                 | Description                                 |
| ------------------------ | ------------------------------------------- |
| **`env.` prefix**        | Access any env variable: `env.BUILD_NUMBER` |
| **`params.` prefix**     | Access Jenkins parameters: `params.VERSION` |
| **`currentBuild.*`**     | Build result, duration, causes.             |
| **`BUILD_DISPLAY_NAME`** | Friendly display name for build.            |


## How to Run the SAME Jenkins Pipeline to Deploy on Multiple Kubernetes Environments?
⭐ Approach 1 — Use Pipeline Parameters (MOST COMMON)
You provide an environment name (dev/stage/prod) when starting the pipeline.
Step 1 — Add parameter
```groovy
pipeline {
  agent any
  
  parameters {
    choice(
      name: 'ENV',
      choices: ['dev', 'qa', 'stage', 'prod'],
      description: 'Select deployment environment'
    )
  }
```

Step 2 — Use different kubeconfig/context per env
```groovy
stages {
  stage('Deploy') {
    steps {
      script {
        if (params.ENV == 'dev') {
          sh "kubectl --kubeconfig=kubeconfig-dev apply -f deployment.yaml"
        }
        if (params.ENV == 'stage') {
          sh "kubectl --kubeconfig=kubeconfig-stage apply -f deployment.yaml"
        }
        if (params.ENV == 'prod') {
          sh "kubectl --kubeconfig=kubeconfig-prod apply -f deployment.yaml"
        }
      }
    }
  }
}
```

✔️ Advantages
- One pipeline, many environments
- Easy to trigger manually
- Works with different cluster configs


⭐ Approach 2 — Use Shared Library for Reusable Deployment Logic
Step 1 — Shared library function (vars/k8sDeploy.groovy)
```groovy
def call(envName) {
    sh "kubectl --kubeconfig=kubeconfig-${envName} apply -f deployment.yaml"
}
```

Step 2 — Jenkinsfile
```groovy
@Library('k8s-lib') _

pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['dev', 'qa', 'prod'])
    }

    stages {
        stage('Deploy') {
            steps {
                k8sDeploy(params.ENV)
            }
        }
    }
}
```













