# Server creation

### Create a docker image
- ensure you have your .env file filled and in resources folder
- create image from dockerfile:
1. docker build -t alfonsoristorato/lucrezia .
- push it to dockerhub:
1. docker login 
2. docker push alfonsoristorato/lucrezia:latest
3. docker logout (if needed)

### Create a droplet on Digital Ocean
Ubuntu version used 20.04 LTS

### Buy a domain and link it to your droplet (I used namecheap)
https://docs.digitalocean.com/products/networking/dns/
- remember to link the www version too

### Initial Server Setup with Ubuntu 20.04
- login as root --> ssh user@ipaddress
- create user with sudo permissions
- login as user
  https://www.digitalocean.com/community/tutorials/initial-server-setup-with-ubuntu-20-04

### Install nginx
  https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-20-04#step-5-%E2%80%93-setting-up-server-blocks-(recommended)

### Secure Nginx with Let's Encrypt
  https://www.digitalocean.com/community/tutorials/how-to-secure-nginx-with-let-s-encrypt-on-ubuntu-20-04

### Install docker and dockerCompose
  https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-20-04
  https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04

### Install MySql on your droplet
  https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-20-04
- create the db
- as our springboot app will run in a docker container and connect to the remote MySql, we need to create a database and user like so:
  CREATE USER 'username'@'dockerContainerIpAddress' IDENTIFIED WITH authentication_plugin BY 'password';
  CREATE DATABASE lucreziasPresent;
  USE lucreziasPresent;
  CREATE USER 'user'@'dockerContainerIpAddress' IDENTIFIED WITH mysql_native_password BY 'password';
  GRANT ALL PRIVILEGES ON lucreziasPresent.* TO 'username'@'dockerContainerIpAddress';
  FLUSH PRIVILEGES;

### Allow remote access to MySql (needed for docker to be able to connect)
  https://www.digitalocean.com/community/tutorials/how-to-allow-remote-access-to-mysql
- just allow the port of your docker, do not change mysqld.cnf bind-address

### Transfer your docker-compose.yml to your droplet
- without having to install git, you can use scp to transfer files:
1. scp docker-compose.yml root@dropletip:/home/alfonso/lucrezia

### Start the application
- docker login
- docker pull alfonsoristorato/lucrezia
- docker logout
- docker-compose up (add -d if undetached)
