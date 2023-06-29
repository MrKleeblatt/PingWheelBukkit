package me.axerr.pingwheel;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import lombok.Getter;
import me.axerr.pingwheel.hooks.WorldGuardHook;
import me.axerr.pingwheel.ping.PingListener;
import me.axerr.pingwheel.ratelimit.RateLimiter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

public final class PingWheel extends JavaPlugin {
    @Getter
    private static PingWheel plugin;

    @Getter
    private static RateLimiter rateLimiter;

    @Getter
    private static boolean modernMessages;

    @Override
    public void onEnable() {
        plugin = this;
        rateLimiter = new RateLimiter();
        modernMessages = isPaper();

        Config.loadConfig(plugin);

        Messenger messenger = getServer().getMessenger();
        messenger.registerIncomingPluginChannel(this, Constants.CLIENT_TO_SERVER_CHANNEL, new PingListener());
        messenger.registerOutgoingPluginChannel(this, Constants.SERVER_TO_CLIENT_CHANNEL);
    }

    @Override
    public void onLoad() {
        if (!isWorldGuardInstalled())
            return;
        WorldGuardHook.hook();
    }

    private boolean isWorldGuardInstalled() {
        return getServer().getPluginManager().getPlugin("WorldGuard") != null;
    }

    @Override
    public void onDisable() {
        Messenger messenger = getServer().getMessenger();
        messenger.unregisterIncomingPluginChannel(this, Constants.CLIENT_TO_SERVER_CHANNEL);
        messenger.unregisterOutgoingPluginChannel(this, Constants.SERVER_TO_CLIENT_CHANNEL);
    }

    private boolean isPaper() {
        return hasClass("com.destroystokyo.paper.PaperConfig") ||
                hasClass("io.papermc.paper.configuration.Configuration");
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
