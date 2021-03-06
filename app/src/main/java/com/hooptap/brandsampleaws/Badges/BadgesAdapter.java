package com.hooptap.brandsampleaws.Badges;

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
import com.hooptap.sdkbrandclub.Models.HooptapItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 16/12/15.
 */
public class BadgesAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<HooptapItem> badges;

    public BadgesAdapter(Activity activity, ArrayList<HooptapItem> badges) {
        this.activity = activity;
        this.badges = badges;
    }

    @Override
    public int getCount() {
        return badges.size();
    }

    @Override
    public HooptapBadge getItem(int i) {
        return (HooptapBadge) badges.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.badges_item, viewGroup, false);
        }

        ImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView texto = ViewHolder.get(convertView, R.id.texto);

        HooptapBadge badge = getItem(position);
        texto.setText(badge.getName());

        if (badge.getImage() != null && !badge.getImage().equals("")) {
            Picasso.with(activity).load(badge.getImage()).into(imagen);
        }

        return convertView;
    }
}
