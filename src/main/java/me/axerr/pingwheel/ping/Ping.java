package me.axerr.pingwheel.ping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.axerr.pingwheel.api.FriendlyByteBuf;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class Ping {
    private Player player;
    private String channel;
    private double x, y, z;
    private boolean isEntity;
    private UUID entityUUID;

    public Ping(Player player, FriendlyByteBuf data) {
        this.player = player;
        this.channel = data.readString();
        this.x = data.readDouble();
        this.y = data.readDouble();
        this.z = data.readDouble();
        this.isEntity = data.readBoolean();
        if (isEntity)
            this.entityUUID = data.readUUID();
    }
}