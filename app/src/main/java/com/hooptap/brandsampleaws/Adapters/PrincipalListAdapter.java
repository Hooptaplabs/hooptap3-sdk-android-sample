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

import java.util.ArrayList;

/**
 * Created by root on 15/12/15.
 */
public class PrincipalListAdapter extends BaseAdapter {
    ArrayList<ImageView> objectos;
    ArrayList<String> lista= new ArrayList<>();
    Activity activity;

    public PrincipalListAdapter(Activity activity) {

        this.activity = activity;
        lista.add("Profile");
        lista.add("Update User");
        lista.add("Levels");
        lista.add("Ranking");
        lista.add("Badges");
        lista.add("Actions");
        lista.add("Feed");
        lista.add("Quests");
        lista.add("List Items");
        lista.add("Detalles");
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public String getItem(int i) {

        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.listview_cell, parent, false);
        }

        ImageView image = ViewHolder.get(convertView, R.id.image);
        TextView text = ViewHolder.get(convertView, R.id.text);
        text.setText(lista.get(position));

        return convertView;
    }


}

