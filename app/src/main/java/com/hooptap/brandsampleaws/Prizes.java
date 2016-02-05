package com.hooptap.brandsampleaws;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 15/12/15.
 */
public class Prizes extends AppCompatActivity {
    @Bind(R.id.user_id)
    EditText user_id;
    @Bind(R.id.good) EditText good;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Buy");
        setContentView(R.layout.comprar_good);
        ButterKnife.bind(this);
        user_id.setText(HTApplication.getTinydb().getString("user_id"));
        good.setText("55dafa57f95836fb6de15dce");
    }
    @OnClick(R.id.btn)
    public void llamarAccion() {
        HooptapApi.buyGood(user_id.getText().toString(), good.getText().toString(), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.e("buyGood", jsonObject + "");

                try {
                    JSONObject datos=jsonObject.getJSONObject("response");


                    if(datos.getBoolean("purchased")){
                        new MaterialDialog.Builder(Prizes.this)
                                .title(R.string.compra)
                                .content(datos.getString("code"))
                                .positiveText("Salir")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        startActivity(new Intent(Prizes.this,Principal.class));
                                    }
                                })
                                .show();
                    }else{
                        new MaterialDialog.Builder(Prizes.this)
                                .title(R.string.compra_no)
                                .content("No se ha podido comprar")
                                .positiveText("Salir")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        startActivity(new Intent(Prizes.this, Principal.class));
                                    }
                                })
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ResponseError responseError) {
                Log.e("buyGood", responseError.getReason() + "");
                new MaterialDialog.Builder(Prizes.this)
                        .title(R.string.error)
                        .content(responseError.getReason())
                        .positiveText("Salir")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                startActivity(new Intent(Prizes.this, Principal.class));
                            }
                        })
                        .show();
                Log.e("usuariosregisterEEE", responseError.getReason() + "");
            }
        });



    }
}
