package com.hooptap.brandsampleaws.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.HTApplication;
import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.BlurTransformation;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapPoint;
import com.hooptap.sdkbrandclub.Models.HooptapUser;
import com.hooptap.sdkbrandclub.Models.ResponseError;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends HooptapActivity {

    @Bind(R.id.photoPr)
    CircleImageView photoPr;
    @Bind(R.id.photoPrBlur)
    ImageView photoPrBlur;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.id)
    TextView id;
    @Bind(R.id.points)
    TextView points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Profile");

        final ProgressDialog pd = Utils.showProgress("Loading Profile", this);
        HooptapApi.getProfile(HTApplication.getTinydb().getString("user_id"), new HooptapCallback<HooptapUser>() {
            @Override
            public void onSuccess(HooptapUser user) {
                getPoints();
                fillProfile(user);
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Profile.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });

    }

    private void getPoints(){
        HooptapApi.getPoints(HTApplication.getTinydb().getString("user_id"), new HooptapCallback<HooptapPoint>() {
            @Override
            public void onSuccess(HooptapPoint hooptapPoint) {
                points.setText(hooptapPoint.getQuantity()+" PUNTOS");
            }

            @Override
            public void onError(ResponseError responseError) {

            }
        });
    }

    private void fillProfile(HooptapUser user) {

        fillTextView(name, (user.getUsername() != null) ? user.getUsername(): "");
        fillTextView(email, (user.getEmail() != null) ? "Email : " + user.getEmail() : "");
        fillTextView(id, (user.getExternalId() != null) ? "Id : " + user.getExternalId() : "");

        if ((user.getImage() != null && !user.getImage().equals(""))) {
            Picasso.with(this).load(user.getImage()).into(photoPr);
            Picasso.with(this).load(user.getImage()).transform(new BlurTransformation(this)).into(photoPrBlur);
        } else {
            Picasso.with(this).load(R.drawable.tapface).into(photoPr);
            Picasso.with(this).load(R.drawable.tapface).transform(new BlurTransformation(this)).into(photoPrBlur);
        }
    }

    private void fillTextView(TextView textview, String text) {
        textview.setText(text);
        textview.setVisibility((text.equals("")) ? View.GONE : View.VISIBLE);
    }


}
