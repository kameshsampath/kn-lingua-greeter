# Lingua Greeter

A Knative service to translate the greeting using Google Translate API

## Pre-requisites

* [Kafka](https://stirmzi.io)
* [Tekton Pipelines](https://tekton.dev)

## Google Application Credentials

We will be using Google Translate to translate the text https://cloud.google.com/translate/docs/quickstarts

```shell
kubectl create ns knativetutorial
kubectl create secret generic -n knativetutorial google-cloud-creds --from-file=google-cloud-credentials.json=<your-gcp-credentials>
```

## Download sources

git clone this repository `git clone https://github.com/kameshsampath/kn-lingua-greeter`. Lets call this cloned repository directory as `$PROJECT_HOME`

## Build and Deploy

## minikube

```shell
./buildAndDeploy.sh
```

## Build with Tekton Pipelines

Create the pipeline service account

```shell
kubectl apply -f https://raw.githubusercontent.com/redhat-developer-demos/knative-tutorial/master/06-pipelines/pipeline-sa-role.yaml
```

```shell
kubectl apply  -n knativetutorial -f $PROJECT_HOME/kubernetes/build-resources.yaml
kubectl apply  -n knativetutorial -f $PROJECT_HOME/kubernetes/clients-task.yaml
kubectl apply  -n knativetutorial -f $PROJECT_HOME/kubernetes/build-app-task.yaml
kubectl apply  -n knativetutorial -f $PROJECT_HOME/kubernetes/build-and-deploy.yaml
```

Run the pipeline using the command

```shell
tkn pipeline start kn-svc-deploy \
 --param="mavenMirrorUrl=http://nexus.rhd-workshop-infra:8081/nexus/content/groups/public"  \
 --resource="appSource=lingua-greeter-git-source" \
 --resource="appImage=lingua-greeter-image" \
 --serviceaccount='pipeline'
```
