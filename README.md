# Barkend project

This is ...

Components are organized like this:

```
|- barkend
   |- barkend-infra                     (k8s infrastructure)
   |- barkend-alarm-launcher            (k8s micro service)
   |- barkend-bark-detector             (k8s micro service)
   |- barkend-noise-stream-processor    (k8s micro service)
   |- barkend-ml-serving                (k8s ML serving service)
   |- barkend-ml-training               (Keras ML training)   
   |- barkend-sound-capture             (Raspberry Pi scripts)

```
## Prerequisites

All components are deployed via helm charts. If you have an available kubernetes cluster you can skip this part, otherwise you can use the provided Vagrantfile to spin up a local one:

    $ vagrant up

Once you have downloaded and installed everything (be patient...) you should be able to access the local cluster:

    $ vagrant ssh -c 'kubectl get node'

## Build

In order to build the microservices you need to run:

    $ mvn clean package

## Install

## Tools

### Minio

* Start:
  
        kubectl port-forward -n barkend svc/minio-tenant-1-console 9443:9443

* URL: https://localhost:9443/dashboard

* Credentials:
    admin / $S3_SECRET

### Kafdrop

* Start:

        kubectl proxy

* URL: http://localhost:8001/api/v1/namespaces/kafka/services/http:kafdrop:9000/proxy/