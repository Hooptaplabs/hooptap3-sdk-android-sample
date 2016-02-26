package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Adapters.UserAdapter;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapFilter;
import com.hooptap.sdkbrandclub.Models.HooptapItem;
import com.hooptap.sdkbrandclub.Models.HooptapListResponse;
import com.hooptap.sdkbrandclub.Models.HooptapOptions;
import com.hooptap.sdkbrandclub.Models.HooptapRanking;

import com.hooptap.sdkbrandclub.Models.ResponseError;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RankingDetail extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;
    private String ranking_id;
    private ImageView ranking_img;
    private TextView ranking_title;
    private TextView ranking_desc;

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

        addHeaderView();
        getRankingDetail();
        getRankingUsers();
    }

    private void addHeaderView(){
        View view = getLayoutInflater().inflate(R.layout.ranking_header, null, false);
        ranking_img = (ImageView) view.findViewById(R.id.ranking_img);
        ranking_title = (TextView) view.findViewById(R.id.ranking_title);
        ranking_desc = (TextView) view.findViewById(R.id.ranking_desc);

        list.addHeaderView(view);
    }

    public void getRankingUsers() {
        final ProgressDialog pd = Utils.showProgress("Loading Users", RankingDetail.this);
        HooptapApi.getRankingUsers(HTApplication.getTinydb().getString("user_id"), ranking_id, new HooptapOptions(), new HooptapFilter(), new HooptapCallback<HooptapListResponse>() {
            @Override
            public void onSuccess(HooptapListResponse listUsers) {

                if (listUsers.getItemArray().size() > 0) {
                    UserAdapter userAdapter = new UserAdapter(RankingDetail.this, listUsers.getItemArray());
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

    public void getRankingDetail() {
        final ProgressDialog pd = Utils.showProgress("Loading Rankings", RankingDetail.this);
        HooptapApi.getRankingDetail(ranking_id, new HooptapCallback<HooptapRanking>() {
            @Override
            public void onSuccess(HooptapRanking ranking) {
                if (ranking.getImage() != null || !ranking.getImage().equals("")) {
                    Picasso.with(getApplicationContext()).load(ranking.getImage()).into(ranking_img);
                }
                ranking_title.setText(ranking.getName());
                ranking_desc.setText(ranking.getDescription());

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
