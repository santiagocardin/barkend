package com.barkend.detector.service.handler;

import com.amazonaws.services.s3.model.Tag;
import com.barkend.detector.listener.BarkingDogEventProducer;
import com.barkend.detector.model.AudioCategory;
import com.barkend.detector.model.BarkingDogEvent;
import com.barkend.detector.model.Prediction;
import com.barkend.detector.model.SoundClip;
import com.barkend.detector.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service(AudioCategory.DOG_)
public class OneDogBarkingHandler implements SoundHandler {

	private final BarkingDogEventProducer barkingDogEventProducer;

	private final S3Repository s3Repository;

	@Override
	public void handle(SoundClip clip) {

		Prediction prediction = clip.getMostProbablePrediction().get();

		log.info("Clip {} contains one dog barking ({}). Creating event...", clip.getName(), prediction.getScore());

		barkingDogEventProducer.sendBarkingDogEvent(BarkingDogEvent.builder().type("one-dog").build());

		s3Repository.addTagToFile(clip.getName(),
				List.of(new Tag("bark", "one-dog"), new Tag("prediction", prediction.getScore().toString())));

	}

}
