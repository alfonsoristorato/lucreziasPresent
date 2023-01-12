# Server creation

### Create a docker image

- ensure you have your .env file filled and in resources folder
- create image from dockerfile:

1. docker build -t alfonsoristorato/lucrezia:latest .

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

1. nginx config for location:
   proxy_pass http://localhost:8080;
   proxy_http_version 1.1;
   proxy_set_header Upgrade $http_upgrade;
   proxy_set_header Connection keep-alive;
   proxy_set_header Host $host;
   proxy_cache_bypass $http_upgrade;
   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
   proxy_set_header X-Forwarded-Proto $scheme;
2. nginx config for server (add only, not replace all):
   client_max_body_size 10M;

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
  GRANT ALL PRIVILEGES ON lucreziasPresent.\* TO 'username'@'dockerContainerIpAddress';
  FLUSH PRIVILEGES;
- allow fw access to port 3306 from dockerContainerIpAddress;

### Install automysqlbackup to backup your db

- sudo apt-get install automysqlbackup
- command to restore backup:

1.  from unzipped: mysql -u root -p tableName < backup.sql
2.  from zipped: gunzip < database_2016-05-20_13h31m.Friday.sql.gz | mysql -u root -p databasename

### Allow remote access to MySql (needed for docker to be able to connect)

https://www.digitalocean.com/community/tutorials/how-to-allow-remote-access-to-mysql

- allow docker port and bind address to docker0

### Transfer your docker-compose.yml to your droplet

- without having to install git, you can use scp to transfer files:

1. scp docker-compose.yml root@dropletip:/home/alfonso/lucrezia

### Start the application

- docker login
- docker pull alfonsoristorato/lucrezia:latest
- docker logout
- docker-compose up (add -d if undetached)
