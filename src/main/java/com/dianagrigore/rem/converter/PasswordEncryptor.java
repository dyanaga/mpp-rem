package com.dianagrigore.rem.converter;

import com.dianagrigore.rem.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;
import java.util.Objects;

@Component
public class PasswordEncryptor implements AttributeConverter<String, String> {

    private final Key key;
    private final Cipher cipher;

    public PasswordEncryptor(@Value("${converters.password-encryptor.secret}") String secret) {
        try {
            this.key = new SecretKeySpec(secret.getBytes(), "AES");
            this.cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            throw new BaseException("Could not create the PasswordEncryptor class.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if(Objects.isNull(attribute)) {
            return null;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new BaseException("Password encryption error.", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if(Objects.isNull(dbData)) {
            return null;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new BaseException("Gateway password decryption error.", e);
        }
    }
}
