package me.axerr.pingwheel;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static boolean LOCATION_PING_ENABLED;
    public static boolean LOCATION_PING_PERMISSION_ENABLED;
    public static String LOCATION_PING_PERMISSION;
    public static String LOCATION_PING_NO_PERMISSION_MESSAGE;

    public static boolean ENTITY_PING_ENABLED;
    public static boolean ENTITY_PING_PERMISSION_ENABLED;
    public static String ENTITY_PING_PERMISSION;
    public static String ENTITY_PING_NO_PERMISSION_MESSAGE;

    public static boolean LIMIT_X_ENABLED;
    public static double LIMIT_X_MAX;
    public static double LIMIT_X_MIN;

    public static boolean LIMIT_Y_ENABLED;
    public static double LIMIT_Y_MAX;
    public static double LIMIT_Y_MIN;

    public static boolean LIMIT_Z_ENABLED;
    public static double LIMIT_Z_MAX;
    public static double LIMIT_Z_MIN;

    public static boolean RATE_LIMIT_ENABLED;
    public static int RATE_LIMIT_PING_LIMIT;
    public static long RATE_LIMIT_TIME_WINDOW;
    public static String RATE_LIMIT_MESSAGE;
    public static boolean RATE_LIMIT_BYPASS_ENABLED;
    public static String RATE_LIMIT_BYPASS_PERMISSION;

    public static boolean WORLDGUARD_ENABLED;
    public static String WORLDGUARD_DENY_MESSAGE;

    public static boolean LOGGING_ENABLED;
    public static String LOGGING_MESSAGE;

    public static void loadConfig(PingWheel plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        FileConfiguration config = plugin.getConfig();

        LOCATION_PING_ENABLED = config.getBoolean("features.location-ping.enabled");
        LOCATION_PING_PERMISSION_ENABLED = config.getBoolean("features.location-ping.permission.enabled");
        LOCATION_PING_PERMISSION = config.getString("features.location-ping.permission.permission");
        LOCATION_PING_NO_PERMISSION_MESSAGE = config.getString("features.location-ping.permission.no-permission-message");

        ENTITY_PING_ENABLED = config.getBoolean("features.entity-ping.enabled");
        ENTITY_PING_PERMISSION_ENABLED = config.getBoolean("features.entity-ping.permission.enabled");
        ENTITY_PING_PERMISSION = config.getString("features.entity-ping.permission.permission");
        ENTITY_PING_NO_PERMISSION_MESSAGE = config.getString("features.entity-ping.permission.no-permission-message");

        LIMIT_X_ENABLED = config.getBoolean("limits.x.enabled");
        LIMIT_X_MAX = config.getDouble("limits.x.max");
        LIMIT_X_MIN = config.getDouble("limits.x.min");

        LIMIT_Y_ENABLED = config.getBoolean("limits.y.enabled");
        LIMIT_Y_MAX = config.getDouble("limits.y.max");
        LIMIT_Y_MIN = config.getDouble("limits.y.min");

        LIMIT_Z_ENABLED = config.getBoolean("limits.z.enabled");
        LIMIT_Z_MAX = config.getDouble("limits.z.max");
        LIMIT_Z_MIN = config.getDouble("limits.z.min");

        RATE_LIMIT_ENABLED = config.getBoolean("rate-limit.enabled");
        RATE_LIMIT_PING_LIMIT = config.getInt("rate-limit.ping-limit");
        RATE_LIMIT_TIME_WINDOW = config.getLong("rate-limit.time-window");
        RATE_LIMIT_MESSAGE = config.getString("rate-limit.rate-limit-message");
        RATE_LIMIT_BYPASS_ENABLED = config.getBoolean("rate-limit.bypass.enabled");
        RATE_LIMIT_BYPASS_PERMISSION = config.getString("rate-limit.bypass.permission");

        WORLDGUARD_ENABLED = config.getBoolean("worldguard.enabled");
        WORLDGUARD_DENY_MESSAGE = config.getString("worldguard.deny-message");

        LOGGING_ENABLED = config.getBoolean("logging.enabled");
        LOGGING_MESSAGE = config.getString("logging.message");
    }
}
