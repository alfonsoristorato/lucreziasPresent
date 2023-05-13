#!/bin/bash

FLAGS=${1}

export TEST_USERS_PASSWORD=${2:-'YOURPASSWORD'}

if [ "$1" ]
then
  docker-compose -f docker-compose.functional.yml up --force-recreate -V "$FLAGS"
else
  docker-compose -f docker-compose.functional.yml up --force-recreate -V
fi