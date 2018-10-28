package com.example.dell.v.swipemenu;

import android.content.Context;

import com.example.dell.v.GoalItem;

import java.util.ArrayList;
import java.util.List;

public class SwipeMenu {

    private Context mContext;
    private List<GoalItem> mItems;
    private int mViewType;

    public SwipeMenu(Context context)
    {
        mContext = context;
        mItems = new ArrayList<GoalItem>();
    }
    public Context getContext() {
        return mContext;
    }

    public void addMenuItem(GoalItem item) {
        mItems.add(item);
    }

    public void removeMenuItem(GoalItem item) {
        mItems.remove(item);
    }

    public List<GoalItem> getMenuItems() {
        return mItems;
    }

    public GoalItem getMenuItem(int index) {
        return mItems.get(index);
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }
}
