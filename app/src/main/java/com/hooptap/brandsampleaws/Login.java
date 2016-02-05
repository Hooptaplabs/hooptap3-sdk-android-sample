package com.hooptap.brandsampleaws;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 15/12/15.
 */
public class Login extends AppCompatActivity {
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password) EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Login");
        email.setText("p2@p.com");
        password.setText("asdfqwer");

    }
    @OnClick(R.id.btn)
    public void llamarAccion() {
        HooptapApi.login(email.getText().toString(), password.getText().toString(), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONObject data=jsonObject.getJSONObject("response");
                    HTApplication.getTinydb().putString("user_id",data.getString("_id"));
                    HTApplication.getTinydb().putString("profile",data+"");
                    startActivity(new Intent(Login.this, Principal.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    Log.e("login", jsonObject + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(ResponseError responseError) {
                new MaterialDialog.Builder(Login.this)
                        .title(R.string.error)
                        .content(responseError.getReason())
                        .positiveText("Salir")
                        .show();
                Log.e("usuariosregisterEEE", responseError.getReason() + "");
            }
        });
    }
}
