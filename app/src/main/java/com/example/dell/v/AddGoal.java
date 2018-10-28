package com.example.dell.v;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class AddGoal extends AppCompatActivity{

    private ImageButton addChallenge;
    private ImageButton diyGoal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal_layout);

        addChallenge = findViewById(R.id.button_add_challenge);
        diyGoal = findViewById(R.id.button_diy_goal);

        addChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent challenge = new Intent(AddGoal.this,AddChallenge.class);
                startActivity(challenge);   //跳转
            }
        });

        diyGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diy = new Intent(AddGoal.this,DIYGoal.class);
                startActivity(diy);   //跳转
            }
        });
    }
}
