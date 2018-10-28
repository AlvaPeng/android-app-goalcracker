package com.example.dell.v;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.dell.v.Tools.CalcuDays;

public class GoalItem {
    private String goalName;
    private int startMonth;
    private int startDay;
    private int endMonth;
    private int endDay;
    private int leftDays;
    private int checkedDays = 0;
    private int checkedToday = 0;
    private int lastCheckedMonth = 0;
    private int lastCheckedDay = 0;
    private Drawable itemIcon;
    private String stringIcon = "";
    private int iconId = 0;
    private int duration; //用于challenge
    private int isChallenge = 0;
    CalcuDays cal = new CalcuDays();

    public GoalItem(){}

    //用于添加Challenge到challenge list
    public GoalItem(String goalName, int duration, int iconId)
    {
        this.goalName = goalName;
        this.endMonth = cal.getEndDate(startMonth,startDay,duration)[0];
        this.endDay = cal.getEndDate(startMonth,startDay,duration)[1];
        this.duration = duration;
        this.iconId = iconId;
    }

    //用于添加Item
    public GoalItem(String goalName, int startMonth, int startDay, int endMonth, int endDay,String stringIcon)
    {
        this.goalName = goalName;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.stringIcon = stringIcon;
    }
    //用于加载item到list
    public GoalItem(String goalName, int startMonth, int startDay, int endMonth, int endDay, int lastCheckedMonth, int lastCheckedDay,  int checkedDays, int checkedToday, String stringIcon, int iconID)
    {
        this.goalName = goalName;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.lastCheckedMonth = lastCheckedMonth;
        this.lastCheckedDay = lastCheckedDay;
        this.checkedDays = checkedDays;
        this.checkedToday = checkedToday;
        this.stringIcon = stringIcon;
        this.iconId = iconID;
    }


    public String getGoalName(){ return goalName; }

    public void setGoalName(String goalName){ this.goalName = goalName;}

    public int getStartMonth(){ return startMonth; }

    public void setStartMonth(int startMonth) { this.startMonth = startMonth; }

    public int getStartDay(){ return startDay; }

    public void setStartDay(int startDay) { this.startDay = startDay; }

    public int getEndMonth(){ return endMonth; }

    public void setEndMonth(int endMonth) { this.endMonth = endMonth; }

    public int getEndDay(){ return endDay; }

    public void setEndDay(int endDay) { this.endDay = endDay; }

    public int getLeftDays(){ return leftDays; }

    public void setLeftDays(int leftDays) { this.leftDays = leftDays; }

    public int getCheckedDays(){ return checkedDays; }

    public void setCheckedDays(int checkedDays) { this.checkedDays = checkedDays; }

    public int getCheckedToday(){ return checkedToday; }

    public void setCheckedToday(int leftDays) { this.checkedToday = checkedToday; }

    public int getLastCheckedMonth(){ return lastCheckedMonth; }

    public void setLastCheckedMonth(int lastCheckedMonth) { this.lastCheckedMonth = lastCheckedMonth; }

    public int getLastCheckedDay(){ return lastCheckedDay; }

    public void setLastCheckedDay(int lastCheckedDay) { this.lastCheckedDay = lastCheckedDay; }

    public String getStringIcon(){ return stringIcon;}

    public void setStringIcon(String stringIcon){ this.stringIcon = stringIcon;}

    public Drawable getItemIcon(){return itemIcon;}

    public void setItemIcon(Drawable itemIcon) { this.itemIcon = itemIcon; }

    public int getIconId(){ return iconId;}

    public void setIconId(int iconId) { this.iconId = iconId;}

    public int getIsChallenge() {
        return isChallenge;
    }

    public void setIsChallenge(int isChallenge) {
        this.isChallenge = isChallenge;
    }
}
