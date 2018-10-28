package com.example.dell.v;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AddChallenge extends AppCompatActivity {

    private List<ChallengeItem> challengeList = new ArrayList<ChallengeItem>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        challengeList.clear();

        setContentView(R.layout.add_challenge_layout);
        initData();
        ChallengeAdapter adapter = new ChallengeAdapter(AddChallenge.this, R.layout.challenge_item, challengeList);
        ListView listView = findViewById(R.id.challenge_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChallengeItem item = challengeList.get(position);
                Intent intent = new Intent(AddChallenge.this,ChallengePage.class);
                intent.putExtra("challenge_name",item.getChallengeName());
                intent.putExtra("duration",item.getDuration());
                intent.putExtra("icon_id",item.getIconId());
                intent.putExtra("challenge_bg_id",item.getChallengeBG());
                startActivity(intent);
            }
        });

    }


    private void initData()
    {
        ChallengeItem writing = new ChallengeItem("30天写作挑战", 30,R.drawable.icon_jane,R.drawable.challenge_reading);
        challengeList.add(writing);

        ChallengeItem veggie = new ChallengeItem("30天素食挑战", 30,R.drawable.icon_veggie,R.drawable.challenge_veggie);
        challengeList.add(veggie);
    }
}


