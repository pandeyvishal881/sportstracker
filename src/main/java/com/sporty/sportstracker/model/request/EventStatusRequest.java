package com.sporty.sportstracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventStatusRequest {
  @NotBlank(message = "Event ID must not be blank")
  private String eventId;

  @NotNull(message = "Status must not be null")
  private Boolean status;
}
