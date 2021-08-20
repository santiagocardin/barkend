package com.barkend.detector.service.handler;

import com.amazonaws.services.s3.model.Tag;
import com.barkend.detector.model.AudioCategory;
import com.barkend.detector.model.Prediction;
import com.barkend.detector.model.SoundClip;
import com.barkend.detector.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(AudioCategory.ENGINE_)
@Slf4j
@RequiredArgsConstructor
public class EngineHandler implements SoundHandler {

	private final S3Repository s3Repository;

	@Override
	public void handle(SoundClip clip) {

		Prediction prediction = clip.getMostProbablePrediction().get();

		log.debug("Clip {} contains engine. Deleting...", clip.getName());

		s3Repository.deleteFile(clip.getName());
	}

}
