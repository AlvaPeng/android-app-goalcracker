package com.example.dell.v.Tools;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {
    private Context context;

    public ToastMaker(){}
    public ToastMaker(Context context)
    {this.context = context;}
    public void MakeToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    public void MakeToast(String text)
    {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
