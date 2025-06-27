package com.sporty.sportstracker.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventStatusResponse {
  private String message;
}
