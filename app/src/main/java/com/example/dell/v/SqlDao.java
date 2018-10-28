package com.example.dell.v;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dell.v.Tools.CalcuDays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SqlDao {
    private DBHelper helper;
    java.util.Calendar cur_c = java.util.Calendar.getInstance();    //当下时间
    CalcuDays cal = new CalcuDays();

    public SqlDao(Context context) {
        helper = new DBHelper(context);
    }

    private Resources getResources()
    {
        Resources mResources = null;
        mResources = getResources();
        return mResources;
    }
    //添加
    public boolean add(GoalItem goal) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("GoalName", goal.getGoalName());
        cv.put("StartMonth", goal.getStartMonth());
        cv.put("StartDay", goal.getStartDay());
        cv.put("EndMonth", goal.getEndMonth());
        cv.put("EndDay", goal.getEndDay());
        cv.put("Icon", goal.getStringIcon());
        long insert = db.insert(DBHelper.TABLE_NAME, null, cv);
        db.close();

        if (insert != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addFromChallenge(ChallengeItem challenge) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("GoalName", challenge.getChallengeName());
        int startMonth = cur_c.get(Calendar.MONTH)+1;
        int startDay = cur_c.get(Calendar.DAY_OF_MONTH);
        cv.put("StartMonth",startMonth);
        cv.put("StartDay", startDay);
        int duration = challenge.getDuration();
        cv.put("EndMonth", cal.getEndDate(startMonth,startDay,duration)[0]);
        cv.put("EndDay", cal.getEndDate(startMonth,startDay,duration)[1]);
        cv.put("IconID", challenge.getIconId());
        cv.put("IsChallenge",1);
        long insert = db.insert(DBHelper.TABLE_NAME, null, cv);
        db.close();

        if (insert != -1) {
            return true;
        } else {
            return false;
        }
    }


    //删除
    public void delete(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME, "GoalName=?", new String[]{name});
        db.close();
    }

    //更新LastCheckedMonth和LastCheckedDay
    public int upDate(GoalItem item)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("LastCheckedMonth",item.getLastCheckedMonth());
        cv.put("LastCheckedDay",item.getLastCheckedDay());
        cv.put("CheckedToday",item.getCheckedToday());
        cv.put("CheckedDays",item.getCheckedDays());
        return db.update(DBHelper.TABLE_NAME,cv,"GoalName=?",new String[]{item.getGoalName()});
    }

    public int checkByName(String name,int thisCheckedMonth, int thisCheckedDay)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null,"GoalName=?", new String[]{name}, null, null, null);
        ContentValues cv = new ContentValues();
        if(cursor.moveToFirst())
        {

            cv.put("LastCheckedMonth",thisCheckedMonth);
            cv.put("LastCheckedDay",thisCheckedDay);
            cv.put("CheckedToday",1);
            cv.put("CheckedDays",cursor.getInt(cursor.getColumnIndex("CheckedDays"))+1);

        }
        return db.update(DBHelper.TABLE_NAME,cv,"GoalName=?",new String[]{name});
    }
    //查找任务
    public GoalItem findItem(String name)
    {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, "GoalName=?", new String[]{name}, null, null, null);
        if(cursor.moveToFirst()){
            String goalName = cursor.getString(cursor.getColumnIndex("GoalName"));
            int StartMonth = cursor.getInt(cursor.getColumnIndex("StartMonth"));
            int StartDay = cursor.getInt(cursor.getColumnIndex("StartDay"));
            int EndMonth = cursor.getInt(cursor.getColumnIndex("EndMonth"));
            int EndDay = cursor.getInt(cursor.getColumnIndex("EndDay"));
            String StringIcon = cursor.getString(cursor.getColumnIndex("StringIcon"));
            GoalItem item = new GoalItem(goalName,StartMonth,StartDay,EndMonth,EndDay,StringIcon);
            item.setCheckedDays(1);
            item.setCheckedToday(0);
            return item;
        }
        else return null;
    }

    //get checked state
    public int getState(String name)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null,"GoalName=?", new String[]{name}, null, null, null);
        if(cursor.moveToFirst())
        {
            int state = cursor.getInt(cursor.getColumnIndex("CheckedToday"));
            return state;
        }
        return -1;
    }

    //查询所有任务
    public List<GoalItem> findAll() {
        int current_day = cur_c.get(Calendar.DAY_OF_MONTH);
        int current_month = cur_c.get(Calendar.MONTH)+1;
        int lastCheckedMonth  = 0;
        int lastCheckedDay = 0;
        int checkedDays = 0;
        String icon = null;
        int iconID = 0;

        SQLiteDatabase db = helper.getReadableDatabase();

        List<GoalItem> goalList = new ArrayList<GoalItem>();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String goalName = cursor.getString(cursor.getColumnIndex("GoalName"));
            int startMonth = cursor.getInt(cursor.getColumnIndex("StartMonth"));
            int startDay = cursor.getInt(cursor.getColumnIndex("StartDay"));
            int endMonth = cursor.getInt(cursor.getColumnIndex("EndMonth"));
            int endDay = cursor.getInt(cursor.getColumnIndex("EndDay"));
            lastCheckedMonth = cursor.getInt(cursor.getColumnIndex("LastCheckedMonth"));
            lastCheckedDay = cursor.getInt(cursor.getColumnIndex("LastCheckedDay"));
            checkedDays = cursor.getInt(cursor.getColumnIndex("CheckedDays"));
            int checkedToday;
            if(lastCheckedMonth==current_month&&lastCheckedDay==current_day)
                checkedToday = 1;
            else
                checkedToday = 0;

            ContentValues cv = new ContentValues();
            cv.put("CheckedToday",checkedToday);
            db.update(DBHelper.TABLE_NAME,cv,"GoalName=?",new String[]{goalName});

            icon = cursor.getString(cursor.getColumnIndex("Icon")); //字符串存储icon的代号
            iconID = cursor.getInt(cursor.getColumnIndex("IconID"));

            GoalItem goal = new GoalItem(goalName,startMonth,startDay,endMonth,endDay,lastCheckedMonth,lastCheckedDay,checkedDays,checkedToday,icon,iconID);
            loadPic(goal, icon,iconID, checkedToday);
            goalList.add(goal);
        }
        db.close();
        cursor.close();
        return goalList;
    }

    void loadPic(GoalItem item, String choice, int iconID, int checked) {
        if(choice == "" || choice == null)
        {
            item.setItemIcon(Home.resourcesInstance.getDrawable(iconID));
        }
        else {
            switch (choice) {
                case "reading":
                    if (checked == 0)
                        item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.book_icon_gray_full));
                    else
                        item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.book_icon_full));
                    break;
                case "sports":
                    if (checked == 0)
                        item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.sport_icon_gray_full));
                    else
                        item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.sport_icon_full));
                    break;
                case "scenery":
                    if (checked == 0)
                        item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.scenery_icon_gray_full));
                    else
                        item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.scenery_icon_full));
                    break;
                case "ok":
                    if (checked == 0)
                        item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.ok_hand_icon_gray_full));
                    else
                        item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.ok_hand_icon_full));
                    break;
                default:
                    item.setItemIcon(Home.resourcesInstance.getDrawable(R.drawable.default_pic));
                    break;
            }
        }
    }

    //添加挑战
    public boolean addChallenge(ChallengeItem challenge) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ChallengeName", challenge.getChallengeName());
        cv.put("Duration",challenge.getDuration());
        cv.put("IconId",challenge.getIconId());
        cv.put("ChallengeBG", challenge.getChallengeBG());
        cv.put("Task1", challenge.getChallengeContent()[1]);
        cv.put("Task2", challenge.getChallengeContent()[2]);
        cv.put("Task3", challenge.getChallengeContent()[3]);
        cv.put("Task4", challenge.getChallengeContent()[4]);
        cv.put("Task5", challenge.getChallengeContent()[5]);
        cv.put("Task6", challenge.getChallengeContent()[6]);
        cv.put("Task7", challenge.getChallengeContent()[7]);
        cv.put("Task8", challenge.getChallengeContent()[8]);
        cv.put("Task9", challenge.getChallengeContent()[9]);
        cv.put("Task10", challenge.getChallengeContent()[10]);
        cv.put("Task11", challenge.getChallengeContent()[11]);
        cv.put("Task12", challenge.getChallengeContent()[12]);
        cv.put("Task13", challenge.getChallengeContent()[13]);
        cv.put("Task14", challenge.getChallengeContent()[14]);
        cv.put("Task15", challenge.getChallengeContent()[15]);
        cv.put("Task16", challenge.getChallengeContent()[16]);
        cv.put("Task17", challenge.getChallengeContent()[17]);
        cv.put("Task18", challenge.getChallengeContent()[18]);
        cv.put("Task19", challenge.getChallengeContent()[19]);
        cv.put("Task20", challenge.getChallengeContent()[20]);
        cv.put("Task21", challenge.getChallengeContent()[21]);
        cv.put("Task22", challenge.getChallengeContent()[22]);
        cv.put("Task23", challenge.getChallengeContent()[23]);
        cv.put("Task24", challenge.getChallengeContent()[24]);
        cv.put("Task25", challenge.getChallengeContent()[25]);
        cv.put("Task26", challenge.getChallengeContent()[26]);
        cv.put("Task27", challenge.getChallengeContent()[27]);
        cv.put("Task28", challenge.getChallengeContent()[28]);
        cv.put("Task29", challenge.getChallengeContent()[29]);
        cv.put("Task30", challenge.getChallengeContent()[30]);

        long insert = db.insert(DBHelper.CHALLENGE_TABLE_NAME, null, cv);
        db.close();

        if (insert != -1) {
            return true;
        } else {
            return false;
        }
    }
}






