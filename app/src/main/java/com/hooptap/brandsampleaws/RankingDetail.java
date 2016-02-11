package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Adapters.UserAdapter;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapItem;
import com.hooptap.sdkbrandclub.Models.HooptapOptions;
import com.hooptap.sdkbrandclub.Models.HooptapRanking;

import com.hooptap.sdkbrandclub.Models.ResponseError;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RankingDetail extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;
    private String ranking_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Ranking");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ranking_id = bundle.getString("id");
        } else {
            Toast.makeText(this, "Se ha producido un error, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            finish();
        }

        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HooptapItem item = (HooptapItem) adapterView.getItemAtPosition(i);
                startActivity(new Intent(RankingDetail.this, ItemDetail.class).putExtra("id", item.getIdentificator()));
            }
        });

        getRankings();
    }

    public void getRankings() {
        final ProgressDialog pd = Utils.showProgress("Loading Rankings", RankingDetail.this);
        HooptapApi.getRankingDetail(HTApplication.getTinydb().getString("user_id"), ranking_id, new HooptapOptions(), new HooptapCallback<HooptapRanking>() {
            @Override
            public void onSuccess(HooptapRanking ranking) {

                if (ranking.getUsers().size() > 0) {
                    UserAdapter userAdapter = new UserAdapter(RankingDetail.this, ranking.getUsers());
                    list.setAdapter(userAdapter);
                }
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(RankingDetail.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });


    }
}
