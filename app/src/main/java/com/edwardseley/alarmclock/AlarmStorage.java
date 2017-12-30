package com.edwardseley.alarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import database.AlarmCursorWrapper;
import database.AlarmDatabaseHelper;
import database.AlarmTableSchema;

public class AlarmStorage {

    private static AlarmStorage sAlarmStorage;
    private SQLiteDatabase mDatabase;

    private AlarmStorage(Context context)
    {
        mDatabase = new AlarmDatabaseHelper(context).getWritableDatabase();
    }

    public static AlarmStorage get(Context context)
    {
        if (sAlarmStorage == null)
            sAlarmStorage = new AlarmStorage(context);

        return sAlarmStorage;
    }

    public void updateAlarm(Alarm alarm)
    {
        ContentValues contentValues = getContentValues(alarm);
        mDatabase.update(AlarmTableSchema.NAME, contentValues, AlarmTableSchema.COLS.ALARM_ID + " = ?", new String[] {alarm.getID().toString()});
    }

    public ContentValues getContentValues(Alarm alarm)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AlarmTableSchema.COLS.ALARM_TIME, alarm.getTime().getTime());
        contentValues.put(AlarmTableSchema.COLS.ALARM_DATE, alarm.getDate().getTime());
        contentValues.put(AlarmTableSchema.COLS.ALARM_ID, alarm.getID().toString());
        contentValues.put(AlarmTableSchema.COLS.ALARM_TURNED_ON, alarm.isTurnedOn() ? 1 : 0);
        return contentValues;
    }

    public void addAlarm(Alarm alarm)
    {
        mDatabase.insert(AlarmTableSchema.NAME, null, getContentValues(alarm));
    }

    public void removeAlarm(Alarm alarm)
    {
        mDatabase.delete(AlarmTableSchema.NAME, AlarmTableSchema.COLS.ALARM_ID + " = ?", new String[] {alarm.getID().toString()});
    }

    public Cursor query(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(AlarmTableSchema.NAME, null, whereClause, whereArgs, null, null, null, null);
        return cursor;
    }

    public List<Alarm> getAlarmList()
    {
        List<Alarm> alarmList = new ArrayList<>();

        Cursor cursor = query(null, null);
        AlarmCursorWrapper cursorWrapper = new AlarmCursorWrapper(cursor);
        cursorWrapper.moveToFirst();

        try {
            while (!cursorWrapper.isAfterLast()) {
                Alarm alarm = cursorWrapper.getAlarm();
                alarmList.add(alarm);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return alarmList;
    }

    public Alarm getAlarm(UUID uuid)
    {
        Cursor cursor = query(AlarmTableSchema.COLS.ALARM_ID + " = ?", new String[]{uuid.toString()});
        AlarmCursorWrapper cursorWrapper = new AlarmCursorWrapper(cursor);
        if (cursorWrapper.getCount() == 0)
            return null;

        try {
            cursorWrapper.moveToFirst();
            return cursorWrapper.getAlarm();
        }
        finally {
            cursorWrapper.close();
            cursor.close();
        }
    }
}
