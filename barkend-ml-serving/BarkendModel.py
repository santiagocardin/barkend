import json

from keras.models import load_model
from keras.utils import to_categorical
from seldon_core.proto.prediction_pb2 import SeldonMessage
from sklearn.preprocessing import LabelEncoder

import os
import librosa
import numpy as np
import tempfile
import logging
import base64

log = logging.getLogger()

# Label list
class_lables = [
    "birds_chirping",
    "chainsaw",
    "dog_barking",
    "engine",
    "many_dogs_barking",
    "people_talking",
    "siren"
]

def extract_feature(audio_data, sample_rate):
    try:
        mfccs = librosa.feature.mfcc(y=audio_data, sr=sample_rate, n_mfcc=40)
        mfccsscaled = np.mean(mfccs.T, axis=0)

    except Exception as e:
        log.error('Error encountered while parsing audio data')
        return None, None

    return np.array([mfccsscaled])

def return_prediction(model, le, audio_data, sample_rate):

    prediction_feature = extract_feature(audio_data, sample_rate)
    predicted_vector = model.predict_classes(prediction_feature)
    predicted_class = le.inverse_transform(predicted_vector)
    predicted_proba_vector = model.predict(prediction_feature)
    predicted_proba = predicted_proba_vector[0]
    prediction = {
        'class': predicted_class[0],
        'probabilities': {
            'birds_chirping': predicted_proba[0],
            'chainsaw': predicted_proba[1],
            'dog_barking': predicted_proba[2],
            'engine': predicted_proba[3],
            'many_dogs_barking': predicted_proba[4],
            'people_talking': predicted_proba[5],
            'siren': predicted_proba[6]
        }
    }
    return prediction

class BarkendModel(object):

    model = None
    le = None

    def get_wav_data(self, audio: str):

        log.debug("Loading audio file...")

        raw = base64.b64decode(audio)

        f = open("/tmp/file.wav", "wb")
        f.write(raw)
        f.close()

        tmp = tempfile.NamedTemporaryFile()
        with open(tmp.name, 'wb') as f:

            try:
                audio_data, sample_rate = librosa.load('/tmp/file.wav', res_type='kaiser_fast')
                os.remove("/tmp/file.wav")
            except Exception as e:
                log.error('ERROR: WAV file sampling failed')
                log.error(e)
                raise e
            return audio_data, sample_rate

    def load(self):
        log.info("Loading model")

        self.model = load_model('./model/barkend.hdf5')

        # Encode the classification labels
        self.le = LabelEncoder()
        y = np.array(class_lables)
        yy = to_categorical(self.le.fit_transform(y))

        log.info("Model successfully loaded")

    def predict_raw(self, msg: SeldonMessage):

        log.info("Predicting audio clip...")

        audio_data, sample_rate = self.get_wav_data(msg["binData"])
        prediction = return_prediction(self.model, self.le, audio_data, sample_rate)

        for i in range(len(prediction['probabilities'])):
            category = self.le.inverse_transform(np.array([i]))
            log.info(category[0] + " : " + str(prediction['probabilities'][category[0]]))

        #FIXME !!!!
        response = '{"data": {"names": ["birds_chirping","chainsaw","dog_barking","engine","many_dogs_barking","people_talking","siren"], ' \
                   '"ndarray": [' \
                                ''+str(prediction['probabilities']['birds_chirping'])+',' \
                                ''+str(prediction['probabilities']['chainsaw'])+',' \
                                ''+str(prediction['probabilities']['dog_barking'])+',' \
                                ''+str(prediction['probabilities']['engine'])+',' \
                                ''+str(prediction['probabilities']['many_dogs_barking'])+',' \
                                ''+str(prediction['probabilities']['people_talking'])+',' \
                                ''+str(prediction['probabilities']['siren'])+'' \
                   ']}' \
                   ', "meta": {}}'

        return json.loads(response)