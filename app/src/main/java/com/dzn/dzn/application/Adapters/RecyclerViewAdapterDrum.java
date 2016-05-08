package com.dzn.dzn.application.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzn.dzn.application.Objects.AlarmTest;
import com.dzn.dzn.application.R;

import java.util.ArrayList;

/**
 * Created by zhenya on 08.05.2016.
 */
public class RecyclerViewAdapterDrum extends RecyclerView.Adapter<RecyclerViewAdapterDrum.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterDrum";

    private ArrayList<?> list;

    public RecyclerViewAdapterDrum(ArrayList<AlarmTest> list) {
        ArrayList<AlarmTest> newList = new ArrayList<AlarmTest>();
        AlarmTest empty = new AlarmTest();
        empty.setVisibleTime(false);
        switch (list.size()){
            case 0:{
                newList.add(empty);
                newList.add(empty);
                newList.add(new AlarmTest());
                newList.add(empty);
                newList.add(empty);
                break;
            }
            case 1:{
                newList.add(empty);
                newList.add(empty);
                newList.add((AlarmTest) list.get(0));
                newList.add(empty);
                newList.add(empty);
                break;
            }
            case 2:{
                newList.add(empty);
                newList.add((AlarmTest) list.get(0));
                newList.add((AlarmTest) list.get(1));
                newList.add(empty);
                newList.add(empty);
                break;
            }
            case 3:{
                newList.add(empty);
                newList.add((AlarmTest) list.get(0));
                newList.add((AlarmTest) list.get(1));
                newList.add((AlarmTest) list.get(2));
                newList.add(empty);
                break;
            }
            case 4:{
                newList.add((AlarmTest) list.get(0));
                newList.add((AlarmTest) list.get(1));
                newList.add((AlarmTest) list.get(2));
                newList.add((AlarmTest) list.get(3));
                newList.add(empty);
            }
            default:{
                for (AlarmTest al : list) {
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
        AlarmTest alarmTest = (AlarmTest) list.get(position);
        holder.tvTime.setText(alarmTest.getTime());

        /**
        if (list.size() == 1) {
            holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.drumUpDivider.setVisibility(View.VISIBLE);
            holder.drumDownDivider.setVisibility(View.VISIBLE);
            holder.tvTime.setTextSize(46);
            holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Black.ttf"));
        } else if (list.size() == 2 || list.size() == 3) {
            if (position == 1) {
                holder.drumUpDivider.setVisibility(View.VISIBLE);
                holder.drumDownDivider.setVisibility(View.VISIBLE);
                holder.tvTime.setTextSize(46);
                holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Black.ttf"));
            } else {
                holder.linearLayout.setGravity(Gravity.CENTER);
                holder.tvTime.setTextSize(36);
                holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
                holder.tvTime.setTextColor(holder.itemView.getResources().getColor(R.color.colorNewEditPastLastTime));
            }
        } else
         */
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
        if (alarmTest.getVisibleTime()) {
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
