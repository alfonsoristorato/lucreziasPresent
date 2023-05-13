#!/bin/bash

export TEST_USERS_PASSWORD=${1:-'YOURPASSWORD'}

docker-compose -f docker-compose.local.yml up