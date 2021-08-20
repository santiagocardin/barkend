#!/usr/bin/env bash

AUDIO_PATH="./model/barkend/audio"
METADATA_PATH="./model/barkend/metadata"

echo "slice_file_name,fold,class" > $METADATA_PATH/barkend.csv
find $AUDIO_PATH/1-dog/*.wav -printf '%f,1-dog,dog_barking\n' >> $METADATA_PATH/barkend.csv
find $AUDIO_PATH/birds/*.wav -printf '%f,birds,birds_chirping\n' >> $METADATA_PATH/barkend.csv
find $AUDIO_PATH/many-dogs/*.wav -printf '%f,many-dogs,many_dogs_barking\n' >> $METADATA_PATH/barkend.csv
find $AUDIO_PATH/people/*.wav -printf '%f,people,people_talking\n' >> $METADATA_PATH/barkend.csv
find $AUDIO_PATH/siren/*.wav -printf '%f,siren,siren\n' >> $METADATA_PATH/barkend.csv
find $AUDIO_PATH/engine/*.wav -printf '%f,engine,engine\n' >> $METADATA_PATH/barkend.csv
find $AUDIO_PATH/chainsaw/*.wav -printf '%f,chainsaw,chainsaw\n' >> $METADATA_PATH/barkend.csv

. conda activate dog-bark
time jupyter nbconvert --to notebook --execute barkend.ipynb