# Barkend :: barkend-sound-capture

Audio recording component for the Barkend project. 

Continuously records audio from a USB microphone in .WAV chunks and uploads them to a S3 compatible bucket.

## Recording tools

    pi@raspberrypi:~ $ arecord -l
    **** List of CAPTURE Hardware Devices ****
    card 1: K38 [K38], device 0: USB Audio [USB Audio]
    Subdevices: 0/1
    Subdevice #0: subdevice #0

    pi@raspberrypi: $ amixer -c 1
    Simple mixer control 'Mic',0
    Capabilities: cvolume cvolume-joined cswitch cswitch-joined
    Capture channels: Mono
    Limits: Capture 0 - 100
    Mono: Capture 100 [100%] [-2.00dB] [on]


## Delete objects

    mc rm --insecure --recursive --force --dangerous  --older-than 1h local/audio/

## Useful links

https://pimylifeup.com/raspberrypi-microphone/

https://trac.ffmpeg.org/wiki/Capture/ALSA

https://trac.ffmpeg.org/wiki/AudioVolume