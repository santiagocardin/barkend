package com.barkend.detector.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Prediction {

  private AudioCategory category;

  private Double score;
}
