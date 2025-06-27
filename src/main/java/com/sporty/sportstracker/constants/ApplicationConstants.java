package com.sporty.sportstracker.constants;

public class ApplicationConstants {
  public static final String LIVE_SPORTS_EVENTS = "live-sports-events";
  public static final String PUBLISHED_EVENT_TO_KAFKA_TOPIC =
      "Published event {} to Kafka topic {}";
  public static final String FAILED_TO_PUBLISH_EVENT = "Failed to publish event {}: {}";
  public static final String EVENT_MARKED_AS_LIVE = "Event {} marked as LIVE";
  public static final String EVENT_MARKED_AS_NOT_LIVE = "Event {} marked as NOT LIVE";
  public static final String EVENT_IS_ALREADY_LIVE_AND_SCHEDULED =
      "Event {} is already live and scheduled";
  public static final String STARTING_LIVE_SCHEDULE_FOR_EVENT =
      "Starting live schedule for event {}";
  public static final String NO_RESPONSE_RECEIVED_FOR_EVENT = "No response received for event {}";
  public static final String ERROR_WHILE_FETCHING_OR_PUBLISHING_SCORE_FOR_EVENT =
      "Error while fetching or publishing score for event {}: {}";
  public static final String STOPPED_LIVE_SCHEDULE_FOR_EVENT = "Stopped live schedule for event {}";
  public static final String NO_SCHEDULED_TASK_FOUND_FOR_EVENT =
      "No scheduled task found for event {}";
  public static final String FETCHED_SCORE_FOR = "Fetched score for {}: {}";
  public static final String ERROR_POLLING_API_FOR_EVENT = "Error polling API for event {}: {}";
  public static final String PREFIX_LIVE_EVENT_SCHEDULER = "live-event-scheduler-";
  public static final String EVENT_STATUS_UPDATED = "Event status updated";
}
