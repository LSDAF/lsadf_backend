package com.lsadf.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RedisProperties {
  private boolean enabled;
  private String host;
  private int port;
  private int database;
  private String password;
  private boolean embedded;
}
