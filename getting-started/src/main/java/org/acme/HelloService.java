package org.acme;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloService {

    @ConfigProperty(name = "hello")
    String hello;

    @PostConstruct
    void init() {
        Log.info("Initialized");
    }

    public String createHelloMessage(String name) {
        return "%s %s!".formatted(hello, name);
    }
}
