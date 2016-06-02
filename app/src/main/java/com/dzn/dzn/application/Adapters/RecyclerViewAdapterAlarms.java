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
import android.widget.TextView;

import com.dzn.dzn.application.Activities.CreateSelfieActivity;
import com.dzn.dzn.application.Activities.NewEditActivity;
import com.dzn.dzn.application.MainActivity;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.DateTimeOperator;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhenya on 14.05.2016.
 */
public class RecyclerViewAdapterAlarms extends RecyclerView.Adapter<RecyclerViewAdapterAlarms.ViewHolder> {
    private static final String TAG = "RVAdapterAlarms";
    private Context context;
    private Alarm alarm;
    private ArrayList<Alarm> list;

    public RecyclerViewAdapterAlarms(Context context, ArrayList<Alarm> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        alarm = list.get(position);
        String s = DateTimeOperator.dateToTimeString(alarm.getDate());

        holder.tvAlarmTime.setText(s);
        holder.tvAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm al = list.get(position);
                Intent intent = new Intent(context, NewEditActivity.class);
                intent.putExtra("idAlarm", al.getID());
                context.startActivity(intent);
            }
        });

        holder.btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm al = list.get(position);
                Intent intent = new Intent(context, NewEditActivity.class);
                intent.putExtra("idAlarm", al.getID());
                context.startActivity(intent);
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
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, alarm.getID(), intent, PendingIntent.FLAG_ONE_SHOT);
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

        Log.d(TAG, "Date: " + s);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Class is like the helper
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnDelete;
        public TextView tvAlarmTime;
        public Button btnAlarm;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAlarmTime = (TextView) itemView.findViewById(R.id.tvAlarmTime);
            PFHandbookProTypeFaces.BLACK.apply(tvAlarmTime);

            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnAlarm = (Button) itemView.findViewById(R.id.btnAlarm);
        }
    }
}
