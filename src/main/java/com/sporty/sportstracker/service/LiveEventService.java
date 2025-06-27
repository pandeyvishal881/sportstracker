package com.sporty.sportstracker.service;

import static com.sporty.sportstracker.constants.ApplicationConstants.EVENT_MARKED_AS_LIVE;
import static com.sporty.sportstracker.constants.ApplicationConstants.EVENT_MARKED_AS_NOT_LIVE;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LiveEventService {

  private final LiveEventScheduler liveEventScheduler;
  private final Map<String, Boolean> eventStatusMap = new ConcurrentHashMap<>();

  public LiveEventService(LiveEventScheduler liveEventScheduler) {
    this.liveEventScheduler = liveEventScheduler;
  }

  public void updateEventStatus(String eventId, Boolean isLive) {
    if (isLive) {
      eventStatusMap.put(eventId, true);
      log.info(EVENT_MARKED_AS_LIVE, eventId);
      liveEventScheduler.startLiveEvent(eventId);
    } else {
      eventStatusMap.remove(eventId);
      log.info(EVENT_MARKED_AS_NOT_LIVE, eventId);
      liveEventScheduler.stopLiveEvent(eventId);
    }
  }

  public Map<String, Boolean> getLiveEvents() {
    return eventStatusMap;
  }
}
