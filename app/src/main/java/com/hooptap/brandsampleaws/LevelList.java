package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Adapters.LevelsAdapter;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapFilter;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;
import com.hooptap.sdkbrandclub.Models.HooptapListResponse;
import com.hooptap.sdkbrandclub.Models.HooptapOptions;
import com.hooptap.sdkbrandclub.Models.ResponseError;
import com.hooptap.sdkbrandclub.Models.HooptapFilter.Builder.Order;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LevelList extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;
    private ProgressDialog pd;
    private ArrayList<HooptapLevel> levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Levels");

        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HooptapLevel level = (HooptapLevel) adapterView.getItemAtPosition(i);
                startActivity(new Intent(LevelList.this, LevelDetail.class).putExtra("level", level));
            }
        });

        loadLevels();
    }

    public void loadLevels() {
        pd = Utils.showProgress("Loading Levels", LevelList.this);

        HooptapFilter filter = new HooptapFilter.Builder()
                .sort("min_points", Order.asc)
                .build();

        HooptapApi.getLevels(new HooptapOptions(), filter, new HooptapCallback<HooptapListResponse>() {
            @Override
            public void onSuccess(HooptapListResponse response) {

                levels = response.getItemArray();

                if (levels.size() > 0) {
                    getLevelUser();
                }else{
                    Utils.dismisProgres(pd);
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.dismisProgres(pd);
                Utils.createDialogError(LevelList.this, responseError.getReason());
            }
        });


    }

    public void getLevelUser() {
        HooptapApi.getLevelDetail(HTApplication.getTinydb().getString("user_id"), new HooptapCallback<HooptapLevel>() {
            @Override
            public void onSuccess(HooptapLevel level) {
                for (int i = 0; i < levels.size(); i++) {
                    Log.e("LEVEL", "My " + level.getMin_points() + " Other " + levels.get(i).getMin_points());
                    if (level.getMin_points() >= levels.get(i).getMin_points()) {
                        levels.get(i).setPassed(true);
                    } else {
                        levels.get(i).setPassed(false);
                    }

                    LevelsAdapter userAdapter = new LevelsAdapter(LevelList.this, levels);
                    list.setAdapter(userAdapter);
                    Utils.dismisProgres(pd);
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(LevelList.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });


    }
}
