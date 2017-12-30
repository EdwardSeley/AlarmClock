package com.edwardseley.alarmclock;

import android.content.Context;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AlarmStorage {

    private static AlarmStorage sAlarmStorage;
    private List<Alarm> mAlarms;

    private AlarmStorage(Context context)
    {
        mAlarms = new ArrayList<Alarm>();
    }

    public static AlarmStorage get(Context context)
    {
        if (sAlarmStorage == null)
            sAlarmStorage = new AlarmStorage(context);

        return sAlarmStorage;
    }

    public void addAlarm(Alarm alarm)
    {
        mAlarms.add(alarm);
    }

    public List<Alarm> getAlarmList()
    {
        return mAlarms;
    }

    public Alarm getAlarm(UUID uuid)
    {
        for (Alarm alarm : mAlarms)
            if (alarm.getID().equals(uuid))
                return alarm;
        return null;
    }
}
