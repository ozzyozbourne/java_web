package org.acme;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ByeService {

    @ConfigProperty(name = "Bye")
    String bye;

    @PostConstruct
    void init() {
        Log.info("Initialized!");
    }

    public String createByeMessage(String name) {
        return "%s %s".formatted(bye, name);
    }
}
