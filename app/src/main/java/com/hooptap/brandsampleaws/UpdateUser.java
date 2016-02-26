package com.hooptap.brandsampleaws;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapRegister;
import com.hooptap.sdkbrandclub.Models.HooptapUserUpdate;
import com.hooptap.sdkbrandclub.Models.HooptapUser;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 15/12/15.
 */

public class UpdateUser extends HooptapActivity {
    @Bind(R.id.ll_generic)
    LinearLayout ll_generic;
    @Bind(R.id.btn)
    Button btn;

    private HashMap<String, Object> objectsResponses = new HashMap<>();
    private Field[] fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Update User");
        btn.setText("Update User");

        fields = HooptapUserUpdate.class.getFields();

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().contains("birth")) {
                ll_generic.addView(generateInputTimestamp(fields[i].getName()));
            } else if (fields[i].getName().contains("gender")) {
                ll_generic.addView(generateInput(fields[i].getName(), InputType.TYPE_CLASS_NUMBER));
            } else {
                ll_generic.addView(generateInput(fields[i].getName(), InputType.TYPE_CLASS_TEXT));
            }
        }
    }

    @OnClick(R.id.btn)
    public void submit() {
        try {

            HooptapUserUpdate userInfo = fillUserInfoForUpdateModel();

            final ProgressDialog pd = Utils.showProgress("Update Model", UpdateUser.this);

            HooptapApi.updateUser(HTApplication.getTinydb().getString("user_id"), userInfo, new HooptapCallback<HooptapUser>() {
                @Override
                public void onSuccess(HooptapUser user) {
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

    private View generateInput(final String key, int typeClassNumber) {

        View action_edit = getLayoutInflater().inflate(R.layout.action_edit, null);
        TextView txt_boolean = (TextView) action_edit.findViewById(R.id.txt);
        txt_boolean.setText(key);
        final EditText edit = (EditText) action_edit.findViewById(R.id.edit);
        edit.setHint(key);
        edit.setId(objectsResponses.size());
        edit.setInputType(typeClassNumber);

        objectsResponses.put(key, edit);

        return action_edit;
    }

    private View generateInputTimestamp(final String key) {

        View action_edit = getLayoutInflater().inflate(R.layout.action_picker, null);
        TextView txt_boolean = (TextView) action_edit.findViewById(R.id.txt);
        txt_boolean.setText(key);
        final EditText edit = (EditText) action_edit.findViewById(R.id.picker);
        edit.setId(objectsResponses.size());
        edit.setHint(key);
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    Calendar calendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            edit.setText(selectedYear + "/" + selectedMonth + "/" + selectedDay);
                        }
                    };

                    new DatePickerDialog(UpdateUser.this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

                }
            }
        });

        objectsResponses.put(key, edit);

        return action_edit;
    }

    private HooptapUserUpdate fillUserInfoForUpdateModel() throws JSONException {
        JSONObject jsonParametersToBeParse = new JSONObject();
        for (int i = 0; i < fields.length; i++) {
            if (!((EditText) objectsResponses.get(fields[i].getName())).getText().toString().equals("")){
                if (fields[i].getName().equals("gender")) {
                    jsonParametersToBeParse.put(fields[i].getName(), Integer.parseInt(((EditText) objectsResponses.get(fields[i].getName())).getText().toString()));
                } else {
                    jsonParametersToBeParse.put(fields[i].getName(), ((EditText) objectsResponses.get(fields[i].getName())).getText().toString());
                }
            }
        }

        Gson gson = new Gson();
        HooptapUserUpdate userModelUpdate = gson.fromJson(jsonParametersToBeParse.toString(), HooptapUserUpdate.class);
        return userModelUpdate;
    }

}
