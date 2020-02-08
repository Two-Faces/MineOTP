package org.jboss.aerogear.security.otp.api;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.nio.ByteBuffer;

public class Hmac
{
    private final Hash hash;
    private final byte[] secret;
    private final long currentInterval;
    
    public Hmac(final Hash hash, final byte[] secret, final long currentInterval) {
        this.hash = hash;
        this.secret = secret;
        this.currentInterval = currentInterval;
    }
    
    public byte[] digest() throws NoSuchAlgorithmException, InvalidKeyException {
        final byte[] challenge = ByteBuffer.allocate(8).putLong(this.currentInterval).array();
        final Mac mac = Mac.getInstance(this.hash.toString());
        final SecretKeySpec macKey = new SecretKeySpec(this.secret, "RAW");
        mac.init(macKey);
        return mac.doFinal(challenge);
    }
}
