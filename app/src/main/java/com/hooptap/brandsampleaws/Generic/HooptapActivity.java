package com.hooptap.brandsampleaws.Generic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.Utils;

/**
 * Created by root on 15/12/15.
 */
public class HooptapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } /*else if (id == R.id.show) {
            if (!data.equals("")){
                Utils.createDialog(this, data);
            }
            return true;

        }*/ else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_show_json, menu);
        return true;
    }
}
