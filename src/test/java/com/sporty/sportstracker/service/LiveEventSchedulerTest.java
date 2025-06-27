package com.sporty.sportstracker.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import com.sporty.sportstracker.model.EventScoreResponse;
import java.util.concurrent.ScheduledFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.client.RestTemplate;

class LiveEventSchedulerTest {

  private LiveEventScheduler liveEventScheduler;

  private PublisherService publisherService;
  private RestTemplate restTemplate;
  private TaskScheduler taskScheduler;

  private final String eventId = "test-event";
  private final String baseUrl = "http://mock.api/url/";

  @BeforeEach
  void setUp() {
    publisherService = mock(PublisherService.class);
    restTemplate = mock(RestTemplate.class);
    taskScheduler = mock(TaskScheduler.class);
    liveEventScheduler =
        new LiveEventScheduler(publisherService, restTemplate, taskScheduler, baseUrl);
  }

  @Test
  void startLiveEvent_shouldScheduleAndPublish() {
    EventScoreResponse mockedResponse = new EventScoreResponse();
    mockedResponse.setEventId(eventId);
    mockedResponse.setCurrentScore("1:0");

    when(restTemplate.getForObject(baseUrl + eventId, EventScoreResponse.class))
        .thenReturn(mockedResponse);

    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);

    doReturn(scheduledFuture)
        .when(taskScheduler)
        .scheduleAtFixedRate(any(Runnable.class), eq(10_000L));

    liveEventScheduler.startLiveEvent(eventId);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(taskScheduler).scheduleAtFixedRate(runnableCaptor.capture(), eq(10_000L));

    runnableCaptor.getValue().run();

    verify(restTemplate).getForObject(baseUrl + eventId, EventScoreResponse.class);
    verify(publisherService).publishEventScore(mockedResponse);

    verifyNoMoreInteractions(publisherService, restTemplate, taskScheduler);
  }

  @Test
  void startLiveEvent_shouldNotRescheduleIfAlreadyRunning() {
    ScheduledFuture<?> mockFuture = mock(ScheduledFuture.class);
    liveEventScheduler.getScheduledTasks().put(eventId, mockFuture);

    liveEventScheduler.startLiveEvent(eventId);

    verifyNoInteractions(taskScheduler);
    verifyNoInteractions(restTemplate);
    verifyNoInteractions(publisherService);
  }

  @Test
  void stopLiveEvent_shouldCancelTaskAndRemoveFromMap() {
    ScheduledFuture<?> mockFuture = mock(ScheduledFuture.class);
    liveEventScheduler.getScheduledTasks().put(eventId, mockFuture);

    liveEventScheduler.stopLiveEvent(eventId);

    verify(mockFuture).cancel(false);
    assertFalse(liveEventScheduler.getScheduledTasks().containsKey(eventId));
  }
}
