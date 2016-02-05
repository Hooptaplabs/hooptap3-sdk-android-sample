package com.hooptap.brandsampleaws.Generic;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Adapters.UserAdapter;
import com.hooptap.brandsampleaws.HTApplication;
import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.Options;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 18/12/15.
 */
public class Tab extends Fragment {

    @Bind(R.id.lista)
    ListView list;

    static int posicion;
    static HashMap<Integer, String> lista = new HashMap<>();

    static {
        lista.put(0, "GENERAL");
        lista.put(1, "DAILY");
        lista.put(2, "WEEKLY");
        lista.put(3, "MONTHLY");
    }

    public static Tab newInstance(int someInt) {
        Tab myFragment = new Tab();
        posicion = someInt;
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ranking_tab, container, false);
        ButterKnife.bind(this, v);

        list.setEmptyView(v.findViewById(R.id.emptyElement));
        final ProgressDialog pd = Utils.showProgress("Loading Rank", this.getActivity());
        HooptapApi.getRanking(HTApplication.getTinydb().getString("user_id"), new Options(), lista.get(posicion), new HooptapCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                HooptapActivity.data = jsonObject.toString();
                try {
                    JSONObject user = jsonObject.getJSONObject("response");

                    JSONArray array = user.getJSONArray("users");
                    if (array.length() > 0) {
                        UserAdapter userAdapter = new UserAdapter(getActivity(), array);
                        list.setAdapter(userAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.dismisProgres(pd);
            }
        });

        return v;
    }

}