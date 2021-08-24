#!/usr/bin/env bash
set -e

apt-get update -y

export APT_KEY_DONT_WARN_ON_DANGEROUS_USAGE=1
export DEBIAN_FRONTEND=noninteractive

# Install Docker
# Reference: https://docs.docker.com/engine/install/ubuntu/
apt-get install -y apt-transport-https ca-certificates curl gnupg-agent software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
apt-key fingerprint 0EBFCD88
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
apt-get update -y
apt-get install -y docker-ce docker-ce-cli containerd.io

# Install kubectl
curl -LOsS "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
mv kubectl /usr/local/bin/ && chmod +x /usr/local/bin/kubectl

# Install helm
# Reference: https://helm.sh/docs/intro/install/
curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh

# Install ZSH
apt-get install -y zsh
chsh -s $(which zsh)

# Add vagrant user to docker group (Running docker command without sudo)
addgroup -a vagrant docker

# Install k3d
# Reference: https://github.com/rancher/k3d
wget -q -O - https://raw.githubusercontent.com/rancher/k3d/main/install.sh | bash

k3d cluster create barkend --agents 1 --servers 1 --api-port 127.0.0.1:6443 -p 8080:80@loadbalancer

# Configure kube config for vagrant user
mkdir -p /home/vagrant/.kube
cp -i /root/.kube/config /home/vagrant/.kube/config
chown vagrant:vagrant -R /home/vagrant/.kube