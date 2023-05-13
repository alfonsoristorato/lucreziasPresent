#!/bin/bash
DETACHED=${1}
if [ "$1" ]
then
  docker-compose -f docker-compose.functional.yml up --force-recreate -V "$DETACHED"
else
  docker-compose -f docker-compose.functional.yml up --force-recreate -V
fi