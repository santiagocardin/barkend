#!/bin/bash

mkdir -p /opt/barkend/audio

echo 'Recording audio...'
ffmpeg -f alsa -i default  \
    -ac 1 \
    -fflags +igndts \
    -ar 44100 \
    -c:a libmp3lame -b:a 64k \
    -filter:a "volume=15dB" \
    -f segment -segment_time 5 -reset_timestamps 1 \
    /opt/barkend/audio/clip_%d.wav
