#!/bin/bash

container_version=$(cat container.version)
container_name="$1-c${container_version}"
sh -c 'docker logs -f --since 0s "'"$container_name"'" | { sed "/Tomcat started on port/ q; /Application startup failed/ q; /APPLICATION FAILED TO START/ q" && kill $$ ;}'

health=$(curl $2)
if [[ $health == *"UP"* ]]; then
  echo "Service is UP"
  exit 0
else
  echo "Start service failed" >&2
  exit 1
fi
