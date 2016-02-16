package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Adapters.ItemsAdapter;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapListResponse;
import com.hooptap.sdkbrandclub.Models.HooptapOptions;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 18/12/15.
 */
public class Feed extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Feed");

        getFeedUser();
    }

    public void getFeedUser() {
        final ProgressDialog pd = Utils.showProgress("Loading User Feed", Feed.this);
        HooptapApi.getUserFeed(HTApplication.getTinydb().getString("user_id"), new HooptapOptions(), new HooptapCallback<HooptapListResponse>() {
            @Override
            public void onSuccess(HooptapListResponse htResponse) {
                    list.setEmptyView(findViewById(R.id.emptyElement));
                    if (htResponse.getItemArray().size() > 0) {
                        ItemsAdapter userAdapter = new ItemsAdapter(Feed.this, htResponse.getItemArray());
                        list.setAdapter(userAdapter);
                    }
                    Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Feed.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });
    }


}
