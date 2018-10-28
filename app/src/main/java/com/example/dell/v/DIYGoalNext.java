package com.example.dell.v;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DIYGoalNext extends AppCompatActivity {

    private TextView textLeftDays;
    private ImageButton DIYFinished;
    private int leftDays;
    private String newGoalName;
    private int startMonth;
    private int startDay;
    private int endMonth;
    private int endDay;
    private RadioGroup rbgroup;
    private String newIconString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diy_goal_next);

        //由intent得到DIYGoal类中的值
        leftDays = getIntent().getIntExtra("d_left_days",0);
        newGoalName = getIntent().getStringExtra("d_new_goal_name");
        startMonth = getIntent().getIntExtra("d_start_month",0);
        startDay = getIntent().getIntExtra("d_start_day",0);
        endMonth = getIntent().getIntExtra("d_end_month",0);
        endDay = getIntent().getIntExtra("d_end_day",0);

        //剩余时间显示
        textLeftDays = findViewById(R.id.text_left_days);
        textLeftDays.setText(leftDays+"");

        rbgroup = findViewById(R.id.rb_group);

        rbgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.rb_icon_book: newIconString = "reading"; break;
                    case R.id.rb_icon_sport: newIconString = "sports"; break;
                    case R.id.rb_icon_scenery: newIconString = "scenery"; break;
                    case R.id.rb_icon_ok: newIconString = "ok"; break;
                }
            }
        });

        DIYFinished = findViewById(R.id.button_diy_finished);

        DIYFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoalItem newItem = new GoalItem(newGoalName,startMonth,startDay,endMonth,endDay,newIconString);
                SqlDao dao = new SqlDao(DIYGoalNext.this);
                dao.add(newItem);
                Intent diy = new Intent(DIYGoalNext.this,Home.class);
                startActivity(diy);   //跳转
            }
        });
    }//oncreate
}
