package com.sporty.sportstracker.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sporty.sportstracker.model.response.EventScoreResponse;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

  @Mock private KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks private PublisherService publisherService;

  @Test
  void publishEventScore_sendsMessage() {
    EventScoreResponse event = new EventScoreResponse();
    event.setEventId("match-1");
    event.setCurrentScore("1:0");

    when(kafkaTemplate.send(anyString(), anyString(), any()))
        .thenReturn(CompletableFuture.completedFuture(null));

    publisherService.publishEventScore(event);

    verify(kafkaTemplate).send(eq("live-sports-events"), eq("match-1"), eq(event));
  }
}
