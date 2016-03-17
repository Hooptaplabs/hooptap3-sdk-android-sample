package com.hooptap.brandsampleaws.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.hooptap.brandsampleaws.MainActivity;
import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapRegister;
import com.hooptap.sdkbrandclub.Models.HooptapUser;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONException;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 15/12/15.
 */

public class Register extends AppCompatActivity {

    @Bind(R.id.external_id)
    EditText external_id;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.name)
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @OnClick(R.id.btn)
    public void submit() {
        try {
            HooptapRegister userInfo = fillUserInfoForRegister();

            final ProgressDialog pd = Utils.showProgress("Register", Register.this);
            HooptapApi.registerUser(userInfo, new HooptapCallback<HooptapUser>() {
                @Override
                public void onSuccess(HooptapUser user) {
                    Utils.setUserId(user.getExternalId());
                    startActivity(new Intent(Register.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    Utils.dismisProgres(pd);
                }

                @Override
                public void onError(ResponseError responseError) {
                    Toast.makeText(getApplicationContext(), responseError.getReason(), Toast.LENGTH_LONG).show();
                    Utils.dismisProgres(pd);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HooptapRegister fillUserInfoForRegister() throws JSONException {
        HooptapRegister userRegister = new HooptapRegister();
        if (!TextUtils.isEmpty(external_id.getText().toString())) {
            userRegister.setExternalId(external_id.getText().toString());
        }

        if (!TextUtils.isEmpty(email.getText().toString())) {
            userRegister.setEmail(email.getText().toString());
        }

        if (!TextUtils.isEmpty(password.getText().toString())) {
            userRegister.setPassword(password.getText().toString());
        }

        if (!TextUtils.isEmpty(name.getText().toString())) {
            userRegister.setUsername(name.getText().toString());
        }

        return userRegister;
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
