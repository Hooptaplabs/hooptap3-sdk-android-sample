package com.hooptap.brandsampleaws.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.hooptap.sdkbrandclub.Models.HooptapUser;
import com.hooptap.sdkbrandclub.Models.HooptapUserRanking;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 2/12/15.
 */
public class UserAdapter extends BaseAdapter {
    ArrayList users;
    Activity activity;

    public UserAdapter(Activity activity, ArrayList users) {
        this.users = users;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public HooptapUser getItem(int i) {
        return (HooptapUser) users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.user_list, parent, false);
        }

        ImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView text = ViewHolder.get(convertView, R.id.text);
        TextView points = ViewHolder.get(convertView, R.id.points);
        HooptapUser user = getItem(position);

        if (user.getUsername() != null)
            text.setText(user.getUsername());
        if (user.getImage() != null && !user.getImage().equals(""))
            Picasso.with(activity).load(user.getImage()).into(imagen);

        if(user instanceof HooptapUserRanking){
            points.setVisibility(View.VISIBLE);
            points.setText(((HooptapUserRanking)user).getMark()+"");
        }

        return convertView;
    }


}

