package com.barkend.detector.client;

import com.barkend.detector.client.api.ExternalAmbassadorApiApi;
import com.barkend.detector.client.model.DefaultData;
import com.barkend.detector.client.model.SeldonMessage;
import com.barkend.detector.model.SoundClip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class SeldonClient {

	private final ExternalAmbassadorApiApi api;

	public SeldonMessage predict(SoundClip soundClip) {

		Assert.notNull(soundClip, "Sound clip must not be null");
		Assert.notNull(soundClip.getContent(), "Sound clip content must not be null");

		SeldonMessage message = new SeldonMessage();
		message.setBinData(soundClip.getContent());

		DefaultData data = new DefaultData();
		message.setData(data);

		return api.predict("A", "B", message);
	}

}
