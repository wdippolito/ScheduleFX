package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * class for handling time conversions
 */
public class Time {

    private static ZoneId UTC = ZoneId.of("UTC");

    /**
     * convert local system time to UTC
     */
    public static LocalDateTime toUTC(LocalDateTime toConvert){
       // ZoneOffset utcOffset = localZone.getRules().getOffset(Instant.now())
        return toConvert.atZone(ZoneId.systemDefault()).withZoneSameInstant(UTC).toLocalDateTime();
    }
    /**
     * convert local UTC time to local system time
     */
    public static LocalDateTime toSystem(LocalDateTime toConvert){
        return toConvert.atZone(UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }
}
