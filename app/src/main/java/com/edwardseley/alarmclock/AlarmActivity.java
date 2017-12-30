package com.edwardseley.alarmclock;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class AlarmActivity extends AppCompatActivity {

    private static final String EXTRA_TAG = "com.edwardseley.alarmclock.AlarmActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_TAG);
        Fragment fragment = AlarmFragment.newInstance(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_alarm_container, fragment).commit();
    }

    public static Intent newIntent(Context packageContext, UUID uuid)
    {
        Intent intent = new Intent(packageContext, AlarmActivity.class);
        intent.putExtra(EXTRA_TAG, uuid);
        return intent;
    }
}
