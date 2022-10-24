package me.moose.gaia.common.utils;

import me.moose.gaia.common.GaiaServer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
public class SecurityHandler {

    public static KeyPair generateKeyPair() {

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");     kpg.initialize(2048);

            return kpg.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            GaiaServer.getLogger().error("SecurityHandler", "Error while generating Key Pair");
            e.printStackTrace();
            return null;
        }

    }
}
