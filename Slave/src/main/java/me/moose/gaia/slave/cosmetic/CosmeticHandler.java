package me.moose.gaia.slave.cosmetic;

import me.moose.gaia.common.cosmetic.ICosmeticHandler;
import me.moose.gaia.common.cosmetic.data.CommonCosmetic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
public class CosmeticHandler implements ICosmeticHandler {
    private Map<String, CommonCosmetic> cosmetics = new HashMap<>();

    public Map<String, CommonCosmetic> getCosmetics() {
        return cosmetics;
    }
    public void addCosmetics(List<CommonCosmetic> cosmetics) {
        cosmetics.forEach(this::addCosmetic);
    }
    public void addCosmetic(CommonCosmetic cosmetic) {
        cosmetics.put(cosmetic.getName(), cosmetic);
    }


    public CommonCosmetic getCosmetic(String name) {
        return cosmetics.get(name);
    }

    @Override
    public CommonCosmetic getCommonCosmetic(String name) {
        return getCosmetic(name);
    }
}
