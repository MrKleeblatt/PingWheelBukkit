package me.axerr.pingwheel.ping;

import io.netty.buffer.Unpooled;
import me.axerr.pingwheel.Config;
import me.axerr.pingwheel.PingWheel;
import me.axerr.pingwheel.api.FriendlyByteBuf;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PingListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.copiedBuffer(message));
        Ping ping = new Ping(
                player,
                data.readString(),
                data.readDouble(),
                data.readDouble(),
                data.readDouble(),
                data.readBoolean(),
                null
        );
        if (ping.isEntity())
            ping.setEntityUUID(data.readUUID());

        if (!validateEntityPing(ping))
            return;
        if (!validateLocationPing(ping))
            return;
        if (!validateLimits(ping))
            return;

        if (!PingWheel.getRateLimiter().canPing(player) && !PingWheel.getRateLimiter().checkBypass(player)) {
            if (!Config.RATE_LIMIT_MESSAGE.isEmpty())
                player.sendMessage(MiniMessage.miniMessage().deserialize(Config.RATE_LIMIT_MESSAGE));
            return;
        }

        broadcastPing(message, player.getWorld());

        logPing(ping);
    }

    private boolean validateEntityPing(Ping ping) {
        Player player = ping.getPlayer();
        if (ping.isEntity() && (!Config.ENTITY_PING_ENABLED || (Config.ENTITY_PING_PERMISSION_ENABLED && !player.hasPermission(Config.ENTITY_PING_PERMISSION)))) {
            if (!Config.ENTITY_PING_NO_PERMISSION_MESSAGE.isEmpty())
                player.sendMessage(MiniMessage.miniMessage().deserialize(Config.ENTITY_PING_NO_PERMISSION_MESSAGE));
            return false;
        }
        return true;
    }

    private boolean validateLocationPing(Ping ping) {
        Player player = ping.getPlayer();
        if (!ping.isEntity() && ((!Config.LOCATION_PING_ENABLED) || (Config.LOCATION_PING_PERMISSION_ENABLED && !player.hasPermission(Config.LOCATION_PING_PERMISSION)))) {
            if (!Config.LOCATION_PING_NO_PERMISSION_MESSAGE.isEmpty())
                player.sendMessage(MiniMessage.miniMessage().deserialize(Config.LOCATION_PING_NO_PERMISSION_MESSAGE));
            return false;
        }
        return true;
    }

    private boolean validateLimits(Ping ping) {
        if (Config.LIMIT_X_ENABLED && (ping.getX() > Config.LIMIT_X_MAX || ping.getX() < Config.LIMIT_X_MIN))
            return false;
        if (Config.LIMIT_Y_ENABLED && (ping.getY() > Config.LIMIT_Y_MAX || ping.getY() < Config.LIMIT_Y_MIN))
            return false;
        if (Config.LIMIT_Z_ENABLED && (ping.getZ() > Config.LIMIT_Z_MAX || ping.getZ() < Config.LIMIT_Z_MIN))
            return false;
        return true;
    }

    public void broadcastPing(byte[] data, World world) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getWorld() != world)
                continue;
            onlinePlayer.sendPluginMessage(
                    PingWheel.getPlugin(),
                    "ping-wheel-s2c:ping-location",
                    data
            );
        }
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
