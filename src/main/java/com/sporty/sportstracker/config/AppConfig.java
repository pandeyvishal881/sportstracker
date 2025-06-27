package com.sporty.sportstracker.config;

import static com.sporty.sportstracker.constants.ApplicationConstants.PREFIX_LIVE_EVENT_SCHEDULER;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(5);
    scheduler.setThreadNamePrefix(PREFIX_LIVE_EVENT_SCHEDULER);
    scheduler.initialize();
    return scheduler;
  }
}
