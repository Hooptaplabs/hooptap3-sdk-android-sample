package com.hooptap.brandsampleaws;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hooptap.brandsampleaws.Actions.Actions;
import com.hooptap.brandsampleaws.Badges.BadgeList;
import com.hooptap.brandsampleaws.Feed.Feed;
import com.hooptap.brandsampleaws.Levels.LevelList;
import com.hooptap.brandsampleaws.Login.Login;
import com.hooptap.brandsampleaws.Quest.QuestsList;
import com.hooptap.brandsampleaws.Ranking.RankingList;
import com.hooptap.brandsampleaws.User.Profile;
import com.hooptap.brandsampleaws.User.UpdateUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.lista)
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        if (HTApplication.getTinydb().getString("user_id").equals("")) {
            startActivity(new Intent(this, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
        PrincipalListAdapter listAdapter = new PrincipalListAdapter(this);
        lista.setAdapter(listAdapter);

    }

    @OnItemClick(R.id.lista)
    void onItemSelected(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, Profile.class));
                break;
            case 1:
                startActivity(new Intent(this, UpdateUser.class));
                break;
            case 2:
                startActivity(new Intent(this, LevelList.class));
                break;
            case 3:
                startActivity(new Intent(this, RankingList.class));
                break;
            case 4:
                startActivity(new Intent(this, BadgeList.class));
                break;
            case 5:
                startActivity(new Intent(this, Actions.class));
                break;
            case 6:
                startActivity(new Intent(this, Feed.class));
                break;
            case 7:
                startActivity(new Intent(this, QuestsList.class));
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
                HTApplication.getTinydb().clear();
                startActivity(new Intent(MainActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
