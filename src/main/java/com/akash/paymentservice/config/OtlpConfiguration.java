package com.akash.paymentservice.config;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtlpConfiguration {

    @Bean
    OtlpGrpcSpanExporter otelHttpSpanExporter(@Value("${tracingUrl") String tracingUrl) {
        return OtlpGrpcSpanExporter.builder()
                .setEndpoint(tracingUrl)
                .build();
    }

}
