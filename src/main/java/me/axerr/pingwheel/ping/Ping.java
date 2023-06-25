package me.axerr.pingwheel.ping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Ping {
    private Player player;
    private String channel;
    private double x, y, z;
    private boolean isEntity;
    private UUID entityUUID;
}