package com.example.dell.v;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.v.MorphingButton.MorphingButton;
import com.example.dell.v.Tools.CalcuDays;
import com.example.dell.v.Tools.ToastMaker;

import java.util.Calendar;

public class ItemPage extends AppCompatActivity {

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    private ColorArcProgressBar progressBar;
    private ColorArcProgressBar innerBar;
    private TextView nameText;
    private String ItemName;
    private int passedDays;
    private int totalDays;
    java.util.Calendar cur_c = java.util.Calendar.getInstance();    //当下时间
    private int current_day, current_month, start_month, start_day,end_month,end_day, checked_days, checked_today;
    ToastMaker toast = new ToastMaker();
    private int state;
    private SqlDao dao;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_page_layout);

        final MorphingButton checkButton = (MorphingButton) findViewById(R.id.button_morph);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButtonClicked(checkButton);
            }
        });
        morphToSquare(checkButton, 0); //初始状态

        initItemPage();

        dao = new SqlDao(ItemPage.this);
        state = dao.getState(ItemName);

    }
    void initItemPage()
    {
        ItemName = getIntent().getStringExtra("goal_name");
        start_month = getIntent().getIntExtra("start_month",0);
        start_day = getIntent().getIntExtra("start_day",0);
        end_month = getIntent().getIntExtra("end_month",0);
        end_day = getIntent().getIntExtra("end_day",0);
        checked_days = getIntent().getIntExtra("checked_days",0);
        checked_today = getIntent().getIntExtra("checked_today",0);

        current_day = cur_c.get(Calendar.DAY_OF_MONTH);
        current_month = cur_c.get(Calendar.MONTH)+1;
        CalcuDays calcu = new CalcuDays();
        //从目标设定的日起开始记时
        passedDays = calcu.getMinus(start_month,start_day,current_month,current_day)+1;
        totalDays = calcu.getMinus(start_month,start_day,end_month,end_day)+1;

        nameText = findViewById(R.id.itemName);
        nameText.setText(ItemName);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMaxValues(totalDays);    //顺序很重要，要先设置maxvalue,再设置currentvalue否则会出错
        progressBar.setCurrentValues(passedDays);

        innerBar = findViewById(R.id.progress_inner_bar);
        innerBar.setMaxValues(totalDays);
        innerBar.setCurrentValues(checked_days);
    }

    //设置morphing button
    private void onMorphButtonClicked(final MorphingButton btnMorph) {
        if (state == 0) {
            morphToSuccess(btnMorph);
        }
        else
            toast.MakeToast(ItemPage.this,"今天已经打卡过啦");
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_120))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
                .text("check");
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);

            state = 1;
            dao.checkByName(ItemName,current_month,current_day);
            innerBar.setCurrentValues(checked_days+1);
    }

    private void morphToFailure(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_red))
                .colorPressed(color(R.color.mb_red_dark))
                .icon(R.drawable.ic_lock);
        btnMorph.morph(circle);
    }
}
