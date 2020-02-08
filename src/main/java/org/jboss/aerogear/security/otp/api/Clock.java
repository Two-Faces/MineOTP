package org.jboss.aerogear.security.otp.api;

import java.util.TimeZone;
import java.util.Calendar;

public class Clock
{
    private final int interval;

    public Clock() {
        this.interval = 30;
    }

    public long getCurrentInterval() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        final long currentTimeSeconds = calendar.getTimeInMillis() / 1000L;
        return currentTimeSeconds / this.interval;
    }
}
