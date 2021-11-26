#!/bin/bash

container_version=$(cat container.version)
container_name="$1-c${container_version}"
echo "stoping $container_name..."
docker stop $container_name || true
