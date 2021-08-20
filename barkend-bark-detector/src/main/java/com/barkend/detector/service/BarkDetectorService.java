package com.barkend.detector.service;

import com.barkend.detector.client.SeldonClient;
import com.barkend.detector.client.model.SeldonMessage;
import com.barkend.detector.config.ApplicationConfig;
import com.barkend.detector.model.AudioCategory;
import com.barkend.detector.model.Prediction;
import com.barkend.detector.model.SoundClip;
import com.barkend.detector.repository.S3Repository;
import com.barkend.detector.service.handler.SoundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BarkDetectorService {

	private final S3Repository s3Repository;

	private final SeldonClient seldonClient;

	private final Map<String, SoundHandler> commandHandlersMap;

	private final ApplicationConfig applicationConfig;

	public void processClip(final String clipName) {

		log.debug("Processing new audio file {} ...", clipName);

		try {

			SoundClip clip = s3Repository.downloadFile(clipName);
			log.debug("File {} successfully downloaded", clip.getName());

			if (clip.getTags().isEmpty()) {

				// Call ML engine
				SeldonMessage response = seldonClient.predict(clip);
				loadPredictions(clip, response);

				// Handle sound
				Prediction prediction = clip.getMostProbablePrediction().get();
				if (prediction.getScore() >= applicationConfig.getPredictionMinScore()) {
					commandHandlersMap.get(prediction.getCategory().getName()).handle(clip);
				}
				else {
					s3Repository.deleteFile(clip.getName());
				}
			}
			else {
				log.debug("Ignored tagged clip {}", clipName);
			}
		}
		catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
	}

	private void loadPredictions(SoundClip clip, SeldonMessage response) {

		assert response.getData() != null;

		List<String> names = response.getData().getNames();
		for (int i = 0; i < names.size(); i++) {

			String name = names.get(i);

			Prediction p = null;
			if (response.getData().getNdarray() != null) {

				Double pValue = (Double) response.getData().getNdarray().get(i);

				p = Prediction.builder().category(AudioCategory.valueOfLabel(name)).score(pValue).build();
			}

			clip.addPrediction(p);
		}
	}

}