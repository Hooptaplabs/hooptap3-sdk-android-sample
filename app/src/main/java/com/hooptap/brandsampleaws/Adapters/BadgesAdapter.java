package com.hooptap.brandsampleaws.Adapters;

import android.app.Activity;
import android.util.Log;
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
 * Created by root on 16/12/15.
 */
public class BadgesAdapter extends BaseAdapter{

    Activity activity;
    JSONArray objetos;

    public BadgesAdapter(Activity activity, JSONArray objetos) {
        this.activity=activity;
        this.objetos=objetos;
    }

    @Override
    public int getCount() {
        return objetos.length();
    }

    @Override
    public JSONObject getItem(int i) {
        try {
            return objetos.getJSONObject(i);
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.badges_item, viewGroup, false);
        }

        ImageView imagen = ViewHolder.get(convertView, R.id.image);
        TextView texto = ViewHolder.get(convertView, R.id.texto);

        JSONObject datos = getItem(position);
        try {
            texto.setText(datos.getString("name"));
            Picasso.with(activity).load(datos.getString("image_on")).into(imagen);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
