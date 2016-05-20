package com.dzn.dzn.application.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.DateTimeOperator;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.ArrayList;

/**
 * Created by zhenya on 14.05.2016.
 */
public class RecyclerViewAdapterOnOff extends RecyclerView.Adapter<RecyclerViewAdapterOnOff.ViewHolder> {
    private static final String TAG = "RVAdapterOnOff";

    private DataBaseHelper dataBaseHelper;
    private ArrayList<?> list;

    public RecyclerViewAdapterOnOff(ArrayList<Alarm> list) {
        this.list = list;
    }

    public RecyclerViewAdapterOnOff(ArrayList<Alarm> list, DataBaseHelper dataBaseHelper) {
        this.list = list;
        this.dataBaseHelper = dataBaseHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_on_off_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Alarm alarm = (Alarm) list.get(position);
        String s = DateTimeOperator.dateToTimeString(alarm.getDate());
        holder.tvAlarmTime.setText(s);
        Log.d(TAG, "Date: " + s);

        holder.toggleAlarmOnOff.setChecked(alarm.isTurnOn());
        holder.toggleAlarmOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setTurnOn(isChecked);
                dataBaseHelper.addAlarm(alarm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * Class is like the helper
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAlarmTime;
        public ToggleButton toggleAlarmOnOff;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAlarmTime = (TextView) itemView.findViewById(R.id.tvAlarmTime);
            PFHandbookProTypeFaces.BLACK.apply(tvAlarmTime);

            toggleAlarmOnOff = (ToggleButton) itemView.findViewById(R.id.toggleAlarmOnOff);
        }
    }
}
