# Server creation

### CI/CD

CI/CD is achieved via GitHub actions. 
- CI: Docker image is created and pushed to digital ocean registry.
- CD: runner ssh into droplet, logs in to the registry, stops running container, saves env vars into a file and fires up docker-compose

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

https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-20-04

1. nginx config for location:
   proxy_pass http://localhost:8080;
   proxy_http_version 1.1;
   proxy_set_header Upgrade $http_upgrade;
   proxy_set_header Connection keep-alive;
   proxy_set_header Host $host;
   proxy_cache_bypass $http_upgrade;
   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
   proxy_set_header X-Forwarded-Proto $scheme;
   deny all;
   allow xxx.xxx.xxx(ip address of your frontend);
2. nginx config for server (add only, not replace all):
   client_max_body_size 10M;
3. for default site, add in location:
   deny all;

### Secure Nginx with Let's Encrypt

https://www.digitalocean.com/community/tutorials/how-to-secure-nginx-with-let-s-encrypt-on-ubuntu-20-04

NB: Once the cert needs to be renewed, HTTP traffic needs to be allowed again via the DigitalOcean Firewall, via: https://cloud.digitalocean.com/networking/firewalls

### Install docker and dockerCompose

https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-20-04
https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04

### Install MySql on your droplet

https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-20-04

- create the db
- as our springboot app will run in a docker container and connect to the remote MySql, we need to create a database and user like so:
  CREATE DATABASE lucreziasPresent;
  USE lucreziasPresent;
  CREATE USER 'user'@'dockerContainerIpAddress(end it with rather than 1)' IDENTIFIED WITH mysql_native_password BY 'password';
  GRANT ALL PRIVILEGES ON lucreziasPresent.\* TO 'username'@'dockerContainerIpAddress';
  FLUSH PRIVILEGES;

### Install automysqlbackup to backup your db

- sudo apt-get install automysqlbackup
- configuration:

1. sudo nano /etc/default/automysqlbackup
2. change DBNAME to the dbName you want to backup, else it will backup all of them

- The backup files are located in /var/lib/automysqlbackup by default

- command to manually run the backup (it will do it daily anyway, just for testing)

1. sudo automysqlbackup

- command to restore backup:

1.  from unzipped: mysql -u root -p tableName < backup.sql
2.  from zipped: gunzip < database_2016-05-20_13h31m.Friday.sql.gz | mysql -u root -p databasename

### Allow remote access to MySql (needed for docker to be able to connect)

https://www.digitalocean.com/community/tutorials/how-to-allow-remote-access-to-mysql

- bind address to docker0 (and version ending with 2 too)
- allow docker port to 3306

### Transfer your docker-compose.yml to your droplet

- without having to install git, you can use scp to transfer files:

1. scp docker-compose.yml root@dropletip:/home/alfonso/lucrezia

### Start the application

- docker login
- docker pull alfonsoristorato/lucrezia:latest
- docker logout
- docker-compose up (add -d if undetached)
