package com.edwardseley.alarmclock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AlarmFragment extends Fragment {

    private static final String BUNDLE_TAG = "com.edwardseley.alarmclock.AlarmFragment";
    Button mDateButton;
    TimePicker mClock;
    Switch mSwitch;
    Alarm mAlarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getArguments().getSerializable(BUNDLE_TAG);
        mAlarm = AlarmStorage.get(getActivity()).getAlarm(uuid);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.alarm_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_alarm:
                AlarmStorage.get(getActivity()).removeAlarm(mAlarm);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        mDateButton = view.findViewById(R.id.date_button);
        mClock = view.findViewById(R.id.timePicker);
        mSwitch = view.findViewById(R.id.on_switch);
        mSwitch.setChecked(mAlarm.isTurnedOn());
        mDateButton.setText("Date: " + DateFormat.format("MM-dd-yyyy", mAlarm.getDate()));
        Date date = mAlarm.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        mClock.setHour(hours);
        mClock.setMinute(minutes);
        Log.d("CLOCK SET", "hours: " + hours + ", minutes: " + minutes);

        mClock.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                Time time = new Time(timePicker.getHour(), timePicker.getMinute(), 0);
                mAlarm.setTime(time);
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAlarm.setTurnedOn(b);
            }
        });

        return view;
    }

    public static AlarmFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_TAG, uuid);
        AlarmFragment fragment = new AlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAlarm != null)
            AlarmStorage.get(getActivity()).updateAlarm(mAlarm);
    }
}
