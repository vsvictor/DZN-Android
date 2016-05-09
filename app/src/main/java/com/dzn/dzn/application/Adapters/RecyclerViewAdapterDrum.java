package com.dzn.dzn.application.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;

import java.util.ArrayList;

/**
 * Created by zhenya on 08.05.2016.
 */
public class RecyclerViewAdapterDrum extends RecyclerView.Adapter<RecyclerViewAdapterDrum.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterDrum";

    private ArrayList<Alarm> list;

    public RecyclerViewAdapterDrum(ArrayList<Alarm> list) {
        ArrayList<Alarm> newList = new ArrayList<Alarm>();
        Alarm empty = new Alarm();
        empty.setVisible(false);
        switch (list.size()){
            case 0:{
                newList.add(empty);
                newList.add(empty);
                newList.add(new Alarm());
                newList.add(empty);
                newList.add(empty);
                break;
            }
            case 1:{
                newList.add(empty);
                newList.add(empty);
                newList.add(list.get(0));
                newList.add(empty);
                newList.add(empty);
                break;
            }
            case 2:{
                newList.add(empty);
                newList.add(list.get(0));
                newList.add(list.get(1));
                newList.add(empty);
                newList.add(empty);
                break;
            }
            case 3:{
                newList.add(empty);
                newList.add(list.get(0));
                newList.add(list.get(1));
                newList.add(list.get(2));
                newList.add(empty);
                break;
            }
            case 4:{
                newList.add(list.get(0));
                newList.add(list.get(1));
                newList.add(list.get(2));
                newList.add(list.get(3));
                newList.add(empty);
            }
            default:{
                for (Alarm al : list) {
                    newList.add(al);
                }
            }
        }
        this.list = newList;
    }

    @Override
    public RecyclerViewAdapterDrum.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drum, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterDrum.ViewHolder holder, int position) {
        Log.d(TAG, "position:" + position);
        Alarm alarm = list.get(position);
        holder.tvTime.setText(alarm.getTime());

        if (list.size() >= 4) {
            if (position == 2) {
                holder.drumUpDivider.setVisibility(View.VISIBLE);
                holder.drumDownDivider.setVisibility(View.VISIBLE);
                holder.tvTime.setTextSize(46);
                holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Black.ttf"));
                holder.tvTime.setTextColor(holder.itemView.getResources().getColor(R.color.colorWhite));
            } else if (position == 1 || position == 3) {
                holder.tvTime.setTextSize(36);
                holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
                holder.tvTime.setTextColor(holder.itemView.getResources().getColor(R.color.colorNewEditPastLastTime));
            } else {
                holder.tvTime.setTextSize(24);
                holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
                holder.tvTime.setTextColor(holder.itemView.getResources().getColor(R.color.colorNewEditPastLastTime));
            }
        }

        //fix filling
        if (alarm.isVisible()) {
            holder.tvTime.setVisibility(View.VISIBLE);
        } else {
            holder.tvTime.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Class is like the helper
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public View drumUpDivider;
        public View drumDownDivider;
        public TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.llDrumItem);
            drumUpDivider = itemView.findViewById(R.id.drumUpDivider);
            drumDownDivider = itemView.findViewById(R.id.drumDownDivider);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
