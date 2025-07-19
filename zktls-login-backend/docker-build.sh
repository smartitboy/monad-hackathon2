#!/bin/bash
if [ -z "$1" ]; then
  DEFAULT_VALUE="latest" # set default
else
  DEFAULT_VALUE="$1" # use input
fi
gradle bootJar
docker build -t smartboy/zktls-login-backend:$DEFAULT_VALUE .
