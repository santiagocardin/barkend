package com.barkend.detector.model;

import lombok.Data;

import java.util.*;

@Data
public class SoundClip {

	private String name;

	private byte[] content;

	private Map<String, String> tags;

	private List<Prediction> predictions;

	public SoundClip() {
		tags = new HashMap<>();
		predictions = new ArrayList<>();
	}

	public Optional<Prediction> getMostProbablePrediction() {
		return predictions.stream().max(Comparator.comparing(Prediction::getScore));
	}

	public void addPrediction(Prediction p) {
		getPredictions().add(p);
	}

}
