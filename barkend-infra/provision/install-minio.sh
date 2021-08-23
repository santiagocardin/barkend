#!/usr/bin/env bash
set -e

# Reference: https://docs.min.io/minio/k8s/tenant-management/deploy-minio-tenant-using-commandline.html#deploy-minio-tenant-commandline
wget https://github.com/minio/operator/releases/download/v4.1.3/kubectl-minio_4.1.3_linux_amd64 -O kubectl-minio
chmod +x kubectl-minio &&
mv kubectl-minio /usr/local/bin/

kubectl create namespace minio
kubectl minio init --namespace minio
kubectl minio tenant create minio-tenant-1 \
        --servers 1 \
        --volumes 4 \
        --capacity 1Gi \
        --storage-class local-path \
        --namespace minio