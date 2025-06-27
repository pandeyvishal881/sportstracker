package com.sporty.sportstracker.controller;

import static com.sporty.sportstracker.constants.ApplicationConstants.EVENT_STATUS_UPDATED;

import com.sporty.sportstracker.model.request.EventStatusRequest;
import com.sporty.sportstracker.model.response.EventStatusResponse;
import com.sporty.sportstracker.service.LiveEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventStatusController {

  private final LiveEventService liveEventService;

  @PostMapping(
      value = "/status",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EventStatusResponse> updateEventStatus(
      @Valid @RequestBody EventStatusRequest request) {

    liveEventService.updateEventStatus(request.getEventId(), request.getStatus());
    EventStatusResponse response =
        EventStatusResponse.builder().message(EVENT_STATUS_UPDATED).build();
    return ResponseEntity.ok(response);
  }
}
