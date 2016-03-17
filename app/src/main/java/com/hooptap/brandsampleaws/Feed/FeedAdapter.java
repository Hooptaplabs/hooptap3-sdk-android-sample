package com.hooptap.brandsampleaws.Feed;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.ViewHolder;
import com.hooptap.sdkbrandclub.Models.HooptapBadge;
import com.hooptap.sdkbrandclub.Models.HooptapFeed;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;
import com.hooptap.sdkbrandclub.Models.HooptapPoint;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 2/12/15.
 */
public class FeedAdapter<T> extends BaseAdapter {
    ArrayList rewards;
    Activity activity;
    private ImageView imagen;
    private TextView text;

    public FeedAdapter(Activity activity, ArrayList rewards) {
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

        HooptapFeed reward = (HooptapFeed) getItem(position);
        Log.e("REWARD",reward+" / "+reward.getFeed());
        switch (reward.getFeed().getClass().getSimpleName()){
            case "HooptapPoint":
                fillHooptapPoint(reward);
                break;
            case "HooptapBadge":
                fillHooptapBadge(reward);
                break;
            case "HooptapLevel":
                fillHooptapLevel(reward);
                break;
            case "HooptapUser":
                fillHooptapUser(reward);
                break;
        }

        return convertView;
    }

    private void fillHooptapPoint(HooptapFeed pointFeed){
        HooptapPoint point = ((HooptapPoint) pointFeed.getFeed());
        String url_image = point.getImage();
        if (url_image != null && !url_image.equals("")){
            Picasso.with(activity).load(url_image).into(imagen);
        }else{
            imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star));
        }
        text.setText("You "+ pointFeed.getReason()+" "+ point.getQuantity()+" "+ point.getType());
    }

    private void fillHooptapBadge(HooptapFeed badgeFeed){
        HooptapBadge badge = (HooptapBadge) badgeFeed.getFeed();
        String url_image = badge.getImage();
        if (url_image != null && !url_image.equals("")){
            Picasso.with(activity).load(url_image).into(imagen);
        }else{
            imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_badge));
        }
        text.setText("You "+ badgeFeed.getReason()+" the badge "+badge.getName());
    }

    private void fillHooptapLevel(HooptapFeed levelFeed){
        HooptapLevel level = (HooptapLevel) levelFeed.getFeed();
        String url_image = level.getImage();
        if (url_image != null && !url_image.equals("")){
            Picasso.with(activity).load(url_image).into(imagen);
        }else{
            imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_level));
        }
        text.setText("You "+ levelFeed.getReason()+" to "+ level.getName());
    }

    private void fillHooptapUser(HooptapFeed userFeed){
        String url_image = ((HooptapPoint) userFeed.getFeed()).getImage();
        if (url_image != null && !url_image.equals("")){
            Picasso.with(activity).load(url_image).into(imagen);
        }else{
            imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.tapface));
        }
        text.setText(userFeed.getReason()+" "+userFeed.getReason_type()+" ");
    }

}

