package com.lsadf;

import com.lsadf.configurations.LsadfConfiguration;
import com.lsadf.core.utils.ApplicationUtils;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/** Main class for the application */
@SpringBootApplication
@Import(LsadfConfiguration.class)
@Slf4j
public class LsadfApplication {

  public static void main(String[] args) throws UnknownHostException {
    SpringApplication application = new SpringApplication(LsadfApplication.class);
    ConfigurableApplicationContext context = application.run(args);
    ApplicationUtils.printAccessUrl(context, log);
  }
}
