package com.hooptap.brandsampleaws.Levels;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.ViewHolder;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 2/12/15.
 */
public class LevelsAdapter<T> extends BaseAdapter {
    ArrayList objects;
    Activity activity;

    public LevelsAdapter(Activity activity, ArrayList objects) {
        this.objects = objects;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public T getItem(int i) {
        return (T) objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.listview_cell_levels, parent, false);
        }

        CircleImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView level_text = ViewHolder.get(convertView, R.id.text);
        TextView level_desc = ViewHolder.get(convertView, R.id.desc);
        TextView level_completed = ViewHolder.get(convertView, R.id.level_completed);

        HooptapLevel level = (HooptapLevel) getItem(position);

        if (level.isPassed()) {
            level_completed.setTextColor(activity.getResources().getColor(R.color.active));
            level_completed.setText(activity.getResources().getString(R.string.completed));
        } else {
            level_completed.setText(activity.getResources().getString(R.string.uncompleted));
        }

        if (level.getDescription() != null){
            level_desc.setText(level.getDescription());
        }

        if (level.getName() != null) {
            level_text.setText(level.getName());
        }

        if (level.getImage() != null && !level.getImage().equals("")) {
            Picasso.with(activity).load(level.getImage()).into(imagen);
        }

        return convertView;
    }


}

