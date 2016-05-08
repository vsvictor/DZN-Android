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

    public RecyclerViewAdapterDrum(ArrayList<?> list) {
        this.list = list;
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

        if (list.size() == 1) {
            holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.linearLayout.setGravity(Gravity.CENTER);
            holder.drumUpDivider.setVisibility(View.VISIBLE);
            holder.drumDownDivider.setVisibility(View.VISIBLE);
            holder.tvTime.setTextSize(42);
            holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Black.ttf"));
        } else if (list.size() == 2 || list.size() == 3) {
            if (position == 1) {
                //holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.linearLayout.setGravity(Gravity.CENTER);
                holder.drumUpDivider.setVisibility(View.VISIBLE);
                holder.drumDownDivider.setVisibility(View.VISIBLE);
                holder.tvTime.setTextSize(42);
                holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Black.ttf"));
            } else {
                holder.linearLayout.setGravity(Gravity.CENTER);
                holder.tvTime.setTextSize(32);
                holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
                holder.tvTime.setTextColor(holder.itemView.getResources().getColor(R.color.colorNewEditPastLastTime));
            }
        } else if (list.size() >= 4) {
            if (position == 2) {
                //holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.drumUpDivider.setVisibility(View.VISIBLE);
                holder.drumDownDivider.setVisibility(View.VISIBLE);
                holder.tvTime.setTextSize(46);
                holder.tvTime.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/PFHandbookPro-Black.ttf"));
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
