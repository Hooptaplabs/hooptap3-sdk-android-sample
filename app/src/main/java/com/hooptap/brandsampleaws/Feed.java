package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        HooptapApi.getUserFeed(HTApplication.getTinydb().getString("user_id"), new Options(), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                data = jsonObject.toString();
                try {
                    JSONObject data = jsonObject.getJSONObject("response");
                    JSONArray array = data.getJSONArray("items");
                    list.setEmptyView(findViewById(R.id.emptyElement));
                    if (array.length() > 0) {
                        UserAdapter userAdapter = new UserAdapter(Feed.this, array);
                        list.setAdapter(userAdapter);
                    }
                    Utils.dismisProgres(pd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Feed.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });
    }


}
