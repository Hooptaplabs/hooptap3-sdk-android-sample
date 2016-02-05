package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

public class Levels extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Levels");
        list.setEmptyView(findViewById(R.id.emptyElement));

        loadLevels();
    }

    public void loadLevels() {
        final ProgressDialog pd = Utils.showProgress("Loading rewards", Levels.this);
        HooptapApi.getLevels(HTApplication.getTinydb().getString("user_id"), new Options(), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                data = jsonObject.toString();
                try {
                    JSONObject user = jsonObject.getJSONObject("response");

                    JSONArray array = new JSONArray();
                    array.put(user.getJSONObject("current"));
                    if (!user.isNull("next"))
                        array.put(user.getJSONObject("next"));
                    if (array.length() > 0) {
                        Log.e("listado", array + "");
                        UserAdapter userAdapter = new UserAdapter(Levels.this, array);
                        list.setAdapter(userAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.dismisProgres(pd);
                Utils.createDialogError(Levels.this, responseError.getReason());
            }
        });


    }
}
