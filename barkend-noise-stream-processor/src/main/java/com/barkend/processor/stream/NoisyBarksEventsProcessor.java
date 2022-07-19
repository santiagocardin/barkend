package com.barkend.processor.stream;

import com.barkend.alarm.model.TooNoisyEvent;
import com.barkend.detector.model.BarkingDogEvent;
import com.barkend.processor.config.ApplicationConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NoisyBarksEventsProcessor {

  private final ApplicationConfig applicationConfig;

  public NoisyBarksEventsProcessor(ApplicationConfig applicationConfig) {
    this.applicationConfig = applicationConfig;
  }

  @Bean
  public Function<KStream<String, BarkingDogEvent>, KStream<String, TooNoisyEvent>> process() {

    return event ->
        event
            .groupByKey()
            .windowedBy(SessionWindows.with(applicationConfig.getTimeWindow()))
            .count()
            .toStream()
            .peek(
                (key, value) ->
                    log.info(
                        "Dogs barked {} times during the last {}",
                        value,
                        applicationConfig.getTimeWindow()))
            .flatMap(
                (key, value) -> {
                  List<KeyValue<String, TooNoisyEvent>> result = new ArrayList<>();
                  if (value != null && value > applicationConfig.getMaxBarksAllowed()) {
                    log.info(
                        "Barking limit of {} times per {} exceeded. Creating new TOO_NOISY"
                            + " event...",
                        applicationConfig.getMaxBarksAllowed(),
                        applicationConfig.getTimeWindow());
                    result.add(
                        new KeyValue<>("alarm", TooNoisyEvent.builder().reason("F* dogs").build()));
                  }
                  return result;
                });
  }
}
