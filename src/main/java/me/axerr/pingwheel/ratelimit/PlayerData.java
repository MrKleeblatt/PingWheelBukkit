package me.axerr.pingwheel.ratelimit;

import lombok.Getter;
import lombok.Setter;
import me.axerr.pingwheel.Config;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerData {
    private long lastPing;

    public PlayerData() {
        this.lastPing = 0;
    }

    public void ping() {
        this.lastPing = System.currentTimeMillis();
    }

    public boolean canPing(Player player) {
        boolean x = (Config.RATE_LIMIT_ENABLED && System.currentTimeMillis() - Config.RATE_LIMIT_TIME > lastPing) ||
                (Config.RATE_LIMIT_BYPASS_ENABLED && player.hasPermission(Config.RATE_LIMIT_BYPASS_PERMISSION));
        return x;
    }
}
