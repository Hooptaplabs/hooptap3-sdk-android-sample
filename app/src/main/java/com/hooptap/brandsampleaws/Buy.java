package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Buy extends HooptapActivity {

    @Bind(R.id.good)
    EditText good;
    private String good_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Buy");
        setContentView(R.layout.buy_good);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            good_id = bundle.getString("id");
            good.setText(good_id);
        }
    }

    @OnClick(R.id.btn)
    public void buyGood() {
        final ProgressDialog pd = Utils.showProgress("Buying Good", Buy.this);
        HooptapApi.buyGood(HTApplication.getTinydb().getString("user_id"), good.getText().toString(), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                data = jsonObject.toString();
                try {
                    JSONObject datos = jsonObject.getJSONObject("response");
                    if (datos.getBoolean("purchased")) {
                        Utils.createDialog(Buy.this, "Congratulations, yo win this code: " + datos.getString("code"));
                    } else {
                        Utils.createDialog(Buy.this, "Sorry, this good is not avaiable");
                    }
                    Utils.dismisProgres(pd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Buy.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });


    }
}
