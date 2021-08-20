package com.barkend.detector.repository;

import com.amazonaws.services.s3.model.Tag;
import com.barkend.detector.model.SoundClip;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class S3RepositoryIT {

	@Autowired
	S3Repository s3Repository;

	@Test
	@Disabled
	public void downloadFile() throws IOException {
		SoundClip clip = s3Repository.downloadFile("clip_3599.wav");
		assertNotNull(clip.getContent());
		assertNotNull(clip.getName());
	}

	@Test
	public void addTagToFile() {
		s3Repository.addTagToFile("clip30.wav", List.of(new Tag("type", "barking")));
	}

}