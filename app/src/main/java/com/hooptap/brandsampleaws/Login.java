package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapLogin;
import com.hooptap.sdkbrandclub.Models.HooptapUser;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 15/12/15.
 */
public class Login extends AppCompatActivity {
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Login");
        email.setText("ca");
        password.setText("ca");

    }

    @OnClick(R.id.btn)
    public void llamarAccion() {
        final ProgressDialog pd = Utils.showProgress("Login", Login.this);

        HooptapLogin userLogin = new HooptapLogin();
        userLogin.setLogin(email.getText().toString());
        userLogin.setPassword(password.getText().toString());

        HooptapApi.login(userLogin, new HooptapCallback<HooptapUser>() {
            @Override
            public void onSuccess(HooptapUser user) {
                Utils.setUserId(user.getExternalId());
                startActivity(new Intent(Login.this, Principal.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Toast.makeText(Login.this, responseError.getReason(), Toast.LENGTH_LONG).show();
                Utils.dismisProgres(pd);
            }
        });
    }

}
