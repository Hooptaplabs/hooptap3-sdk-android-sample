package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class DetailItem extends HooptapActivity {

    private String item_id;
    @Bind(R.id.detail) TextView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_item);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Detail Item");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            item_id = bundle.getString("id");
        } else {
            Toast.makeText(this, "Se ha producido un error, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            finish();
        }

        getDetailItem();

    }

    public void getDetailItem() {
        final ProgressDialog pd = Utils.showProgress("Loading Items", DetailItem.this);
        HooptapApi.getItemDetail(HTApplication.getTinydb().getString("user_id"), item_id, new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                data = jsonObject.toString();
                try {
                    JSONObject data = jsonObject.getJSONObject("response");

                    if (!data.equals("")) {
                        detail.setText(Utils.formatJSON(data + ""));
                    } else {
                        Utils.createDialog(DetailItem.this, "There aren\'t any elements at this moment");
                    }
                    Utils.dismisProgres(pd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.dismisProgres(pd);
                Utils.createDialogError(DetailItem.this, responseError.getReason());
            }
        });
    }
}
