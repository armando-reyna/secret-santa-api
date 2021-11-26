#!/bin/bash

container_version=$(cat container.version)
container_version=`expr $container_version + 1`
container_name="$1-c${container_version}"
echo "starting $container_name..."
echo "docker run --name $container_name --label $1 --restart always -d -p $2:$3 -e SPRING_PROFILES_ACTIVE=$4 -e TZ=$5 $8 $6$1:$7"
docker run --name $container_name -d -p $2:$3 -e SPRING_PROFILES_ACTIVE=$4 -e TZ=$5 $8 $6$1:$7
echo $container_version > container.version
