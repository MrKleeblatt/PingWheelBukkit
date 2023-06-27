package me.axerr.pingwheel.ratelimit;

import me.axerr.pingwheel.Config;
import me.axerr.pingwheel.PingWheel;
import org.bukkit.entity.Player;

import java.util.*;

public class RateLimiter {
    private final Map<UUID, List<Long>> pings;

    public RateLimiter() {
        this.pings = new HashMap<>();

        PingWheel.getPlugin()
                .getServer()
                .getScheduler()
                .scheduleSyncRepeatingTask(PingWheel.getPlugin(), this::removeUnusedEntries, 200, 200);
    }

    public void removeUnusedEntries() {
        pings.forEach((key, value) -> value.removeIf(list -> list + Config.RATE_LIMIT_TIME_WINDOW < System.currentTimeMillis()));
        pings.entrySet().removeIf(entry -> entry.getValue().size() == 0);
    }

    public boolean canPing(Player player) {
        if (!Config.RATE_LIMIT_ENABLED)
            return true;

        List<Long> pingTimes = pings.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        pingTimes.removeIf(entry -> entry + Config.RATE_LIMIT_TIME_WINDOW < System.currentTimeMillis());

        int pings = pingTimes.size();
        if (pings >= Config.RATE_LIMIT_PING_LIMIT)
            return false;

        pingTimes.add(System.currentTimeMillis());

        return true;
    }

    public boolean checkBypass(Player player) {
        return Config.RATE_LIMIT_BYPASS_ENABLED && player.hasPermission(Config.RATE_LIMIT_BYPASS_PERMISSION);
    }
}
