package com.lsadf.engine;

import com.lsadf.engine.config.LsadfEngineConfiguration;
import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(LsadfEngineConfiguration.class)
@Slf4j
@SpringBootApplication
public class LsadfEngineApplication {
  public static void main(String[] args) {
    SpringApplication.run(LsadfEngineApplication.class, args);
  }

  @Bean
  public CommandLineRunner startWorkers(WorkerFactory workerFactory) {
    return args -> {
      // Start all workers
      workerFactory.start();
      log.info("Temporal workers started successfully");
    };
  }
}
