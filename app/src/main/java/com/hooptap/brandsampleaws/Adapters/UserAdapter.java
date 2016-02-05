package com.hooptap.brandsampleaws.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hooptap.brandsampleaws.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 2/12/15.
 */
public class UserAdapter extends BaseAdapter {
    JSONArray objectos;
    Activity activity;

    public UserAdapter(Activity activity, JSONArray objectos) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.lista, parent, false);
        }

        ImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView phoneView = ViewHolder.get(convertView, R.id.text);
        TextView points = ViewHolder.get(convertView, R.id.points);
        JSONObject datos = getItem(position);
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

