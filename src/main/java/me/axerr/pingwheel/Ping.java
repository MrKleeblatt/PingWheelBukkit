package me.axerr.pingwheel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Ping {
    private String channel;
    private double x, y, z;
    private boolean isEntity;
    private UUID entityUUID;
}