package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.BlurTransformation;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapUser;
import com.hooptap.sdkbrandclub.Models.ResponseError;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
public class Profile extends HooptapActivity {

    @Bind(R.id.photoPr)
    ImageView photoPr;
    @Bind(R.id.photoPrBlur)
    ImageView photoPrBlur;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.surname)
    TextView surname;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.id)
    TextView id;
    @Bind(R.id.gender)
    TextView gender;

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
                //data = jsonObject + "";
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

    private void fillProfile(HooptapUser user) {

            //If parameter username is not empty in the Json Response, get the value and fill textview with
            //this value, if not set visibility to GONE
            fillTextView(username, (!user.getUsername().equals("")) ? user.getUsername() : "");
            fillTextView(surname, (!user.getSurname().equals("")) ? "SurName : "+user.getSurname() : "");
            fillTextView(email, (!user.getEmail().equals("")) ? "Email : "+user.getEmail() : "");
            fillTextView(id, (!user.get_id().equals("")) ? "Id : "+user.get_id() : "");

            //Gender is a int value = -1 Undefined; 0 = Male; 1 = female
            int genderInt = user.getGender();
            if (genderInt == 0) {
                fillTextView(gender, "Gender : Male");
            } else if (genderInt == 1) {
                fillTextView(gender, "Gender : Female");
            } else {
                fillTextView(gender, "Gender : Undefined");
            }

            //If parameter image is not empty in the Json Response, download the image with Picasso library, if not
            //put a static image
            if ((!user.getImage().equals("")) ? true : false) {
                Picasso.with(this).load(user.getImage()).into(photoPr);
                Picasso.with(this).load(user.getImage()).transform(new BlurTransformation(this)).into(photoPrBlur);
            }else{
                Picasso.with(this).load(R.drawable.tapface).into(photoPr);
                Picasso.with(this).load(R.drawable.tapface).transform(new BlurTransformation(this)).into(photoPrBlur);
            }
    }

    private void fillTextView(TextView textview, String text) {
        textview.setText(text);
        textview.setVisibility((text.equals("")) ? View.GONE : View.VISIBLE);
    }


}
