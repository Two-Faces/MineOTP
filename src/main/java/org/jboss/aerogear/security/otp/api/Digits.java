package org.jboss.aerogear.security.otp.api;

public enum Digits
{
    SIX("SIX", 0, 1000000);
    
    private int digits;
    
    Digits(final String s, final int n, final int digits) {
        this.digits = digits;
    }
    
    public int getValue() {
        return this.digits;
    }
}
