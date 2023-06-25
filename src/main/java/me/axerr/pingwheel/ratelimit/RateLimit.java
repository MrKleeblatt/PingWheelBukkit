package me.axerr.pingwheel.ratelimit;

import java.util.HashMap;
import java.util.UUID;

public class RateLimit {
    public static HashMap<UUID, PlayerData> data = new HashMap<>();

    public static PlayerData getData(UUID uuid) {
        if (!data.containsKey(uuid))
            data.put(uuid, new PlayerData());
        return data.get(uuid);
    }
}