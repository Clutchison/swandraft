package com.hutchison.swandraft.model.player;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerIdentifier {
    Long discordId;
    String name;
    Integer discriminator;
    String string;

    public PlayerIdentifier(@NonNull Long discordId,
                             @NonNull String name,
                             @NonNull Integer discriminator) {
        this.discordId = discordId;
        this.name = name;
        this.discriminator = discriminator;
        this.string = name + "#" + discriminator + "-" + discordId;
    }

    public static PlayerIdentifier fromPlayer(Player player) {
        return new PlayerIdentifier(player.getDiscordId(), player.getName(), player.getDiscriminator());
    }

    @Override
    public String toString() {
        return string;
    }

    private String getString() {
        return string;
    }
}
