package com.hooptap.brandsampleaws;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.BlurTransformation;
import com.hooptap.brandsampleaws.Utils.GrayscaleTransformation;
import com.hooptap.sdkbrandclub.Models.HooptapBadge;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BadgeDetail extends HooptapActivity {

    @Bind(R.id.badge_name)
    TextView badge_name;
    @Bind(R.id.badge_desc)
    TextView badge_desc;
    @Bind(R.id.badge_progress)
    TextView badge_progress;
    @Bind(R.id.badge_img)
    ImageView badge_img;

    private HooptapBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.badge_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Badge Detail");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            badge = (HooptapBadge) bundle.get("badge");
        } else {
            Toast.makeText(this, "Se ha producido un error, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            finish();
        }

        fillBadge(badge);

    }

    private void fillBadge(HooptapBadge badge) {
        if (badge.getImage() != null && !badge.getImage().equals("")) {
            if (badge.getProgress() < 100){
                Picasso.with(getApplicationContext()).load(badge.getImage()).transform(new GrayscaleTransformation()).into(badge_img);
            }else{
                Picasso.with(getApplicationContext()).load(badge.getImage()).into(badge_img);
            }
        }
        badge_name.setText(badge.getName());
        badge_desc.setText(badge.getDescription());
        badge_progress.setText(badge.getProgress()+"%");
    }
}
