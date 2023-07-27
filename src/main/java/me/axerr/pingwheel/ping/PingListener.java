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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PingListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.copiedBuffer(message));
        Ping ping = new Ping(player, data);

        if(!isPingAllowed(ping)) return;

        broadcastPing(message, player.getWorld());
        logPing(ping);
    }

    private boolean isPingAllowed(Ping ping) {
        Player player = ping.getPlayer();

        if(!canPlayerUsePing(ping)) return false;
        if(!isPingWithinXYZLimits(ping)) return false;
        if(!isInsidePingableWorldGuardRegion(ping)) {
            sendMessage(player, Config.WORLDGUARD_DENY_MESSAGE);
            return false;
        }
        if(isPlayerRateLimited(player)){
            sendMessage(player, Config.RATE_LIMIT_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean canPlayerUsePing(Ping ping) {
        if(!isPermissionToUsePingRequired(ping)) return true;

        boolean isPlayerPermitted = doesPlayerHavePermissionToUsePing(ping);
        if(!isPlayerPermitted)
            sendPingNoPermissionMessage(ping);
        return isPlayerPermitted;
    }

    private boolean isPingWithinXYZLimits(Ping ping) {
        return isPingWithinXLimit(ping) && isPingWithinYLimit(ping) && isPingWithinZLimit(ping);
    }

    private boolean isInsidePingableWorldGuardRegion(Ping ping) {
        Player player = ping.getPlayer();
        Location location = ping.getLocation();
        return WorldGuardHook.canLocationBePinged(location, player);
    }

    private boolean isPlayerRateLimited(Player player) {
        if(PingWheel.getRateLimiter().checkBypass(player)) return false;
        return !PingWheel.getRateLimiter().canPing(player);
    }

    private boolean isPermissionToUsePingRequired(Ping ping) {
        if(ping.isEntity())
            return Config.ENTITY_PING_PERMISSION_ENABLED;
        else
            return Config.LOCATION_PING_PERMISSION_ENABLED;
    }

    private void sendPingNoPermissionMessage(Ping ping) {
        Player player = ping.getPlayer();
        if(ping.isEntity())
            sendMessage(player, Config.ENTITY_PING_NO_PERMISSION_MESSAGE);
        else
            sendMessage(player, Config.LOCATION_PING_NO_PERMISSION_MESSAGE);
    }

    private boolean doesPlayerHavePermissionToUsePing(Ping ping) {
        Player player = ping.getPlayer();
        if(ping.isEntity())
            return player.hasPermission(Config.ENTITY_PING_PERMISSION);
        else
            return player.hasPermission(Config.LOCATION_PING_PERMISSION);
    }

    private boolean isPingWithinZLimit(Ping ping) {
        return isWithinLimit(ping.getZ(), Config.LIMIT_Z_ENABLED, Config.LIMIT_Z_MIN, Config.LIMIT_Z_MAX);
    }

    private boolean isPingWithinYLimit(Ping ping) {
        return isWithinLimit(ping.getY(), Config.LIMIT_Y_ENABLED, Config.LIMIT_Y_MIN, Config.LIMIT_Y_MAX);
    }

    private boolean isPingWithinXLimit(Ping ping) {
        return isWithinLimit(ping.getX(), Config.LIMIT_X_ENABLED, Config.LIMIT_X_MIN, Config.LIMIT_X_MAX);
    }

    private boolean isWithinLimit(double value, boolean isEnabled, double minLimit, double maxLimit) {
        return !isEnabled || (value >= minLimit && value <= maxLimit);
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

    //TODO: Extract into separate util class and add static import
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
