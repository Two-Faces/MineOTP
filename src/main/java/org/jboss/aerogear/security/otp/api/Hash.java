package org.jboss.aerogear.security.otp.api;

public enum Hash
{
    SHA1("SHA1", 0, "HMACSHA1");
    
    private String hash;
    
    Hash(final String s, final int n, final String hash) {
        this.hash = hash;
    }
    
    @Override
    public String toString() {
        return this.hash;
    }
}
