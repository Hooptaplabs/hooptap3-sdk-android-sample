package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapGame;
import com.hooptap.sdkbrandclub.Models.HooptapItem;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemDetail extends HooptapActivity {

    private String item_id;
    @Bind(R.id.detail)
    TextView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
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
        final ProgressDialog pd = Utils.showProgress("Loading Items", ItemDetail.this);
        HooptapApi.getItemDetail(HTApplication.getTinydb().getString("user_id"), item_id, new HooptapCallback<HooptapItem>() {
            @Override
            public void onSuccess(HooptapItem item) {
                detail.setText(item.getName() + " / " + item.getImage() + " / " + item.getItemType() + " \n" + " \n");
                if (item.getItemType().equals("Game")) {
                    detail.setText(detail.getText() + "" + ((HooptapGame) item).getUrl_game());
                }
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.dismisProgres(pd);
                Utils.createDialogError(ItemDetail.this, responseError.getReason());
            }
        });
    }
}
