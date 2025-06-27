package com.sporty.sportstracker.service;

import static com.sporty.sportstracker.constants.ApplicationConstants.*;

import com.sporty.sportstracker.model.EventScoreResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class LiveEventScheduler {

  private final PublisherService publisherService;
  private final RestTemplate restTemplate;
  private final TaskScheduler taskScheduler;
  private final String externalApiUrl;

  @Getter private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

  @Autowired
  public LiveEventScheduler(
      PublisherService publisherService,
      RestTemplate restTemplate,
      TaskScheduler taskScheduler,
      @Value("${mock.api.url}") String externalApiUrl) {
    this.publisherService = publisherService;
    this.restTemplate = restTemplate;
    this.taskScheduler = taskScheduler;
    this.externalApiUrl = externalApiUrl;
  }

  @PostConstruct
  public void init() {
    if (this.taskScheduler instanceof ThreadPoolTaskScheduler threadPool) {
      threadPool.setPoolSize(5);
      threadPool.setThreadNamePrefix(PREFIX_LIVE_EVENT_SCHEDULER);
      threadPool.initialize();
    }
  }

  public void startLiveEvent(String eventId) {
    if (scheduledTasks.containsKey(eventId)) {
      log.info(EVENT_IS_ALREADY_LIVE_AND_SCHEDULED, eventId);
      return;
    }
    log.info(STARTING_LIVE_SCHEDULE_FOR_EVENT, eventId);

    ScheduledFuture<?> future =
        taskScheduler.scheduleAtFixedRate(
            () -> {
              try {
                EventScoreResponse response =
                    restTemplate.getForObject(externalApiUrl + eventId, EventScoreResponse.class);
                if (response != null) {
                  publisherService.publishEventScore(response);
                } else {
                  log.warn(NO_RESPONSE_RECEIVED_FOR_EVENT, eventId);
                }
              } catch (Exception e) {
                log.error(
                    ERROR_WHILE_FETCHING_OR_PUBLISHING_SCORE_FOR_EVENT, eventId, e.getMessage());
              }
            },
            10_000);

    scheduledTasks.put(eventId, future);
  }

  public void stopLiveEvent(String eventId) {
    ScheduledFuture<?> future = scheduledTasks.remove(eventId);
    if (future != null) {
      future.cancel(false);
      log.info(STOPPED_LIVE_SCHEDULE_FOR_EVENT, eventId);
    } else {
      log.info(NO_SCHEDULED_TASK_FOUND_FOR_EVENT, eventId);
    }
  }
}
