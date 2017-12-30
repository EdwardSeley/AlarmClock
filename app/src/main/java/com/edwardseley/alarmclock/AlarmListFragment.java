package com.edwardseley.alarmclock;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AlarmListFragment extends Fragment {

    RecyclerView mRecycleView;
    Adapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_list, container, false);
        mRecycleView = view.findViewById(R.id.recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(layoutManager);
        Adapter mAdapter = new Adapter(AlarmStorage.get(getActivity()).getAlarmList());
        mRecycleView.setAdapter(mAdapter);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mRecycleView.getContext(), DividerItemDecoration.VERTICAL
        );
        mRecycleView.addItemDecoration(mDividerItemDecoration);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.add_alarm:
                Alarm alarm = new Alarm();
                AlarmStorage.get(getActivity()).addAlarm(alarm);
                Intent intent = AlarmActivity.newIntent(getActivity(), alarm.getID());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGUI();
    }

    public void updateGUI()
    {
        List<Alarm> alarmList = AlarmStorage.get(getActivity()).getAlarmList();

        if (mAdapter != null)
        {
            mAdapter.setAlarms(alarmList);
            mAdapter.notifyDataSetChanged();
        }
        else {
            mAdapter = new Adapter(alarmList);
            mRecycleView.setAdapter(mAdapter);
        }
    }

    class Adapter extends RecyclerView.Adapter
    {
        private List<Alarm> mAlarms;

        private Adapter(List <Alarm> alarmList)
        {
            mAlarms = alarmList;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private ViewHolder(View itemView) {
                super(itemView);
            }
        }

        private void setAlarms(List <Alarm> alarms)
        {
            this.mAlarms = alarms;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View itemView = layoutInflater.inflate(R.layout.item_view, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            View view = holder.itemView;
            TextView timeView = view.findViewById(R.id.timeView);
            TextView dateView = view.findViewById(R.id.dateView);
            final Switch switchView = view.findViewById(R.id.switch_view);
            final Alarm alarm = mAlarms.get(position);
            switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    switchView.setChecked(b);
                    alarm.setTurnedOn(b);

                }
            });
            Date date = new Date(alarm.getTime().getTime());
            timeView.setText(DateFormat.format("h:mm a", date));
            dateView.setText(DateFormat.format("MM-dd-yyyy", mAlarms.get(position).getDate()));
            switchView.setChecked(alarm.isTurnedOn());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = AlarmActivity.newIntent(getActivity(), alarm.getID());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mAlarms.size();
        }
    }
}
