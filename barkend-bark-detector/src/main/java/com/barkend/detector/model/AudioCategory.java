package com.barkend.detector.model;

public enum AudioCategory {

  // @formatter:off
  BIRDS(AudioCategory.BIRDS_),
  ONE_DOG_BARKING(AudioCategory.DOG_),
  MANY_DOGS_BARKING(AudioCategory.DOGS_),
  SIREN(AudioCategory.SIREN_),
  MOTOR_ENGINE(AudioCategory.ENGINE_),
  PEOPLE(AudioCategory.PEOPLE_),
  CHAINSAW(AudioCategory.CHAINSAW_);
  // @formatter:on

  public static final String BIRDS_ = "birds_chirping";

  public static final String DOG_ = "dog_barking";

  public static final String DOGS_ = "many_dogs_barking";

  public static final String SIREN_ = "siren";

  public static final String ENGINE_ = "engine";

  public static final String PEOPLE_ = "people_talking";

  public static final String CHAINSAW_ = "chainsaw";

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private String name;

  AudioCategory(String name) {
    this.name = name;
  }

  public static AudioCategory valueOfLabel(String name) {
    for (AudioCategory b : AudioCategory.values()) {
      if (b.name.equalsIgnoreCase(name)) {
        return b;
      }
    }
    return null;
  }
}
