package me.moose.gaia.common.profile.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Moose1301
 * @date 10/22/2022
 */
@AllArgsConstructor
public enum Rank {
    OWNER(100),
    DEVELOPER(95),
    DEFAULT(0);

    @Getter
    private final int priority;

    /**
     * Checks if a rank has valid permission or same permission as
     * b rank.
     *
     * @param a the rank to testify with
     * @param b the rank to check if valid
     * @return if b rank is valid
     */
    public static boolean isValid(Rank a, Rank b) {
        return b.priority >= a.priority;
    }
}
