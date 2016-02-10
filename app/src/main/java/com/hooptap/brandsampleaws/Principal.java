package com.hooptap.brandsampleaws;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Adapters.ListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class Principal extends AppCompatActivity {

    @Bind(R.id.lista)
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal);
        ButterKnife.bind(this);
        Log.e("principal", HTApplication.getTinydb().getString("user_id"));
        if (HTApplication.getTinydb().getString("user_id").equals("")) {
            startActivity(new Intent(this, Initial.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
        ListAdapter listAdapter = new ListAdapter(this);
        lista.setAdapter(listAdapter);

    }

    @OnItemClick(R.id.lista)
    void onItemSelected(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, Profile.class));
                break;
            case 1:
                startActivity(new Intent(this, Levels.class));
                break;
            case 2:
                startActivity(new Intent(this, RankingList.class));
                break;
            case 3:
                startActivity(new Intent(this, MarketPlace.class));
                break;
            case 4:
                startActivity(new Intent(this, Goods.class));
                break;
            case 5:
                startActivity(new Intent(this, Buy.class));
                break;
            case 6:
                startActivity(new Intent(this, Badges.class));
                break;
            case 7:
                startActivity(new Intent(this, Actions.class));
                break;
            case 8:
                startActivity(new Intent(this, Feed.class));
                break;
            case 9:
                startActivity(new Intent(this, Game.class));
                break;
            case 10:
                startActivity(new Intent(this, ItemList.class));
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Log.i("ActionBar", "Nuevo!");
                HTApplication.getTinydb().clear();
                startActivity(new Intent(Principal.this, Principal.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
