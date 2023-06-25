package me.axerr.pingwheel;

import lombok.Getter;
import me.axerr.pingwheel.ping.PingListener;
import me.axerr.pingwheel.ratelimit.RateLimiter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

public final class PingWheel extends JavaPlugin {
    @Getter
    private static PingWheel plugin;

    @Getter
    private static RateLimiter rateLimiter;

    @Override
    public void onEnable() {
        plugin = this;
        rateLimiter = new RateLimiter();

        if (!isPaper())
            return;

        Config.loadConfig(plugin);

        Messenger messenger = getServer().getMessenger();
        messenger.registerIncomingPluginChannel(this, "ping-wheel-c2s:ping-location", new PingListener());
        messenger.registerOutgoingPluginChannel(this, "ping-wheel-s2c:ping-location");
    }

    @Override
    public void onDisable() {
        Messenger messenger = getServer().getMessenger();
        messenger.unregisterIncomingPluginChannel(this, "ping-wheel-c2s:ping-location");
        messenger.unregisterOutgoingPluginChannel(this, "ping-wheel-s2c:ping-location");
    }

    private boolean isPaper() {
        if (hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration"))
            return true;
        getLogger().severe("=====");
        getLogger().severe(" PingWheelBukkit requires PaperMC.io as your server software.");
        getLogger().severe("=====");
        getServer().getPluginManager().disablePlugin(plugin);
        return false;
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
