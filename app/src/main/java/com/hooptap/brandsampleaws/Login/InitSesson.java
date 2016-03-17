package com.hooptap.brandsampleaws.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.hooptap.brandsampleaws.MainActivity;
import com.hooptap.brandsampleaws.R;
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
public class InitSesson extends AppCompatActivity {
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_sesson);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @OnClick(R.id.btn)
    public void llamarAccion() {
        final ProgressDialog pd = Utils.showProgress("Login", InitSesson.this);

        HooptapLogin userLogin = new HooptapLogin();
        userLogin.setLogin(email.getText().toString());
        userLogin.setPassword(password.getText().toString());

        HooptapApi.login(userLogin, new HooptapCallback<HooptapUser>() {
            @Override
            public void onSuccess(HooptapUser user) {
                Utils.setUserId(user.getExternalId());
                startActivity(new Intent(InitSesson.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Toast.makeText(InitSesson.this, responseError.getReason(), Toast.LENGTH_LONG).show();
                Utils.dismisProgres(pd);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
