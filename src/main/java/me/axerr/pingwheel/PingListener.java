package me.axerr.pingwheel;

import io.netty.buffer.Unpooled;
import me.axerr.pingwheel.api.FriendlyByteBuf;
import me.axerr.pingwheel.ratelimit.PlayerData;
import me.axerr.pingwheel.ratelimit.RateLimit;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PingListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        // Cast data to object
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.copiedBuffer(message));
        Ping ping = new Ping(
                data.readString(),
                data.readDouble(),
                data.readDouble(),
                data.readDouble(),
                data.readBoolean(),
                null
        );
        if (ping.isEntity())
            ping.setEntityUUID(data.readUUID());

        // Validate entity ping
        if (ping.isEntity() && (!Config.ENTITY_PING_ENABLED || (Config.ENTITY_PING_PERMISSION_ENABLED && !player.hasPermission(Config.ENTITY_PING_PERMISSION)))) {
            if (!Config.ENTITY_PING_NO_PERMISSION_MESSAGE.isEmpty())
                player.sendMessage(MiniMessage.miniMessage().deserialize(Config.ENTITY_PING_NO_PERMISSION_MESSAGE));
            return;
        }

        // Validate location ping
        if (!ping.isEntity() && ((!Config.LOCATION_PING_ENABLED) || (Config.LOCATION_PING_PERMISSION_ENABLED && !player.hasPermission(Config.LOCATION_PING_PERMISSION)))) {
            if (!Config.LOCATION_PING_NO_PERMISSION_MESSAGE.isEmpty())
                player.sendMessage(MiniMessage.miniMessage().deserialize(Config.LOCATION_PING_NO_PERMISSION_MESSAGE));
            return;
        }

        // Validate limits data
        if (Config.LIMIT_X_ENABLED && (ping.getX() > Config.LIMIT_X_MAX || ping.getX() < Config.LIMIT_X_MIN))
            return;
        if (Config.LIMIT_Y_ENABLED && (ping.getY() > Config.LIMIT_Y_MAX || ping.getY() < Config.LIMIT_Y_MIN))
            return;
        if (Config.LIMIT_Z_ENABLED && (ping.getZ() > Config.LIMIT_Z_MAX || ping.getZ() < Config.LIMIT_Z_MIN))
            return;

        // Check rate limit
        PlayerData playerData = RateLimit.getData(player.getUniqueId());
        if (!playerData.canPing(player)) {
            if (!Config.RATE_LIMIT_MESSAGE.isEmpty())
                player.sendMessage(MiniMessage.miniMessage().deserialize(Config.RATE_LIMIT_MESSAGE));
            return;
        }
        playerData.ping();

        // Broadcast ping
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            if (onlinePlayer.getWorld() == player.getWorld())
                onlinePlayer.sendPluginMessage(
                        PingWheel.getPlugin(),
                        "ping-wheel-s2c:ping-location",
                        message
                );

        // Logging
        if (Config.LOGGING_ENABLED)
            PingWheel.getPlugin().getLogger().info(Config.LOGGING_MESSAGE
                    .replace("%player%", player.getName())
                    .replace("%entity%", String.valueOf(ping.isEntity()))
                    .replace("%x%", String.valueOf(ping.getX()))
                    .replace("%y%", String.valueOf(ping.getY()))
                    .replace("%z%", String.valueOf(ping.getZ()))
                    .replace("%channel%", ping.getChannel())
            );
    }
}