// 
// Decompiled by Procyon v0.5.36
// 

package org.jboss.aerogear.security.otp;

import org.jboss.aerogear.security.otp.api.Digits;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.jboss.aerogear.security.otp.api.Hmac;
import org.jboss.aerogear.security.otp.api.Base32;
import org.jboss.aerogear.security.otp.api.Hash;
import org.jboss.aerogear.security.otp.api.Clock;

public class Totp
{
    private final String secret;
    private final Clock clock;

    public Totp(final String secret) {
        this.secret = secret;
        this.clock = new Clock();
    }

    public boolean verify(final String otp) {
        final long code = Long.parseLong(otp);
        final long currentInterval = this.clock.getCurrentInterval();
        for (int i = Math.max(1, 0); i >= 0; --i) {
            final int candidate = this.generate(this.secret, currentInterval - i);
            if (candidate == code) {
                return true;
            }
        }
        return false;
    }
    
    private int generate(final String secret, final long interval) {
        return this.hash(secret, interval);
    }
    
    private int hash(final String secret, final long interval) {
        byte[] hash = new byte[0];
        try {
            hash = new Hmac(Hash.SHA1, Base32.decode(secret), interval).digest();
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | Base32.DecodingException e) {
            e.printStackTrace();
        }
        return this.bytesToInt(hash);
    }
    
    private int bytesToInt(final byte[] hash) {
        final int offset = hash[hash.length - 1] & 0xF;
        final int binary = (hash[offset] & 0x7F) << 24 | (hash[offset + 1] & 0xFF) << 16 | (hash[offset + 2] & 0xFF) << 8 | (hash[offset + 3] & 0xFF);
        return binary % Digits.SIX.getValue();
    }
}
