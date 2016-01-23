#!/bin/sh
echo "Hello, World!"
./ngrok http 3000
node server.js