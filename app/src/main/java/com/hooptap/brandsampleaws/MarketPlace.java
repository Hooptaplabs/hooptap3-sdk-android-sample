package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Adapters.UserAdapter;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.Options;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MarketPlace extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        list.setEmptyView(findViewById(R.id.emptyElement));
        getMarketPlace();
    }

    public void getMarketPlace() {
        final ProgressDialog pd = Utils.showProgress("Loading Marketplace", MarketPlace.this);
        HooptapApi.getMarketPlace(HTApplication.getTinydb().getString("user_id"), new Options(), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                data = jsonObject.toString();
                try {
                    JSONObject data = jsonObject.getJSONObject("response");

                    JSONArray array = data.getJSONArray("items");
                    if (array.length() > 0) {
                        UserAdapter userAdapter = new UserAdapter(MarketPlace.this, array);
                        list.setAdapter(userAdapter);
                    } else {
                        Utils.createDialog(MarketPlace.this, "There aren\'t any elements at this moment");
                    }
                    Utils.dismisProgres(pd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(MarketPlace.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });
    }
}
