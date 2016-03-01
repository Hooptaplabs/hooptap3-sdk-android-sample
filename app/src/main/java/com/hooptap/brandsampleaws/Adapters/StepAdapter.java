package com.hooptap.brandsampleaws.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.hooptap.sdkbrandclub.Models.HooptapQuest;
import com.hooptap.sdkbrandclub.Models.HooptapStep;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 2/12/15.
 */
public class StepAdapter<T> extends BaseAdapter {
    private HooptapQuest quest;
    ArrayList elements;
    Activity activity;

    public StepAdapter(Activity activity, ArrayList elements, HooptapQuest quest) {
        this.elements = elements;
        this.activity = activity;
        this.quest = quest;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public T getItem(int i) {
        return (T) elements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.listview_cell_step, parent, false);
        }

        ImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView text = ViewHolder.get(convertView, R.id.text);
        TextView num_step = ViewHolder.get(convertView, R.id.num_step);
        LinearLayout element = ViewHolder.get(convertView, R.id.ll_element);

        HooptapStep step = (HooptapStep) getItem(position);

        if (step.getName() != null)
            text.setText(step.getName());
        if (step.getImage() != null && !step.getImage().equals(""))
            Picasso.with(activity).load(step.getImage()).into(imagen);
        else
            Picasso.with(activity).load((String) step.getImages().get(0)).into(imagen);
        num_step.setText(position + ")");

        cutomizeActualStep(position, element);

        return convertView;
    }

    private void cutomizeActualStep(int position, LinearLayout element) {
        Log.e("position", position+" / "+quest.getNumCompletedSteps());
        if (position != quest.getNumCompletedSteps()) {
            element.setBackgroundColor(activity.getResources().getColor(R.color.caption));
        }else{
            element.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }

    }

}

