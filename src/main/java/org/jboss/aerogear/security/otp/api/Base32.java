package org.jboss.aerogear.security.otp.api;

import java.util.Locale;
import java.util.HashMap;

public class Base32
{
    private static final Base32 INSTANCE;
    private int MASK;
    private int SHIFT;
    private HashMap<Character, Integer> CHAR_MAP;

    static Base32 getInstance() {
        return Base32.INSTANCE;
    }
    
    protected Base32(final String alphabet) {
        char[] DIGITS = alphabet.toCharArray();
        this.MASK = DIGITS.length - 1;
        this.SHIFT = Integer.numberOfTrailingZeros(DIGITS.length);
        this.CHAR_MAP = new HashMap<>();
        for (int i = 0; i < DIGITS.length; ++i) {
            this.CHAR_MAP.put(DIGITS[i], i);
        }
    }
    
    public static byte[] decode(final String encoded) throws DecodingException {
        return getInstance().decodeInternal(encoded);
    }
    
    protected byte[] decodeInternal(String encoded) throws DecodingException {
        encoded = encoded.trim().replaceAll("-", "").replaceAll(" ", "");
        encoded = encoded.replaceFirst("[=]*$", "");
        encoded = encoded.toUpperCase(Locale.US);
        if (encoded.length() == 0) {
            return new byte[0];
        }
        final int encodedLength = encoded.length();
        final int outLength = encodedLength * this.SHIFT / 8;
        final byte[] result = new byte[outLength];
        int buffer = 0;
        int next = 0;
        int bitsLeft = 0;
        char[] charArray;
        for (int length = (charArray = encoded.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            if (!this.CHAR_MAP.containsKey(c)) {
                throw new DecodingException("Illegal character: " + c);
            }
            buffer <<= this.SHIFT;
            buffer |= (this.CHAR_MAP.get(c) & this.MASK);
            bitsLeft += this.SHIFT;
            if (bitsLeft >= 8) {
                result[next++] = (byte)(buffer >> bitsLeft - 8);
                bitsLeft -= 8;
            }
        }
        return result;
    }

    static {
        INSTANCE = new Base32("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567");
    }
    
    public static class DecodingException extends Exception
    {
        private static final long serialVersionUID = -5684267668478052752L;
        
        public DecodingException(final String message) {
            super(message);
        }
    }
}
