package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hooptap.brandclub.model.UserModel;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapUser;
import com.hooptap.sdkbrandclub.Models.RegisterModel;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 15/12/15.
 */

public class Register extends AppCompatActivity {
    @Bind(R.id.ll_generic)
    LinearLayout ll_generic;
    RegisterModel info = new RegisterModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Register");

        ArrayList<String> params = info.getParams();
        for (int i = 0; i < params.size(); i++) {
            ll_generic.addView(Utils.createEditText(params.get(i), this));
        }
    }

    @OnClick(R.id.btn)
    public void submit() {
        Log.e("registro", "registrando");
        //JSONObject info = new JSONObject();

        try {

            ArrayList<String> datos = new ArrayList<>();
            for (Map.Entry<Integer, String> entry : Utils.listado.entrySet()) {
                Integer key = entry.getKey();
                String value = entry.getValue();
                Log.e("valores", value + "---" + key);
                EditText edit = (EditText) ll_generic.findViewById(key);

                String texto = edit.getText().toString();
                // Log.e("info",texto);
                datos.add(texto);
               /* if (!texto.matches("")) {


                }*/
            }
            info.setUsername(datos.get(0));
            //info.setPhoneNumber(datos.get(1));
            //info.setBirth(datos.get(2));
            //info.setPostalCode(datos.get(3));
            //info.setSurname(datos.get(4));
            info.setPassword(datos.get(5));
            //info.setGender(new BigDecimal(datos.get(6)));
            info.setEmail(datos.get(7));

            final ProgressDialog pd = Utils.showProgress("Register", Register.this);

            HooptapApi.registerUser(info, new HooptapCallback<HooptapUser>() {
                @Override
                public void onSuccess(HooptapUser user) {
                    HTApplication.getTinydb().putString("user_id", user.get_id());
                    startActivity(new Intent(Register.this, Principal.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    Utils.dismisProgres(pd);
                }

                @Override
                public void onError(ResponseError responseError) {
                    Utils.createDialogError(Register.this, responseError.getReason());
                    Utils.dismisProgres(pd);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
