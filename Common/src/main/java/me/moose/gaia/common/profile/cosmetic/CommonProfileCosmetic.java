package me.moose.gaia.common.profile.cosmetic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moose.gaia.common.cosmetic.data.CommonCosmetic;

/**
 * @author Moose1301
 * @date 10/22/2022
 */

@AllArgsConstructor @NoArgsConstructor @Getter
public class CommonProfileCosmetic {
    private CommonCosmetic cosmetic;
    private boolean active;
}
