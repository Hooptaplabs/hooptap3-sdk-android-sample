package com.hooptap.brandsampleaws.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 2/12/15.
 */
public class LevelsAdapter<T> extends BaseAdapter {
    ArrayList objectos;
    Activity activity;

    public LevelsAdapter(Activity activity, ArrayList objectos) {
        this.objectos = objectos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return objectos.size();
    }

    @Override
    public T getItem(int i) {
        return (T) objectos.get(i);
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

        ImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView text = ViewHolder.get(convertView, R.id.text);
        LinearLayout bg = ViewHolder.get(convertView, R.id.element);

        HooptapLevel item = (HooptapLevel) getItem(position);

        if (!item.isPassed())
            bg.setBackgroundColor(activity.getResources().getColor(R.color.caption));
        if (item.getName() != null)
            text.setText(item.getName());
        if (item.getImage() != null && !item.getImage().equals(""))
            Picasso.with(activity).load(item.getImage()).into(imagen);

        return convertView;
    }


}

