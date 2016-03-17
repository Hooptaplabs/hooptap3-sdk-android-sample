package com.hooptap.brandsampleaws.Badges;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.HTApplication;
import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapBadge;
import com.hooptap.sdkbrandclub.Models.HooptapFilter;
import com.hooptap.sdkbrandclub.Models.HooptapListResponse;
import com.hooptap.sdkbrandclub.Models.HooptapOptions;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BadgeList extends HooptapActivity {

    @Bind(R.id.gridview)
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.badges);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Badges");

        final ProgressDialog pd = Utils.showProgress("Loading Badges", BadgeList.this);
        HooptapApi.getBadges(HTApplication.getTinydb().getString("user_id"), new HooptapOptions(),
                new HooptapFilter(), new HooptapCallback<HooptapListResponse>() {
                    @Override
                    public void onSuccess(HooptapListResponse htResponse) {
                        gridView.setAdapter(new BadgesAdapter(BadgeList.this, htResponse.getItemArray()));
                        Utils.dismisProgres(pd);
                    }

                    @Override
                    public void onError(ResponseError responseError) {
                        Utils.createDialogError(BadgeList.this, responseError.getReason());
                        Utils.dismisProgres(pd);
                    }
                });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                HooptapBadge badge = (HooptapBadge) parent.getItemAtPosition(position);
                startActivity(new Intent(BadgeList.this, BadgeDetail.class).putExtra("badge", badge));
            }
        });

    }
}
