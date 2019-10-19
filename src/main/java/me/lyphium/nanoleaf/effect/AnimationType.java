package me.lyphium.nanoleaf.effect;

import lombok.Getter;

import java.util.UUID;

public enum AnimationType {

    EXPLODE("713518c1-d560-47db-8991-de780af71d1e"),
    FLOW("027842e4-e1d6-4a4c-a731-be74a1ebd4cf"),
    FADE("b3fd723a-aae8-4c99-bf2b-087159e0ef53"),
    HIGHLIGHT("70b7c636-6bf8-491f-89c1-f4103508d642"),
    RANDOM("ba632d3e-9c2b-4413-a965-510c839b3f71"),
    WHEEL("6970681a-20b5-4c5e-8813-bdaebc4ee4fa"),
    STATIC,
    SOLID,
    PLUGIN,
    CUSTOM;

    @Getter
    private final UUID uuid;

    AnimationType() {
        uuid = null;
    }

    AnimationType(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}