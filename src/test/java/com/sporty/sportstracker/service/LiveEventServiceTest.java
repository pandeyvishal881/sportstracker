package com.sporty.sportstracker.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LiveEventServiceTest {

  @Mock private LiveEventScheduler scheduler;

  @InjectMocks private LiveEventService liveEventService;

  @Test
  void updateEventStatus_startsSchedulerWhenLive() {
    liveEventService.updateEventStatus("match-1", true);

    assertTrue(liveEventService.getLiveEvents().containsKey("match-1"));
    verify(scheduler).startLiveEvent("match-1");
  }

  @Test
  void updateEventStatus_stopsSchedulerWhenNotLive() {
    liveEventService.updateEventStatus("match-1", false);

    assertFalse(liveEventService.getLiveEvents().containsKey("match-1"));
    verify(scheduler).stopLiveEvent("match-1");
  }
}
