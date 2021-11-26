#!/bin/bash

docker container prune --filter label=$1 --force
docker image prune -a --filter label=$1 --force
