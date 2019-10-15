# Lingua Greeter

A Knative service to translate the greeting using Google Translate API

## Create kubernetes Namespace

```shell
kubectl create namespace knativetutorial
```

## Google Application Credentials

We will be using Google Translate to translate the text https://cloud.google.com/translate/docs/quickstarts

```shell
k create secret generic -n knativetutorial google-cloud-creds --from-file=google-cloud-credentials.json=<your-gcp-credentials>
```

## Deploy Knative service

```shell

```
