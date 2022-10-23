package me.moose.gaia.common.cosmetic.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor @Getter
public class CommonCosmetic {
    private final UUID uuid;
    private final String name;
    private final String type;
    private float scale;
    private final String resourceLocation;
}
