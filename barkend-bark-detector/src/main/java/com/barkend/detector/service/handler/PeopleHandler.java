package com.barkend.detector.service.handler;

import com.barkend.detector.model.AudioCategory;
import com.barkend.detector.model.SoundClip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(AudioCategory.PEOPLE_)
@Slf4j
public class PeopleHandler implements SoundHandler {

	@Override
	public void handle(SoundClip clip) {
		log.debug("Clip {} contains people. Doing nothing.", clip.getName());
	}

}
