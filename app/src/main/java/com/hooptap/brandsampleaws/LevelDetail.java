package com.hooptap.brandsampleaws;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.GrayscaleTransformation;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LevelDetail extends HooptapActivity {

    @Bind(R.id.level_name)
    TextView level_name;
    @Bind(R.id.level_desc)
    TextView level_desc;
    @Bind(R.id.level_completed)
    TextView level_completed;
    @Bind(R.id.level_img)
    ImageView level_img;

    private HooptapLevel level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_detail);
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
        if (level.getImage() != null && !level.getImage().equals("")) {
            if (level.isPassed()) {
                Picasso.with(getApplicationContext()).load(level.getImage()).into(level_img);
                level_completed.setTextColor(getResources().getColor(R.color.active));
            } else {
                Picasso.with(getApplicationContext()).load(level.getImage()).transform(new GrayscaleTransformation()).into(level_img);
                level_completed.setTextColor(getResources().getColor(R.color.desactive));
            }
        }
        level_name.setText(level.getName());
        level_desc.setText(level.getDescription());
        level_completed.setText(level.isPassed() + "");
    }
}
