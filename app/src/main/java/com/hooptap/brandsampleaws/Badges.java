package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hooptap.brandsampleaws.Adapters.BadgesAdapter;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapListResponse;
import com.hooptap.sdkbrandclub.Models.Options;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Badges extends HooptapActivity {

    @Bind(R.id.gridview)
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.badges);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Badges");

        final ProgressDialog pd = Utils.showProgress("Loading Goods", Badges.this);
        HooptapApi.getBadges(HTApplication.getTinydb().getString("user_id"), new Options(),
                new HooptapCallback<HooptapListResponse>() {
                    @Override
                    public void onSuccess(HooptapListResponse htResponse) {
                        Log.e("BADGES",htResponse.getItemArray().size()+" /");
                        gridView.setAdapter(new BadgesAdapter(Badges.this, htResponse.getItemArray()));
                        /*data = jsonObject.toString();
                        try {
                            JSONArray array = jsonObject.getJSONArray("response");
                            gridView.setAdapter(new BadgesAdapter(Badges.this, array));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        Utils.dismisProgres(pd);
                    }

                    @Override
                    public void onError(ResponseError responseError) {
                        Utils.createDialogError(Badges.this, responseError.getReason());
                        Utils.dismisProgres(pd);
                    }
                });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(Badges.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
