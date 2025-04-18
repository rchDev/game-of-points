package io.rizvan.beans.actors.player;

public enum PlayerMood {
    OPTIMISTIC,
    NEUTRAL,
    PESSIMISTIC;

    public static PlayerMood fromName(String name) {
        return switch (name.toLowerCase().strip()) {
            case "optimistic" -> OPTIMISTIC;
            case "neutral" -> NEUTRAL;
            case "pessimistic" -> PESSIMISTIC;
            default -> throw new IllegalArgumentException("Unknown player mood: " + name);
        };
    }
}
