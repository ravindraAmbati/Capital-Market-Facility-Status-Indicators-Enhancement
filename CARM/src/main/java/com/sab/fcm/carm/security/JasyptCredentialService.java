package com.sab.fcm.carm.security;

import com.sab.fcm.carm.config.CarmFcmProperties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;

@Service
public class JasyptCredentialService implements CredentialService {

    private final StandardPBEStringEncryptor encryptor;

    public JasyptCredentialService(CarmFcmProperties properties) {
        String password = properties.getJasyptPassword();

        if (password == null || password.trim().isEmpty()) {
            password = System.getProperty("carm.fcm.jasypt.password");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Missing CARM-FCM Jasypt master password. " +
                    "Configure carm.fcm.jasypt.password as an IBM WAS JVM Custom Property.");
        }

        encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(properties.getJasyptAlgorithm());
        encryptor.setPassword(password);
    }

    @Override
    public String decrypt(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        if (trimmed.startsWith("ENC(") && trimmed.endsWith(")")) {
            return encryptor.decrypt(trimmed.substring(4, trimmed.length() - 1));
        }

        return value;
    }
}
