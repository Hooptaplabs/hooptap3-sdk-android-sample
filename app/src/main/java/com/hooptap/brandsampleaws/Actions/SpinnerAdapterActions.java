package com.hooptap.brandsampleaws.Actions;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.hooptap.sdkbrandclub.Models.HooptapAction;

import java.util.ArrayList;

public class SpinnerAdapterActions extends BaseAdapter implements SpinnerAdapter {

    /**
     * The internal data (the ArrayList with the Objects).
     */
    private final ArrayList<HooptapAction> actions;
    private final Activity activity;

    public SpinnerAdapterActions(Activity activity, ArrayList<HooptapAction> actions) {
        this.actions = actions;
        this.activity = activity;
    }

    /**
     * Returns the Size of the ArrayList
     */
    @Override
    public int getCount() {
        return actions.size();
    }

    /**
     * Returns one Element of the ArrayList
     * at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return actions.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Returns the View that is shown when a element was
     * selected.
     */
    @Override
    public View getView(int position, View recycle, ViewGroup parent) {
        TextView text;
        if (recycle != null) {
            // Re-use the recycled view here!
            text = (TextView) recycle;
        } else {
            // No recycled view, inflate the "original" from the platform:
            text = (TextView) activity.getLayoutInflater().inflate(
                    android.R.layout.simple_spinner_dropdown_item, parent, false
            );
        }

        text.setText(actions.get(position).getName());
        return text;
    }


}