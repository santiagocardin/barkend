package com.barkend.detector.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonAutoDetect
public class BarkingDogEvent {

	private String type;

}
