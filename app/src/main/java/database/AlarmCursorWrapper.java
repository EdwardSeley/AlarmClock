package database;

import android.database.Cursor;

import com.edwardseley.alarmclock.Alarm;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class AlarmCursorWrapper extends android.database.CursorWrapper {

    public AlarmCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Alarm getAlarm()
    {
        long time = getLong(getColumnIndex(AlarmTableSchema.COLS.ALARM_TIME));
        long date = getLong(getColumnIndex(AlarmTableSchema.COLS.ALARM_DATE));
        String uuidString = getString(getColumnIndex(AlarmTableSchema.COLS.ALARM_ID));
        long turnedOn = getLong(getColumnIndex(AlarmTableSchema.COLS.ALARM_TURNED_ON));

        Alarm alarm = new Alarm(UUID.fromString(uuidString));
        alarm.setTime(new Time(time));
        alarm.setDate(new Date(date));
        alarm.setTurnedOn(turnedOn != 0);

        return alarm;
    }


}
