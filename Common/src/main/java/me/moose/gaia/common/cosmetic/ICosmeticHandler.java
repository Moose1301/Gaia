package me.moose.gaia.common.cosmetic;

import me.moose.gaia.common.cosmetic.data.Cosmetic;

import java.util.Map;
import java.util.Optional;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public interface ICosmeticHandler {

    Map<String, Cosmetic> getCosmetics();
    void addCosmetic(Cosmetic cosmetic);
    Optional<Cosmetic> getCosmetic(String name);
}
