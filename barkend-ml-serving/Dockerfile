FROM tensorflow/tensorflow:2.2.3

RUN apt-get update && apt-get install ffmpeg libsndfile1 -y
RUN python3 -m pip install --upgrade pip

COPY requirements.txt /app/requirements.txt
WORKDIR /app

RUN pip3 install -r requirements.txt

COPY . /app

# Port for GRPC
EXPOSE 5000
# Port for REST
EXPOSE 9000

# Define environment variable
ENV MODEL_NAME BarkendModel
ENV SERVICE_TYPE MODEL

CMD exec seldon-core-microservice $MODEL_NAME --service-type $SERVICE_TYPE