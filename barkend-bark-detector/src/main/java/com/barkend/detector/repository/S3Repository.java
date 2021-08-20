package com.barkend.detector.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.barkend.detector.model.SoundClip;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Repository
public class S3Repository {

	private static final String bucketName = "audio";

	private AmazonS3 amazonS3;

	public S3Repository(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	public SoundClip downloadFile(String key) throws IOException {

		S3Object s3object = amazonS3.getObject(bucketName, key);

		SoundClip clip = new SoundClip();
		clip.setName(key);
		clip.setContent(IOUtils.toByteArray(s3object.getObjectContent()));

		if (s3object.getTaggingCount() != null && s3object.getTaggingCount() > 0) {
			clip.setTags(getTags(key));
		}

		return clip;
	}

	private HashMap<String, String> getTags(String key) {

		HashMap<String, String> tags = new HashMap<>();

		GetObjectTaggingRequest request = new GetObjectTaggingRequest(bucketName, key);
		GetObjectTaggingResult tagresult = amazonS3.getObjectTagging(request);
		tagresult.getTagSet().forEach(tag -> {
			tags.put(tag.getKey(), tag.getValue());
		});
		return tags;
	}

	public void deleteFile(String key) {
		amazonS3.deleteObject(bucketName, key);
	}

	public void addTagToFile(String key, List<Tag> tags) {
		ObjectTagging tagging = new ObjectTagging(tags);
		SetObjectTaggingRequest request = new SetObjectTaggingRequest(bucketName, key, tagging);
		amazonS3.setObjectTagging(request);
	}

}
