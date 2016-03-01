package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Adapters.ItemsAdapter;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapFilter;
import com.hooptap.sdkbrandclub.Models.HooptapItem;
import com.hooptap.sdkbrandclub.Models.HooptapListResponse;
import com.hooptap.sdkbrandclub.Models.HooptapOptions;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemList extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("List Items");

        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HooptapItem item = (HooptapItem) adapterView.getItemAtPosition(i);
                startActivity(new Intent(ItemList.this, ItemDetail.class).putExtra("id", item.getIdentificator()));
            }
        });

        getItems();
    }

    public void getItems() {
        final ProgressDialog pd = Utils.showProgress("Loading Items", ItemList.this);
        HooptapApi.getItems(HTApplication.getTinydb().getString("user_id"), new HooptapOptions(), new HooptapFilter(), new HooptapCallback<HooptapListResponse>() {
            @Override
            public void onSuccess(HooptapListResponse htResponse) {

                if (htResponse.getItemArray().size() > 0) {
                    ItemsAdapter userAdapter = new ItemsAdapter(ItemList.this, htResponse.getItemArray());
                    list.setAdapter(userAdapter);
                }
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(ItemList.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });


    }
}
