package com.hooptap.brandsampleaws;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LevelDetail extends HooptapActivity {

    @Bind(R.id.detail)
    TextView detail;
    private HooptapLevel level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Level Detail");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            level = (HooptapLevel) bundle.get("level");
        } else {
            Toast.makeText(this, "Se ha producido un error, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            finish();
        }

        fillLevel(level);

    }

    private void fillLevel(HooptapLevel level) {
        detail.setText(level.getName() + " / " + level.getImage() + " / " + level.getNumber());
    }
}
