package com.dzn.dzn.application.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dzn.dzn.application.Activities.CreateSelfieActivity;
import com.dzn.dzn.application.Activities.NewEditActivity;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhenya on 06.05.2016.
 */
public class RecyclerViewAdapterMain extends RecyclerView.Adapter<RecyclerViewAdapterMain.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterMain";

    private Context context;
    private ArrayList<Alarm> list;
    private DataBaseHelper dataBaseHelper;

    public RecyclerViewAdapterMain(ArrayList<Alarm> list) {
        this.list = list;
    }

    public RecyclerViewAdapterMain(Context context, ArrayList<Alarm> list) {
        this.context = context;
        this.list = list;
    }

    public RecyclerViewAdapterMain(Context context, ArrayList<Alarm> list, DataBaseHelper dataBaseHelper) {
        this.context = context;
        this.list = list;
        this.dataBaseHelper = dataBaseHelper;
    }

    @Override
    public RecyclerViewAdapterMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_main, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterMain.ViewHolder holder, final int position) {
        final Alarm alarm = list.get(position);
        holder.tvAlarmTime.setText(alarm.getTime());
        holder.tvAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm al = list.get(position);
                Intent intent = new Intent(context, NewEditActivity.class);
                intent.putExtra("idAlarm", al.getID());
                context.startActivity(intent);
            }
        });

        holder.toggleAlarmOnOff.setChecked(alarm.isTurnOn());
        holder.toggleAlarmOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setTurnOn(isChecked);
                dataBaseHelper.addAlarm(alarm);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "Position: " + position);
                DataBaseHelper db = DataBaseHelper.getInstance(context);
                Alarm alarm = list.get(position);
                if (alarm.isTurnOn()) {
                    alarm.setTurnOn(false);

                    // Cancel pendingIntent
                    Date d = alarm.getDate();
                    Date today = Calendar.getInstance().getTime();
                    today.setHours(d.getHours());
                    today.setMinutes(d.getMinutes());
                    today.setSeconds(0);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, CreateSelfieActivity.class);
                    intent.putExtra("id", alarm.getID());
                    intent.putExtra("time", today.getTime());
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, alarm.getID(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, today.getTime(), pendingIntent);
                    pendingIntent.cancel();
                    alarmManager.cancel(pendingIntent);

                }
                if (db != null) {
                    db.removeAlarm(alarm);
                } else {
                    Log.d(TAG, "database null");
                }
                list.remove(position);
                notifyDataSetChanged();
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
        public Button btnDelete;
        public TextView tvAlarmTime;
        public ToggleButton toggleAlarmOnOff;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAlarmTime = (TextView) itemView.findViewById(R.id.tvAlarmTime);
            PFHandbookProTypeFaces.THIN.apply(tvAlarmTime);

            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            toggleAlarmOnOff = (ToggleButton) itemView.findViewById(R.id.toggleAlarmOnOff);
        }
    }
}
