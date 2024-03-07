package io.rizvan.beans.facts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;

@ApplicationScoped
public class FactDeserializer {

    @Inject
    Jsonb jsonb;

    public Fact deserialize(String json) {
        FactType factType = jsonb.fromJson(json, FactType.class);

        return switch (factType.getType()) {
            case "shoot" -> jsonb.fromJson(json, PlayerShootsFact.class);
            case "move" -> jsonb.fromJson(json, PlayerMovesFact.class);
            default -> throw new IllegalArgumentException("Unknown Fact type: " + factType.getType());
        };
    }
}
