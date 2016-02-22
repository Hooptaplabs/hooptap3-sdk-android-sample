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
        email.setText("p2@p.com");
        password.setText("asdfqwer");

    }

    @OnClick(R.id.btn)
    public void llamarAccion() {
        final ProgressDialog pd = Utils.showProgress("Login", Login.this);
        HooptapApi.login(email.getText().toString(), password.getText().toString(), new HooptapCallback<HooptapUser>() {
            @Override
            public void onSuccess(HooptapUser user) {
                HTApplication.getTinydb().putString("user_id", user.get_id());
                startActivity(new Intent(Login.this, Principal.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Toast.makeText(Login.this, responseError.getReason(), Toast.LENGTH_LONG).show();
                //Utils.createDialogError(Login.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });
    }

}
