package me.axerr.pingwheel;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    // Features - location ping
    public static boolean LOCATION_PING_ENABLED;
    public static boolean LOCATION_PING_PERMISSION_ENABLED;
    public static String LOCATION_PING_PERMISSION;
    public static String LOCATION_PING_NO_PERMISSION_MESSAGE;

    // Features - entity ping
    public static boolean ENTITY_PING_ENABLED;
    public static boolean ENTITY_PING_PERMISSION_ENABLED;
    public static String ENTITY_PING_PERMISSION;
    public static String ENTITY_PING_NO_PERMISSION_MESSAGE;

    // Limits - x
    public static boolean LIMIT_X_ENABLED;
    public static double LIMIT_X_MAX;
    public static double LIMIT_X_MIN;

    // Limits - x
    public static boolean LIMIT_Y_ENABLED;
    public static double LIMIT_Y_MAX;
    public static double LIMIT_Y_MIN;

    // Limits - z
    public static boolean LIMIT_Z_ENABLED;
    public static double LIMIT_Z_MAX;
    public static double LIMIT_Z_MIN;

    // Rate limiting
    public static boolean RATE_LIMIT_ENABLED;
    public static int RATE_LIMIT_THRESHOLD;
    public static int RATE_LIMIT_TIME;
    public static String RATE_LIMIT_MESSAGE;
    public static boolean RATE_LIMIT_BYPASS_ENABLED;
    public static String RATE_LIMIT_BYPASS_PERMISSION;

    // Logging
    public static boolean LOGGING_ENABLED;
    public static String LOGGING_MESSAGE;

    public static void loadConfig(PingWheel plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        FileConfiguration config = plugin.getConfig();

        // Features - location ping
        LOCATION_PING_ENABLED = config.getBoolean("features.location-ping.enabled");
        LOCATION_PING_PERMISSION_ENABLED = config.getBoolean("features.location-ping.permission.enabled");
        LOCATION_PING_PERMISSION = config.getString("features.location-ping.permission.permission");
        LOCATION_PING_NO_PERMISSION_MESSAGE = config.getString("features.location-ping.permission.no-permission-message");

        // Features - entity ping
        ENTITY_PING_ENABLED = config.getBoolean("features.entity-ping.enabled");
        ENTITY_PING_PERMISSION_ENABLED = config.getBoolean("features.entity-ping.permission.enabled");
        ENTITY_PING_PERMISSION = config.getString("features.entity-ping.permission.permission");
        ENTITY_PING_NO_PERMISSION_MESSAGE = config.getString("features.entity-ping.permission.no-permission-message");

        // Limits - x
        LIMIT_X_ENABLED = config.getBoolean("limits.x.enabled");
        LIMIT_X_MAX = config.getDouble("limits.x.max");
        LIMIT_X_MIN = config.getDouble("limits.x.min");

        // Limits - y
        LIMIT_Y_ENABLED = config.getBoolean("limits.y.enabled");
        LIMIT_Y_MAX = config.getDouble("limits.y.max");
        LIMIT_Y_MIN = config.getDouble("limits.y.min");

        // Limits - z
        LIMIT_Z_ENABLED = config.getBoolean("limits.z.enabled");
        LIMIT_Z_MAX = config.getDouble("limits.z.max");
        LIMIT_Z_MIN = config.getDouble("limits.z.min");

        // Rate limiting
        RATE_LIMIT_ENABLED = config.getBoolean("rate-limit.enabled");
        RATE_LIMIT_THRESHOLD = config.getInt("rate-limit.threshold");
        RATE_LIMIT_TIME = config.getInt("rate-limit.time");
        RATE_LIMIT_MESSAGE = config.getString("rate-limit.rate-limit-message");
        RATE_LIMIT_BYPASS_ENABLED = config.getBoolean("rate-limit.bypass.enabled");
        RATE_LIMIT_BYPASS_PERMISSION = config.getString("rate-limit.bypass.permission");

        // Logging
        LOGGING_ENABLED = config.getBoolean("logging.enabled");
        LOGGING_MESSAGE = config.getString("logging.message");
    }
}
