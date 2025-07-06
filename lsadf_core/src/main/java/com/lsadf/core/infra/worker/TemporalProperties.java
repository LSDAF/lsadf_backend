package com.lsadf.core.infra.worker;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemporalProperties {
  private String serverAddress;
  private String namespace;

  // Task queues
  private String gameSessionTaskQueue;
}
