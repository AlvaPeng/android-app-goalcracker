package com.example.dell.v;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TestAppTime extends AppCompatActivity {

    private java.util.Calendar beginCal;
    private java.util.Calendar endCal;
    private TextView text;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        setContentView(R.layout.test_app_time_layout);

        beginCal = Calendar.getInstance();
        beginCal.add(Calendar.HOUR_OF_DAY, -1);
        endCal = Calendar.getInstance();
        UsageStatsManager manager=(UsageStatsManager)getApplicationContext().getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> stats=manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,beginCal.getTimeInMillis(),endCal.getTimeInMillis());

        text = findViewById(R.id.app_time_text);
        StringBuilder sb=new StringBuilder();
        for(UsageStats us:stats){
            try {
                PackageManager pm=getApplicationContext().getPackageManager();
                ApplicationInfo applicationInfo=pm.getApplicationInfo(us.getPackageName(),PackageManager.GET_META_DATA);
                if((applicationInfo.flags&applicationInfo.FLAG_SYSTEM)<=0){
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String t = format.format(new Date(us.getLastTimeUsed()));
                    String d = format.format(new Date(us.getTotalTimeInForeground()));
                    sb.append("您在" + t +"已经使用"+ pm.getApplicationLabel(applicationInfo)+" "+ d +"\n");
                    text.setText(sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
