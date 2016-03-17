package com.hooptap.brandsampleaws.Actions;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.ViewHolder;
import com.hooptap.sdkbrandclub.Models.HooptapBadge;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;
import com.hooptap.sdkbrandclub.Models.HooptapPoint;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 2/12/15.
 */
public class ActionRewardsAdapter<T> extends BaseAdapter {
    ArrayList rewards;
    Activity activity;
    private ImageView imagen;
    private TextView text;

    public ActionRewardsAdapter(Activity activity, ArrayList rewards) {
        this.rewards = rewards;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return rewards.size();
    }

    @Override
    public T getItem(int i) {
        return (T) rewards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.listview_cell_feed, parent, false);
        }

        imagen = ViewHolder.get(convertView, R.id.image);
        text = ViewHolder.get(convertView, R.id.text);

        T action = getItem(position);
        switch (action.getClass().getSimpleName()){
            case "HooptapPoint":
                fillHooptapPoint((HooptapPoint) action);
                break;
            case "HooptapBadge":
                fillHooptapBadge((HooptapBadge) action);
                break;
            case "HooptapLevel":
                fillHooptapLevel((HooptapLevel) action);
                break;
        }

        return convertView;
    }

    private void fillHooptapPoint(HooptapPoint point){
        String url_image = point.getImage();
        if (url_image != null && !url_image.equals("")){
            Picasso.with(activity).load(url_image).into(imagen);
        }else{
            imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star));
        }
        text.setText("You win "+  point.getQuantity()+" "+ point.getName());
    }

    private void fillHooptapBadge(HooptapBadge badge){
        String url_image = badge.getImage();
        if (url_image != null && !url_image.equals("")){
            Picasso.with(activity).load(url_image).into(imagen);
        }else{
            imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_badge));
        }
        text.setText("You have won "+badge.getProgress()+"% off the badge "+badge.getName());
    }

    private void fillHooptapLevel(HooptapLevel level){
        String url_image = level.getImage();
        if (url_image != null && !url_image.equals("")){
            Picasso.with(activity).load(url_image).into(imagen);
        }else{
            imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_level));
        }
        text.setText("You up to "+ level.getName());
    }

}

