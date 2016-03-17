package com.hooptap.brandsampleaws.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hooptap.brandsampleaws.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 19/01/16.
 */
public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void loginUser() {
        startActivity(new Intent(this, InitSesson.class));
    }

    @OnClick(R.id.register)
    public void registerUser() {
        startActivity(new Intent(this, Register.class));
    }

}
