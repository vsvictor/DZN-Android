package com.dzn.dzn.application.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DateTimeOperator;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.ArrayList;

/**
 * Created by zhenya on 14.05.2016.
 */
public class RecyclerViewAdapterOnOff extends RecyclerView.Adapter<RecyclerViewAdapterOnOff.ViewHolder> {
    private static final String TAG = "RVAdapterOnOff";

    private ArrayList<?> list;

    public RecyclerViewAdapterOnOff(ArrayList<Alarm> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_on_off_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alarm alarm = (Alarm) list.get(position);
        String s = DateTimeOperator.dateToTimeString(alarm.getDate());
        holder.tvAlarmTime.setText(s);
        Log.d(TAG, "Date: " + s);
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
