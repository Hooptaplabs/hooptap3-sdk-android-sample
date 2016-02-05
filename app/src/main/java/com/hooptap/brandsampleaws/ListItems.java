package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class ListItems extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("List Items");

        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject item = (JSONObject) adapterView.getItemAtPosition(i);
                try {
                    startActivity(new Intent(ListItems.this, DetailItem.class).putExtra("id", item.getString("_id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getItems();
    }

    public void getItems() {
        final ProgressDialog pd = Utils.showProgress("Loading Items", ListItems.this);
        HooptapApi.getItems(HTApplication.getTinydb().getString("user_id"), new Options(), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                try {
                    JSONObject data = jsonObject.getJSONObject("response");

                    JSONArray array = data.getJSONArray("items");
                    if (array.length() > 0) {
                        UserAdapter userAdapter = new UserAdapter(ListItems.this, array);
                        list.setAdapter(userAdapter);
                    }
                    Utils.dismisProgres(pd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(ListItems.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });


    }
}
