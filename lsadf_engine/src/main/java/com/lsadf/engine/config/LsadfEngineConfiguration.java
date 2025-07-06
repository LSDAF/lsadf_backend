package com.lsadf.engine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TemporalWorkerConfiguration.class)
public class LsadfEngineConfiguration {}
