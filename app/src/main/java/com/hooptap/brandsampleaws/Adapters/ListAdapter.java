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
public class ListAdapter extends BaseAdapter {
    ArrayList<ImageView> objectos;
    ArrayList<String> lista= new ArrayList<>();
    Activity activity;

    public ListAdapter(Activity activity) {

        this.activity = activity;
        lista.add("Profile");
        lista.add("Levels");
        lista.add("Ranking");
        lista.add("Market");
        lista.add("Goods");
        lista.add("Buy");
        lista.add("Badges");
        lista.add("Actions");
        lista.add("Feed");
        lista.add("Play Game");
        lista.add("List Items");
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
                    .inflate(R.layout.lista, parent, false);
        }

        ImageView image = ViewHolder.get(convertView, R.id.image);
        TextView text = ViewHolder.get(convertView, R.id.text);
        LinearLayout background=ViewHolder.get(convertView, R.id.element);
        text.setText(lista.get(position));
        if(position%2==0)
            background.setBackgroundColor(activity.getResources().getColor(R.color.listaimpar));

        return convertView;
    }


}

