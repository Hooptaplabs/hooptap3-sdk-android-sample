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
import com.hooptap.sdkbrandclub.Models.HooptapQuest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 2/12/15.
 */
public class QuestListAdapter<T> extends BaseAdapter {
    ArrayList quests;
    Activity activity;

    public QuestListAdapter(Activity activity, ArrayList quests) {
        this.quests = quests;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return quests.size();
    }

    @Override
    public T getItem(int i) {
        return (T) quests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.listview_cell_quest, parent, false);
        }

        ImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView text = ViewHolder.get(convertView, R.id.text);
        TextView active = ViewHolder.get(convertView, R.id.active);
        LinearLayout element = ViewHolder.get(convertView, R.id.element);

        HooptapQuest quest = (HooptapQuest) getItem(position);

        if (quest.getName() != null)
            text.setText(quest.getName());
        if (quest.getImage() != null && !quest.getImage().equals(""))
            Picasso.with(activity).load(quest.getImage()).into(imagen);
        if (quest.isActive()) {
            active.setBackgroundColor(activity.getResources().getColor(R.color.active));
            active.setText("Active");
        } else {
            active.setBackgroundColor(activity.getResources().getColor(R.color.desactive));
            active.setText("Desactive");
        }
        if (quest.isFinished()) {
            element.setBackgroundColor(activity.getResources().getColor(R.color.caption));
            active.setBackgroundColor(activity.getResources().getColor(R.color.caption));
            active.setTextColor(activity.getResources().getColor(R.color.body));
            active.setText("Finalizada");
        }

        return convertView;
    }


}

