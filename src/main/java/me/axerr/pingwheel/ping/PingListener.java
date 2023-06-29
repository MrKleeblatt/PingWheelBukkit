package me.axerr.pingwheel.ping;

import io.netty.buffer.Unpooled;
import me.axerr.pingwheel.Config;
import me.axerr.pingwheel.Constants;
import me.axerr.pingwheel.PingWheel;
import me.axerr.pingwheel.api.FriendlyByteBuf;
import me.axerr.pingwheel.hooks.WorldGuardHook;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PingListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.copiedBuffer(message));

        Ping ping = new Ping(player, data);

        if (!validateEntityPing(ping))
            return;
        if (!validateLocationPing(ping))
            return;
        if (!validateLimits(ping))
            return;
        if (!validateWorldGuardFlag(ping))
            return;
        if (!validateRateLimit(player))
            return;

        broadcastPing(message, player.getWorld());
        logPing(ping);
    }

    private boolean validateEntityPing(Ping ping) {
        Player player = ping.getPlayer();
        if (ping.isEntity() && (!Config.ENTITY_PING_ENABLED || (Config.ENTITY_PING_PERMISSION_ENABLED && !player.hasPermission(Config.ENTITY_PING_PERMISSION)))) {
            sendMessage(player, Config.ENTITY_PING_NO_PERMISSION_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateLocationPing(Ping ping) {
        Player player = ping.getPlayer();
        if (!ping.isEntity() && ((!Config.LOCATION_PING_ENABLED) || (Config.LOCATION_PING_PERMISSION_ENABLED && !player.hasPermission(Config.LOCATION_PING_PERMISSION)))) {
            sendMessage(player, Config.LOCATION_PING_NO_PERMISSION_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateLimits(Ping ping) {
        return isWithinLimits(ping.getX(), Config.LIMIT_X_ENABLED, Config.LIMIT_X_MIN, Config.LIMIT_X_MAX)
                && isWithinLimits(ping.getY(), Config.LIMIT_Y_ENABLED, Config.LIMIT_Y_MIN, Config.LIMIT_Y_MAX)
                && isWithinLimits(ping.getZ(), Config.LIMIT_Z_ENABLED, Config.LIMIT_Z_MIN, Config.LIMIT_Z_MAX);
    }

    private boolean isWithinLimits(double value, boolean isEnabled, double minLimit, double maxLimit) {
        return !isEnabled || (value >= minLimit && value <= maxLimit);
    }

    private boolean validateWorldGuardFlag(Ping ping) {
        Player player = ping.getPlayer();
        if (Config.WORLDGUARD_ENABLED && WorldGuardHook.canLocationBePinged(ping.getLocation(), player)) {
            sendMessage(player, Config.WORLDGUARD_DENY_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateRateLimit(Player player) {
        if (!PingWheel.getRateLimiter().canPing(player) && !PingWheel.getRateLimiter().checkBypass(player)) {
            sendMessage(player, Config.RATE_LIMIT_MESSAGE);
            return false;
        }
        return true;
    }

    public void broadcastPing(byte[] data, World world) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getWorld() != world)
                continue;
            onlinePlayer.sendPluginMessage(
                    PingWheel.getPlugin(),
                    Constants.SERVER_TO_CLIENT_CHANNEL,
                    data
            );
        }
    }

    private void sendMessage(Player player, String message) {
        if (message.isEmpty())
            return;
        if (PingWheel.isModernMessages())
            player.sendMessage(MiniMessage.miniMessage().deserialize(message));
        else
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private void logPing(Ping ping) {
        if (!Config.LOGGING_ENABLED)
            return;
        PingWheel.getPlugin().getLogger().info(Config.LOGGING_MESSAGE
                .replace("%player%", ping.getPlayer().getName())
                .replace("%entity%", String.valueOf(ping.isEntity()))
                .replace("%x%", String.valueOf(ping.getX()))
                .replace("%y%", String.valueOf(ping.getY()))
                .replace("%z%", String.valueOf(ping.getZ()))
                .replace("%channel%", ping.getChannel())
        );
    }
}
