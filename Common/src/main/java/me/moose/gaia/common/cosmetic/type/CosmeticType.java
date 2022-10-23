package me.moose.gaia.common.cosmetic.type;

public enum CosmeticType {
    CAPE,
    DRAGON_WINGS;

    public String getName() {
        return this.name().toLowerCase();
    }
}
