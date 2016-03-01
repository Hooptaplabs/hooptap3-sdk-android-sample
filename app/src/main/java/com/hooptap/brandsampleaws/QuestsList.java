package com.hooptap.brandsampleaws;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Adapters.QuestListAdapter;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapFilter;
import com.hooptap.sdkbrandclub.Models.HooptapListResponse;
import com.hooptap.sdkbrandclub.Models.HooptapOptions;
import com.hooptap.sdkbrandclub.Models.HooptapQuest;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuestsList extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;

    @Override
    protected void onResume() {
        super.onResume();
        getQuests();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("List Quests");

        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final HooptapQuest quest = (HooptapQuest) adapterView.getItemAtPosition(i);
                if (!quest.isFinished()) {
                    activeQuest(quest);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.quest_finished), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void getQuests() {
        final ProgressDialog pd = Utils.showProgress("Loading Quest", QuestsList.this);
        HooptapApi.getUserQuests(HTApplication.getTinydb().getString("user_id"), new HooptapOptions(),
                new HooptapFilter(), new HooptapCallback<HooptapListResponse>() {
                    @Override
                    public void onSuccess(HooptapListResponse htResponse) {

                        if (htResponse.getItemArray().size() > 0) {
                            QuestListAdapter questAdapter = new QuestListAdapter(QuestsList.this, htResponse.getItemArray());
                            list.setAdapter(questAdapter);
                        }
                        Utils.dismisProgres(pd);
                    }

                    @Override
                    public void onError(ResponseError responseError) {
                        Utils.createDialogError(QuestsList.this, responseError.getReason());
                        Utils.dismisProgres(pd);
                    }
                });
    }

    public void activeQuest(final HooptapQuest quest) {
        if (!quest.isActive()) {
            final ProgressDialog pd = Utils.showProgress("Joining to the Quest", QuestsList.this);
            HooptapApi.activeUserQuest(HTApplication.getTinydb().getString("user_id"), quest.getIdentificator(), new HooptapCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean active) {
                    if (active) {
                        startActivity(new Intent(QuestsList.this, QuestDetail.class).putExtra("quest", quest));
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.quest_join_error), Toast.LENGTH_LONG).show();
                    }
                    Utils.dismisProgres(pd);
                }

                @Override
                public void onError(ResponseError responseError) {
                    Toast.makeText(getApplicationContext(), responseError.getReason(), Toast.LENGTH_LONG).show();
                    Utils.dismisProgres(pd);
                }
            });
        } else {
            startActivity(new Intent(QuestsList.this, QuestDetail.class).putExtra("quest", quest));
        }
    }
}
