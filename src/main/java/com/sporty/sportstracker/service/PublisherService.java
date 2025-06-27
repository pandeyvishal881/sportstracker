package com.sporty.sportstracker.service;

import static com.sporty.sportstracker.constants.ApplicationConstants.*;

import com.sporty.sportstracker.model.response.EventScoreResponse;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublisherService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  private final String topic = LIVE_SPORTS_EVENTS;

  public void publishEventScore(EventScoreResponse eventScore) {
    CompletableFuture<SendResult<String, Object>> future =
        kafkaTemplate.send(topic, eventScore.getEventId(), eventScore);

    future
        .thenAccept(
            result -> log.info(PUBLISHED_EVENT_TO_KAFKA_TOPIC, eventScore.getEventId(), topic))
        .exceptionally(
            ex -> {
              log.error(FAILED_TO_PUBLISH_EVENT, eventScore.getEventId(), ex.getMessage());
              return null;
            });
  }
}
