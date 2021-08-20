#!/bin/bash

echo 'Uploading files to S3 local/audio bucket...'
mc mv --insecure --older-than 5s \
  /opt/barkend/audio/*.wav \
  local/audio
