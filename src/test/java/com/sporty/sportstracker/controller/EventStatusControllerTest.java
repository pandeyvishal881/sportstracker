package com.sporty.sportstracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.sportstracker.controller.advice.GlobalExceptionHandler;
import com.sporty.sportstracker.model.EventStatusRequest;
import com.sporty.sportstracker.service.LiveEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EventStatusControllerTest {

  @Autowired private MockMvc mockMvc;

  @Mock private LiveEventService liveEventService;

  @Autowired private ObjectMapper objectMapper;

  private EventStatusController eventStatusController;

  @BeforeEach
  void setup() {
    eventStatusController = new EventStatusController(liveEventService);
    mockMvc =
        MockMvcBuilders.standaloneSetup(eventStatusController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  void updateEventStatus_shouldReturnOk() throws Exception {
    String json = "{\"eventId\":\"match-1\",\"status\":true}";

    mockMvc
        .perform(post("/events/status").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(content().string("Event status updated"));

    verify(liveEventService).updateEventStatus("match-1", true);
  }

  @Test
  void updateEventStatus_shouldReturnBadRequest_whenRequestInvalid() throws Exception {
    // Missing eventId and status
    String invalidRequestJson =
        """
        {
            "eventId": null,
            "status": null
        }
    """;
    mockMvc
        .perform(
            post("/events/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  void updateEventStatus_shouldReturnBadRequest_whenServiceThrowsIllegalArgument()
      throws Exception {
    EventStatusRequest request = new EventStatusRequest();
    request.setEventId("event1");
    request.setStatus(true);

    doThrow(new IllegalArgumentException("Invalid event status"))
        .when(liveEventService)
        .updateEventStatus(anyString(), any());

    mockMvc
        .perform(
            post("/events/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid event status"));
  }

  @Test
  void updateEventStatus_shouldReturnInternalServerError_whenServiceThrowsUnexpected()
      throws Exception {
    EventStatusRequest request = new EventStatusRequest();
    request.setEventId("event1");
    request.setStatus(true);

    doThrow(new RuntimeException("Something went wrong"))
        .when(liveEventService)
        .updateEventStatus(anyString(), any());

    mockMvc
        .perform(
            post("/events/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("An unexpected error occurred"));
  }
}
