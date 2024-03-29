# PingWheelBukkit

This is my Bukkit implementation for the Fabric [Minecraft-Ping-Wheel](https://github.com/LukenSkyne/Minecraft-Ping-Wheel/) mod.

## Integrations

#### WorldGuard
The plugin has [WorldGuard](https://github.com/EngineHub/WorldGuard) support. Custom flag: `allow-pings` (allow/deny)

## Plugin Configuration

This plugin comes with extensive configuration, which allows you to customize the plugin according to your needs.

```
features:
  location-ping:
    enabled: true
    permission:
      enabled: false
      permission: "pingwheel.ping.location"
      no-permission-message: "<gradient:#5ad396:#03c574><b>Ping Wheel<b></gradient> <#42a85a>» <white>You can't ping locations"

  entity-ping:
    enabled: true
    permission:
      enabled: false
      permission: "pingwheel.ping.entity"
      no-permission-message: "<gradient:#5ad396:#03c574><b>Ping Wheel<b></gradient> <#42a85a>» <white>You can't ping entities"

limits:
  x:
    enabled: true
    max: 30000000.0
    min: -30000000.0
  y:
    enabled: true
    min: -64.0
    max: 320.0
  z:
    enabled: true
    max: 30000000.0
    min: -30000000.0

rate-limit:
  enabled: true
  ping-limit: 5
  time-window: 10000 # 10 seconds
  rate-limit-message: "<gradient:#5ad396:#03c574><b>Ping Wheel<b></gradient> <#42a85a>» <white>Wait a while before the next ping!"
  bypass:
    enabled: true
    permission: "pingwheel.ratelimit.bypass"

worldguard:
  enabled: true
  deny-message: "<gradient:#5ad396:#03c574><b>Ping Wheel<b></gradient> <#42a85a>» <white>You can't ping in this region!"

logging:
  enabled: false
  message: "%player% pinged (entity: %entity%) at %x%, %y%, %z% on channel %channel%"
```
