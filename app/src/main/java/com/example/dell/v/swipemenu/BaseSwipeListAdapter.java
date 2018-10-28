package com.example.dell.v.swipemenu;

import android.widget.BaseAdapter;

public abstract class BaseSwipeListAdapter extends BaseAdapter{

    public boolean getSwipEnableByPosition(int position)
    {
        return true;
    }

}
