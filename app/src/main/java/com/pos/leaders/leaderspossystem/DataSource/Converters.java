package com.pos.leaders.leaderspossystem.DataSource;

/**
 * Created by Win8.1 on 11/13/2018.
 */

import android.arch.persistence.room.TypeConverter;
import java.util.Date;
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
