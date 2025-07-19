#!/bin/bash
if [ -z "$1" ]; then
  DEFAULT_VALUE="latest" # set default
else
  DEFAULT_VALUE="$1" # use input
fi
npm install
npm run build
docker build -t smartboy/zktls-login-frontend:$DEFAULT_VALUE .
