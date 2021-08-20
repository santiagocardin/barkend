package com.barkend.detector.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonAutoDetect
@AllArgsConstructor
@NoArgsConstructor
public class BarkingDogEvent {

	private String type;

}
