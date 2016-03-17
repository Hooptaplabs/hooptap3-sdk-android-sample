package com.hooptap.brandsampleaws.Badges;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.HTApplication;
import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.GrayscaleTransformation;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapBadge;
import com.hooptap.sdkbrandclub.Models.ResponseError;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BadgeDetail extends HooptapActivity {

    @Bind(R.id.badge_name)
    TextView badge_name;
    @Bind(R.id.badge_desc)
    TextView badge_desc;
    @Bind(R.id.badge_progress)
    TextView badge_progress;
    @Bind(R.id.badge_img)
    CircleImageView badge_img;

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
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            finish();
        }

        fillBadge(badge);
        //getDetailBadge(badge.getIdentificator());

    }

    private void fillBadge(HooptapBadge badge) {
        if (badge.getImage() != null && !badge.getImage().equals("")) {
            if (badge.getProgress() < 100) {
                Picasso.with(getApplicationContext()).load(badge.getImage()).transform(new GrayscaleTransformation()).into(badge_img);
            } else {
                Picasso.with(getApplicationContext()).load(badge.getImage()).into(badge_img);
            }
        }
        badge_name.setText(badge.getName());
        badge_desc.setText(badge.getDescription());
        badge_progress.setText(badge.getProgress() + "%");
    }

    private void getDetailBadge(String badge_id) {
        HooptapApi.getBadgeDetail(badge_id, HTApplication.getTinydb().getString("user_id"), new HooptapCallback<HooptapBadge>() {
            @Override
            public void onSuccess(HooptapBadge hooptapBadge) {
                fillBadge(badge);
            }

            @Override
            public void onError(ResponseError responseError) {
                Toast.makeText(BadgeDetail.this, responseError.getReason(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
