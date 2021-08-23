# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  config.vm.box = "hashicorp/bionic64"

  # Forwarded port mapping: 8052 -> 8502 for exposed service
  config.vm.network "forwarded_port", guest: 8080, host: 8080
  # Forwarded port mapping: 6443 -> 6443 for kubernetes API Server
  config.vm.network "forwarded_port", guest: 6443, host: 6443
  config.vm.network "private_network", ip: "192.168.33.15"

  config.vm.hostname = "barkend"

  config.vm.provider "virtualbox" do |vb|
    vb.name = "barkend"
    vb.memory = "8192"
    vb.cpus = "2"
  end

  # Enable provisioning guest VM with shell script
  config.vm.provision "shell", name: "base",  path: "barkend-infra/provision/install-base.sh"
  config.vm.provision "shell", name: "kafka", path: "barkend-infra/provision/install-kafka.sh", env: {"PROVISION_HOME" => "/vagrant/barkend-infra/provision"}
  config.vm.provision "shell", name: "minio", path: "barkend-infra/provision/install-minio.sh", env: {"PROVISION_HOME" => "/vagrant/barkend-infra/provision"}

  config.vm.post_up_message = <<-MESSAGE
    All done! Happy no barking...
  MESSAGE

end