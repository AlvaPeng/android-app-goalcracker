package com.example.dell.v.swipemenu;

import java.util.List;

import android.view.View;
import android.widget.LinearLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.v.GoalItem;

public class SwipeMenuView extends LinearLayout implements OnClickListener {

    private SwipeMenuListView mListView;
    private SwipeMenuLayout mLayout;
    private SwipeMenu mMenu;    //使用了之前定义的swipe menu类
    private OnSwipeItemClickListener onItemClickListener;   //在adapter中使用
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SwipeMenuView(SwipeMenu menu, SwipeMenuListView listView)
    {
        super(menu.getContext());
        mListView = listView;
        mMenu = menu;
        List<GoalItem> items = menu.getMenuItems();
        int id = 0;
        for (GoalItem item : items) {
            addItem(item, id++);
        }
    }

    private void addItem(GoalItem item, int id) {
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(id);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setOnClickListener(this);
        addView(parent);

        if (item.getItemIcon() != null) {
            parent.addView(createIcon(item));
        }
        if (!TextUtils.isEmpty(item.getGoalName())) {
            parent.addView(createName(item));
        }

    }

    private ImageView createIcon(GoalItem item) {   //设置icon图像
        ImageView iv = new ImageView(getContext());
        iv.setImageDrawable(item.getItemIcon());
        return iv;
    }

    private TextView createName(GoalItem item) {
        TextView tv = new TextView(getContext());
        tv.setText(item.getGoalName());
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null && mLayout.isOpen()) {
            onItemClickListener.onItemClick(this, mMenu, v.getId());
        }
    }

    public OnSwipeItemClickListener getOnSwipeItemClickListener() {
        return onItemClickListener;
    }

    public void setOnSwipeItemClickListener(OnSwipeItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setLayout(SwipeMenuLayout mLayout) {
        this.mLayout = mLayout;
    }

    public static interface OnSwipeItemClickListener {
        void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
    }
}
