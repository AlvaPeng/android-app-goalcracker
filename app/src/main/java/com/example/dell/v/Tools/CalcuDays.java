package com.example.dell.v.Tools;

public class CalcuDays {
    public int getMinus(int prem, int pred, int postm, int postd)
    {
        int result = 0;
        int day[] = {0,31,28,31,30,31,30,31,31,30,31,30,31};
        for(int i = prem; i<postm; i++)
        {
            result+=day[i];
        }
        result = result - pred + postd;
        return result;
    }

    public int[] getEndDate(int startm, int startd, int duration)
    {
        int day[]= {0,31,28,31,30,31,30,31,31,30,31,30,31};
        int temp = day[startm] - startd; //初始为该月剩余天数
        int i;
        for(i = startm+1; temp <= duration; i++)
        {
            temp+=day[i];
        }
        int m = --i; //结束的月份
        temp-=day[i];
        int d = duration - temp;
        return new int[]{m,d};
    }
}
