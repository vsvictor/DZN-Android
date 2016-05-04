package com.dzn.dzn.application.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhenya on 04.05.2016.
 */
public class SpinnerRepeatAdapter extends ArrayAdapter<String> {
    private static final String TAG = "SpinnerRepeatAdapter";

    private Typeface font;

    public SpinnerRepeatAdapter(Context context, int resource) {
        super(context, resource);
        font = Typeface.createFromAsset(context.getAssets(), "fonts/PFHandbookPro-Thin.ttf");
    }

    public SpinnerRepeatAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        font = Typeface.createFromAsset(context.getAssets(), "fonts/PFHandbookPro-Thin.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }
}
