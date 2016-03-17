package com.hooptap.brandsampleaws.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.HTApplication;
import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapUser;
import com.hooptap.sdkbrandclub.Models.HooptapUserUpdate;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 15/12/15.
 */

public class UpdateUser extends HooptapActivity {
    @Bind(R.id.external_id)
    EditText external_id;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Update User");
        btn.setText("Update User");

    }

    @OnClick(R.id.btn)
    public void submit() {
        try {

            HooptapUserUpdate userInfo = fillUserInfoForUpdateModel();

            final ProgressDialog pd = Utils.showProgress("Update Model", UpdateUser.this);
            HooptapApi.updateUser(HTApplication.getTinydb().getString("user_id"), userInfo, new HooptapCallback<HooptapUser>() {
                @Override
                public void onSuccess(HooptapUser user) {
                    Utils.setUserId(user.getExternalId());
                    Toast.makeText(getApplicationContext(), "User has been update", Toast.LENGTH_LONG).show();
                    Utils.dismisProgres(pd);
                    finish();
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

    private HooptapUserUpdate fillUserInfoForUpdateModel() throws JSONException {
        HooptapUserUpdate userUpdate = new HooptapUserUpdate();

        if (!TextUtils.isEmpty(external_id.getText().toString())) {
            userUpdate.setExternalId(external_id.getText().toString());
        }

        if (!TextUtils.isEmpty(email.getText().toString())) {
            userUpdate.setExternalId(email.getText().toString());
        }

        if (!TextUtils.isEmpty(password.getText().toString())) {
            userUpdate.setExternalId(password.getText().toString());
        }

        if (!TextUtils.isEmpty(name.getText().toString())) {
            userUpdate.setExternalId(name.getText().toString());
        }

        return userUpdate;
    }

}
