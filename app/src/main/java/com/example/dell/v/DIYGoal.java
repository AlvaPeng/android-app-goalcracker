package com.example.dell.v;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.dell.v.Tools.CalcuDays;

import java.util.Calendar;


public class DIYGoal extends AppCompatActivity {

    private Button buttonNext;
    private EditText newNameEnter;
    private CalendarView calendar;
    java.util.Calendar cur_c = java.util.Calendar.getInstance();    //当下时间
    private int selected_day, selected_month;
    private int current_day, current_month;
    int leftDays = 0;    //剩余日数
    String NewGoalName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diy_goal_layout);

        newNameEnter = findViewById(R.id.name_input);

        current_day = cur_c.get(Calendar.DAY_OF_MONTH);
        current_month = cur_c.get(Calendar.MONTH)+1;

        calendar = findViewById(R.id.calendar_date);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                CalcuDays c = new CalcuDays();
                selected_day = dayOfMonth;
                selected_month = month+1;
                leftDays = c.getMinus(current_month,current_day,selected_month,selected_day);
            }
        });



        buttonNext = findViewById(R.id.button_next);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGoalName = newNameEnter.getText().toString();

                Intent toNext = new Intent(DIYGoal.this,DIYGoalNext.class);
                toNext.putExtra("d_left_days",leftDays);  //使用Intent传递参数
                toNext.putExtra("d_new_goal_name",NewGoalName);
                toNext.putExtra("d_end_month",selected_month);
                toNext.putExtra("d_end_day",selected_day);
                toNext.putExtra("d_start_month",current_month);
                toNext.putExtra("d_start_day",current_day);

                startActivity(toNext);   //跳转
            }
        });
    }
}


