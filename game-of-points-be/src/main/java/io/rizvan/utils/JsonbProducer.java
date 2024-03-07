package io.rizvan.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class JsonbProducer {

    @Produces
    @ApplicationScoped
    public Jsonb createJsonb() {
        return JsonbBuilder.create();
    }
}