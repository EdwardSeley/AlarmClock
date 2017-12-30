package com.edwardseley.alarmclock;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class Alarm {

    private Time mTime;
    private Date mDate;
    private UUID mID;
    private boolean mTurnedOn;

    public Alarm()
    {
        mDate = new Date();
        mTime = new Time(mDate.getTime());
        mID = UUID.randomUUID();
        mTurnedOn = true;
    }

    public Time getTime() {
        return mTime;
    }

    public void setTime(Time time) {
        mTime = time;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getID() {
        return mID;
    }

    public boolean isTurnedOn() {
        return mTurnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        mTurnedOn = turnedOn;
    }

}
