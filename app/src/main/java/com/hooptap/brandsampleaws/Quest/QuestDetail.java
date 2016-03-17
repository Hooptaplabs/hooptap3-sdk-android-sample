package com.hooptap.brandsampleaws.Quest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Actions.Actions;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.HTApplication;
import com.hooptap.brandsampleaws.R;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapAction;
import com.hooptap.sdkbrandclub.Models.HooptapQuest;
import com.hooptap.sdkbrandclub.Models.HooptapStep;
import com.hooptap.sdkbrandclub.Models.ResponseError;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuestDetail extends HooptapActivity {

    @Bind(R.id.lista)
    ListView list;
    private ImageView quest_img;
    private TextView quest_title;
    private TextView quest_desc;
    private HooptapQuest quest;
    private StepAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Quest");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            quest = (HooptapQuest) bundle.get("quest");
        } else {
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            finish();
        }

        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (isStepActive(position - 1)) {
                    HooptapStep step = (HooptapStep) adapterView.getItemAtPosition(position);
                    startActivityForResult(new Intent(QuestDetail.this, Actions.class).putExtra("action", (HooptapAction) step.getActions().get(0)), 0);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.step_blocked), Toast.LENGTH_LONG).show();
                }
            }
        });

        addHeaderView();
        fillQuest(quest);
        //getDetailQuest(quest.getIdentificator());
    }

    private void addHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.quest_header, null, false);
        quest_img = (ImageView) view.findViewById(R.id.quest_img);
        quest_title = (TextView) view.findViewById(R.id.quest_title);
        quest_desc = (TextView) view.findViewById(R.id.quest_desc);

        list.addHeaderView(view, null, false);
    }

    private void fillQuest(HooptapQuest quest) {

        if (quest.getImage() != null || !quest.getImage().equals("")) {
            Picasso.with(this).load(quest.getImage()).into(quest_img);
        }
        quest_title.setText(quest.getName());
        quest_desc.setText(quest.getDescription());

        adapter = new StepAdapter(this, quest.getSteps(), quest);
        list.setAdapter(adapter);
    }

    private void getDetailQuest(final String quest_id) {
        HooptapApi.getQuestDetail(quest_id, HTApplication.getTinydb().getString("user_id"), new HooptapCallback<HooptapQuest>() {
            @Override
            public void onSuccess(HooptapQuest hooptapQuest) {
                fillQuest(hooptapQuest);
            }

            @Override
            public void onError(ResponseError responseError) {
                Toast.makeText(QuestDetail.this, responseError.getReason(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            quest.setNumCompletedSteps(quest.getNumCompletedSteps() + data.getExtras().getInt("num_steps_completeds"));
            adapter.notifyDataSetChanged();
            if (data.getExtras().getBoolean("finished")) {
                AlertDialog.Builder alert = new AlertDialog.Builder(QuestDetail.this);
                alert.setTitle("Congratulations!");
                alert.setMessage("You completed the Quest");
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                alert.show();
            }
        }
    }

    private boolean isStepActive(int position) {
        return position == quest.getNumCompletedSteps();
    }


}
