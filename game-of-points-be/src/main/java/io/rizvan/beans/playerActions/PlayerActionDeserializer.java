package io.rizvan.beans.playerActions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;

@ApplicationScoped
public class PlayerActionDeserializer {

    @Inject
    Jsonb jsonb;

    public PlayerAction deserialize(String json) {
        PlayerActionType actionType = jsonb.fromJson(json, PlayerActionType.class);

        return switch (actionType.getType()) {
            case "shoot" -> jsonb.fromJson(json, PlayerShootingAction.class);
            case "move" -> jsonb.fromJson(json, PlayerMovementAction.class);
            case "collect" -> jsonb.fromJson(json, PlayerCollectingAction.class);
            case "aim" -> jsonb.fromJson(json, PlayerAimingAction.class);
            default -> throw new IllegalArgumentException("Unknown Fact type: " + actionType.getType());
        };
    }
}
