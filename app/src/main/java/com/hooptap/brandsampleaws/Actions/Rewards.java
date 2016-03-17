package com.hooptap.brandsampleaws.Actions;

import android.os.Bundle;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.R;
import com.hooptap.sdkbrandclub.Models.HooptapActionResult;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by carloscarrasco on 22/2/16.
 */
public class Rewards extends HooptapActivity {
    private HooptapActionResult action;

    @Bind(R.id.lista)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Action Rewards");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = (HooptapActionResult) bundle.get("action");
        }

        ArrayList arrayRewards = action.getRewards();
        if (action.getLevel() != null)
            arrayRewards.add(0, action.getLevel());

        ActionRewardsAdapter userAdapter = new ActionRewardsAdapter(Rewards.this, arrayRewards);
        list.setAdapter(userAdapter);

    }
}
