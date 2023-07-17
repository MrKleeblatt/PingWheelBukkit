package me.axerr.pingwheel.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.axerr.pingwheel.Config;
import me.axerr.pingwheel.PingWheel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardHook {
    private static boolean HOOKED = false;
    private static StateFlag ALLOW_PINGS_FLAG;

    public static void hook() {
        try {
            ALLOW_PINGS_FLAG = new StateFlag("allow-pings", true);
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            registry.register(ALLOW_PINGS_FLAG);
            HOOKED = true;
        } catch (FlagConflictException exception) {
            PingWheel.getPlugin().getLogger().severe("Couldn't register custom flag due to: " + exception.getMessage());
        }
    }

    public static boolean canLocationBePinged(Location location, Player player) {
        if (!HOOKED || !Config.WORLDGUARD_ENABLED)
            return true;

        RegionContainer container = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer();

        RegionQuery query = container.createQuery();

        return !query.testState(BukkitAdapter.adapt(location),
                WorldGuardPlugin.inst().wrapPlayer(player),
                ALLOW_PINGS_FLAG
        );
    }
}
