package com.sab.carm.fcm.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.NoIvGenerator;

public final class JasyptEncryptionUtil {

    private StandardPBEStringEncryptor encryptor = null;
    public static final String ALGORITHM = "PBEWithMD5AndDES";
    private static final String ENCRYPTION_PASSWORD = "CHANGE_ME";

    private JasyptEncryptionUtil() {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(ENCRYPTION_PASSWORD);
        encryptor.setAlgorithm(ALGORITHM);
        encryptor.setKeyObtentionIterations(1000);
        encryptor.setIvGenerator(new NoIvGenerator());
    }

    public String encrypt(String value) {
        return encryptor.encrypt(value);
    }

    public String decrypt(String encryptedString) {
        return encryptor.decrypt(encryptedString);
    }

    public static void main(String[] args) {

        JasyptEncryptionUtil util = new JasyptEncryptionUtil();
        String originalString = "1234567890!@#$%^&*()_+[];',./?><:|}{\" qwertyuiopasdfghjklmnbvcxz QAZWSXEDCRFVTGBYHNUJMIKLOP";
        System.out.println("Before Encrypt: " + originalString);
        String encryptedString = util.encrypt(originalString);
        System.out.println("After Encrypt:" + "ENC(" + encryptedString + ")");
        String decryptedString = util.decrypt(encryptedString);
        System.out.println();
        System.out.println("Decrypted: " + decryptedString);
        System.out.println("Verification: " + originalString.equals(decryptedString));
    }
}