package com.hooptap.brandsampleaws.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hooptap.brandsampleaws.Buy;
import com.hooptap.brandsampleaws.R;
import com.hooptap.sdkbrandclub.Utilities.Log;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 2/12/15.
 */
public class GoodAdapter extends BaseAdapter {
    JSONArray objectos;
    Activity activity;

    public GoodAdapter(Activity activity, JSONArray objectos) {
        this.objectos = objectos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return objectos.length();
    }

    @Override
    public JSONObject getItem(int i) {
        try {
            return objectos.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.good_list, parent, false);
        }

        ImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView phoneView = ViewHolder.get(convertView, R.id.text);
        TextView points = ViewHolder.get(convertView, R.id.points);
        Button buy_btn = ViewHolder.get(convertView, R.id.buy_button);
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("ID",getItem(position).getString("_id"));
                    activity.startActivity(new Intent(activity, Buy.class).putExtra("id", getItem(position).getString("_id")));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        final JSONObject datos = getItem(position);
        try {
            if (!datos.isNull("username"))
                phoneView.setText(datos.getString("username"));
            else
                phoneView.setText(datos.getString("name"));

            if (!datos.isNull("mark")) {
                points.setText(datos.getString("mark") + " Points");
                points.setVisibility(View.VISIBLE);
            }
            Picasso.with(activity).load(datos.getString("image")).into(imagen);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }


}

