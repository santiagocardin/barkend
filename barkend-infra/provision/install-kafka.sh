#!/usr/bin/env bash
set -e

# Reference: https://strimzi.io/quickstarts/
kubectl create namespace kafka
kubectl -n kafka create -f 'https://strimzi.io/install/latest?namespace=kafka'
kubectl -n kafka apply -f $PROVISION_HOME/kafka/kafka-persistence-single.yaml