package me.moose.gaia.slave.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class CryptUtils {
    public static SecretKey createNewSharedKey()
    {
        try
        {
            KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
            keygenerator.init(128);
            return keygenerator.generateKey();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            throw new Error(nosuchalgorithmexception);
        }
    }

    public static KeyPair generateKeyPair()
    {
        try
        {
            KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
            keypairgenerator.initialize(1024);
            return keypairgenerator.generateKeyPair();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
            System.out.println("Key pair generation failed!");
            return null;
        }
    }

    public static byte[] getServerIdHash(String serverId, PublicKey publicKey, SecretKey secretKey)
    {
        try
        {
            return digestOperation("SHA-1", serverId.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded());
        }
        catch (UnsupportedEncodingException unsupportedencodingexception)
        {
            unsupportedencodingexception.printStackTrace();
            return null;
        }
    }

    private static byte[] digestOperation(String algorithm, byte[]... data)
    {
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance(algorithm);

            for (byte[] abyte : data)
            {
                messagedigest.update(abyte);
            }

            return messagedigest.digest();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
            return null;
        }
    }

    public static PublicKey decodePublicKey(byte[] encodedKey)
    {
        try
        {
            EncodedKeySpec encodedkeyspec = new X509EncodedKeySpec(encodedKey);
            KeyFactory keyfactory = KeyFactory.getInstance("RSA");
            return keyfactory.generatePublic(encodedkeyspec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored)
        {
        }

        System.out.println("Public key reconstitute failed!");
        return null;
    }

    public static SecretKey decryptSharedKey(PrivateKey key, byte[] secretKeyEncrypted)
    {
        return new SecretKeySpec(decryptData(key, secretKeyEncrypted), "AES");
    }

    public static byte[] encryptData(Key key, byte[] data)
    {
        return cipherOperation(1, key, data);
    }

    public static byte[] decryptData(Key key, byte[] data)
    {
        return cipherOperation(2, key, data);
    }

    private static byte[] cipherOperation(int opMode, Key key, byte[] data)
    {
        try
        {
            return createTheCipherInstance(opMode, key.getAlgorithm(), key).doFinal(data);
        }
        catch (IllegalBlockSizeException | BadPaddingException illegalblocksizeexception)
        {
            illegalblocksizeexception.printStackTrace();
        }

        System.out.println("Cipher data failed!");
        return null;
    }

    private static Cipher createTheCipherInstance(int opMode, String transformation, Key key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(opMode, key);
            return cipher;
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException invalidkeyexception)
        {
            invalidkeyexception.printStackTrace();
        }

        System.out.println("Cipher creation failed!");
        return null;
    }

    public static Cipher createNetCipherInstance(int opMode, Key key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(opMode, key, (new IvParameterSpec(key.getEncoded())));
            return cipher;
        }
        catch (GeneralSecurityException generalsecurityexception)
        {
            throw new RuntimeException(generalsecurityexception);
        }
    }
}
