package me.moose.gaia.common.utils;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
public class UUIDUtils {
    public static UUID fromString(String input) {
        return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
