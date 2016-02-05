package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Adapters.GoodAdapter;
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

/**
 * Created by root on 15/12/15.
 */
public class Goods extends HooptapActivity {
    @Bind(R.id.lista)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Goods");
        list.setEmptyView(findViewById(R.id.emptyElement));
        getGoods();
    }

    public void getGoods() {
        final ProgressDialog pd = Utils.showProgress("Loading Goods", Goods.this);
        HooptapApi.getMarketPlace(HTApplication.getTinydb().getString("user_id"), new Options(), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                data = jsonObject.toString();
                try {
                    JSONObject data = jsonObject.getJSONObject("response");

                    JSONArray array = data.getJSONArray("items");
                    if (array.length() > 0) {
                        GoodAdapter userAdapter = new GoodAdapter(Goods.this, array);
                        list.setAdapter(userAdapter);
                    } else {
                        Utils.createDialog(Goods.this, "There aren\'t any elements at this moment");
                    }
                    Utils.dismisProgres(pd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Goods.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });
    }
}
