package com.hooptap.brandsampleaws;

import android.os.Bundle;
import android.util.Log;

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapBadge;
import com.hooptap.sdkbrandclub.Models.HooptapLevel;
import com.hooptap.sdkbrandclub.Models.HooptapQuest;
import com.hooptap.sdkbrandclub.Models.ResponseError;

/**
 * Created by carloscarrasco on 3/3/16.
 */
public class Details_Badge_Level_Quest extends HooptapActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDetailBadge();
        getDetailLevel();
        getDetailQuest();
    }

    private void getDetailBadge() {
        HooptapApi.getBadgeDetail("56d5817155aa7c877a186534", HTApplication.getTinydb().getString("user_id"), new HooptapCallback<HooptapBadge>() {
            @Override
            public void onSuccess(HooptapBadge hooptapBadge) {
                Log.e("BADGE", hooptapBadge.getName());
            }

            @Override
            public void onError(ResponseError responseError) {
                Log.e("BADGE", responseError.getReason());
            }
        });
    }

    private void getDetailLevel() {
        HooptapApi.getLevelDetail("56d57f7d55aa7c877a18652a", new HooptapCallback<HooptapLevel>() {
            @Override
            public void onSuccess(HooptapLevel hooptapLevel) {
                Log.e("Level", hooptapLevel.getName());
            }

            @Override
            public void onError(ResponseError responseError) {
                Log.e("Level", responseError.getReason());
            }
        });
    }

    private void getDetailQuest() {
        HooptapApi.getQuestDetail("56bca740bcfecfac1a4be5b6", HTApplication.getTinydb().getString("user_id"), new HooptapCallback<HooptapQuest>() {
            @Override
            public void onSuccess(HooptapQuest hooptapQuest) {
                Log.e("Quest", hooptapQuest.getName() + " / " + hooptapQuest.getSteps().size());
            }

            @Override
            public void onError(ResponseError responseError) {
                Log.e("Quest", responseError.getReason());
            }
        });
    }
}
