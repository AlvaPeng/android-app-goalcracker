package com.example.dell.v;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.v.Tools.ToastMaker;

public class ChallengePage extends AppCompatActivity{
    private String challengeTitle;
    private int challengeDuration;
    private int challengeIconID;
    private int challengeBGID;
    private TextView title_tv;
    private Button button_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_page_layout);
        challengeTitle = getIntent().getStringExtra("challenge_name");
        challengeDuration = getIntent().getIntExtra("duration",0);
        challengeIconID = getIntent().getIntExtra("icon_id",0);
        challengeBGID = getIntent().getIntExtra("challenge_bg_id",0);
        title_tv = findViewById(R.id.challenge_title);
        title_tv.setText(challengeTitle);
        button_add = findViewById(R.id.button_add_this_challenge);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SqlDao dao = new SqlDao(ChallengePage.this);
                dao.addFromChallenge(new ChallengeItem(challengeTitle,challengeDuration,challengeIconID,challengeBGID));
                Intent back = new Intent(ChallengePage.this, Home.class);
                startActivity(back);
            }
        });
    }
}