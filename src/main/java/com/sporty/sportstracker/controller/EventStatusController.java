package com.sporty.sportstracker.controller;

import static com.sporty.sportstracker.constants.ApplicationConstants.EVENT_STATUS_UPDATED;

import com.sporty.sportstracker.model.EventStatusRequest;
import com.sporty.sportstracker.service.LiveEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventStatusController {

  private final LiveEventService liveEventService;

  @PostMapping("/status")
  public ResponseEntity<String> updateEventStatus(@Valid @RequestBody EventStatusRequest request) {
    System.out.println(request);
    liveEventService.updateEventStatus(request.getEventId(), request.getStatus());
    return ResponseEntity.ok(EVENT_STATUS_UPDATED);
  }
}
