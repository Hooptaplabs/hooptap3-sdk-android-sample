package com.hooptap.brandsampleaws.Ranking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.ViewHolder;
import com.hooptap.sdkbrandclub.Models.HooptapRanking;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 2/12/15.
 */
public class RankingAdapter<T> extends BaseAdapter {
    ArrayList objects;
    Activity activity;

    public RankingAdapter(Activity activity, ArrayList objects) {
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
                    .inflate(R.layout.listview_cell_ranking, parent, false);
        }

        CircleImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView text = ViewHolder.get(convertView, R.id.text);
        TextView level_desc = ViewHolder.get(convertView, R.id.desc);

        HooptapRanking ranking = (HooptapRanking) getItem(position);

        if (ranking.getName() != null) {
            text.setText(ranking.getName());
        }

        if (ranking.getDescription() != null) {
            level_desc.setText(ranking.getDescription());
        }

        if (ranking.getImage() != null && !ranking.getImage().equals("")) {
            Picasso.with(activity).load(ranking.getImage()).into(imagen);
        }

        return convertView;
    }


}

