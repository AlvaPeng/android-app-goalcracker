package com.example.dell.v.Tools;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {
    public void MakeToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
