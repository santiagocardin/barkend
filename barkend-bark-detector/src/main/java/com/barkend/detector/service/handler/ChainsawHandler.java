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

@Service(AudioCategory.CHAINSAW_)
@Slf4j
@RequiredArgsConstructor
public class ChainsawHandler implements SoundHandler {

	private final S3Repository s3Repository;

	@Override
	public void handle(SoundClip clip) {

		// We are not interested in this sound
		log.debug("Clip {} contains chainsaw. Deleting...", clip.getName());

		s3Repository.deleteFile(clip.getName());

	}

}
