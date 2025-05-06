package com.lsadf.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakProperties {
  private String adminUrl;
  private String url;
  private String internalUrl;
  private String realm;
  private String clientId;
  private String clientSecret;
}
