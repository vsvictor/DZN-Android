package com.dzn.dzn.application.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.AlarmTest;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DateTimeOperator;

import java.util.ArrayList;

/**
 * Created by zhenya on 06.05.2016.
 */
public class RecyclerViewAdapterMain extends RecyclerView.Adapter<RecyclerViewAdapterMain.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterMain";

    private ArrayList<?> list;

    public RecyclerViewAdapterMain(ArrayList<Alarm> list) {
        this.list = list;
    }

    @Override
    public RecyclerViewAdapterMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_main, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterMain.ViewHolder holder, int position) {
        Alarm alarm = (Alarm) list.get(position);
        String s = DateTimeOperator.dateToTimeString(alarm.getDate());
        String[] ss = s.split(":");
        holder.tvHour.setText(ss[0]);
        holder.tvMinute.setText(ss[1]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Class is like the helper
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHour;
        public TextView tvColon;
        public TextView tvMinute;

        public ViewHolder(View itemView) {
            super(itemView);
            tvHour = (TextView) itemView.findViewById(R.id.tvHour);
            tvHour.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/PFHandbookPro-Medium.ttf"));
            tvColon = (TextView) itemView.findViewById(R.id.tvColon);
            tvColon.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
            tvMinute = (TextView) itemView.findViewById(R.id.tvMinute);
            tvMinute.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/PFHandbookPro-Medium.ttf"));
        }
    }
}
