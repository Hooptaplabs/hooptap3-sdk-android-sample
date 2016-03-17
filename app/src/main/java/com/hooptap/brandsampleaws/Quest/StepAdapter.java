package com.hooptap.brandsampleaws.Quest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.ViewHolder;
import com.hooptap.sdkbrandclub.Models.HooptapQuest;
import com.hooptap.sdkbrandclub.Models.HooptapStep;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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

        CircleImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView text = ViewHolder.get(convertView, R.id.text);
        TextView desc = ViewHolder.get(convertView, R.id.desc);
        TextView completed = ViewHolder.get(convertView, R.id.completed);

        HooptapStep step = (HooptapStep) getItem(position);

        if (step.getName() != null) {
            text.setText(step.getName());
        }

        if (step.getDescription() != null) {
            desc.setText(step.getDescription());
        }

        if (step.getImage() != null && !step.getImage().equals("")) {
            Picasso.with(activity).load(step.getImage()).into(imagen);
        }

        if (position != quest.getNumCompletedSteps()) {
            completed.setTextColor(activity.getResources().getColor(R.color.body));
            completed.setText(activity.getResources().getString(R.string.locked));
        } else {
            completed.setTextColor(activity.getResources().getColor(R.color.active));
            completed.setText(activity.getResources().getString(R.string.doing));
        }

        return convertView;
    }

}

