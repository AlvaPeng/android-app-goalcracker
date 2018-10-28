package com.example.dell.v;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.v.swipemenu.BaseSwipeListAdapter;
import com.example.dell.v.swipemenu.SwipeMenu;
import com.example.dell.v.swipemenu.SwipeMenuCreator;
import com.example.dell.v.swipemenu.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity {

    private List<GoalItem> goalList = new ArrayList<GoalItem>();
    java.util.Calendar cur_c = java.util.Calendar.getInstance();
    private int current_month, current_day;
    private ImageButton add_ddl;
    private SwipeMenuListView mListView;
    private DDLAdapter mAdapter;
    private Button test;
    private SqlDao dao;
    public static Resources resourcesInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        resourcesInstance = this.getResources();
        current_day = cur_c.get(Calendar.DAY_OF_MONTH);
        current_month = cur_c.get(Calendar.MONTH)+1;
        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        mAdapter = new DDLAdapter();

        initDB();

        //第一步：创建menu creator
        //设置滑动格的属性啦:滑动打卡
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                GoalItem checkItem = new GoalItem();

                checkItem.setItemIcon(getResources().getDrawable(R.drawable.ok_icon_green_lite));

                menu.addMenuItem(checkItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        //设置单击滑动出现的区域
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                GoalItem item = goalList.get(position);
                switch(index)
                {
                    case 0:
                        check(item);
                        break;
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GoalItem item = goalList.get(position);
                Intent intent = new Intent(Home.this,ItemPage.class);
                intent.putExtra("goal_name",item.getGoalName());
                intent.putExtra("start_month",item.getStartMonth());
                intent.putExtra("start_day",item.getStartDay());
                intent.putExtra("end_month",item.getEndMonth());
                intent.putExtra("end_day",item.getEndDay());
                intent.putExtra("checked_days",item.getCheckedDays());
                intent.putExtra("checked_today",item.getCheckedToday());
                startActivity(intent);
            }
        });
        //设置滑动监听
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

        //设置menuset改变的监听
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {

            }

            @Override
            public void onMenuClose(int position) {

            }
        });

        //设置长按删除
        registerForContextMenu(mListView);

        //设置长按监听
        /*
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/

        //加号按钮
          add_ddl = findViewById(R.id.button_add);

           add_ddl.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toAdd = new Intent(Home.this,AddGoal.class);
                    startActivity(toAdd);   //跳转
              }
           });

           test = findViewById(R.id.button_test);
           test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAdd = new Intent(Home.this,MapTest.class);
                startActivity(toAdd);   //跳转
            }
        });

    }//onCreate

    //初始化数据库
    private void initDB()
    {
        dao = new SqlDao(Home.this);
        goalList = dao.findAll();
        mListView.setAdapter(mAdapter);

    }

    private void check(GoalItem item)
    {
        SqlDao dao = new SqlDao(Home.this);
        if(item.getCheckedToday()==0)
        {
            item.setCheckedToday(1);
            item.setLastCheckedMonth(current_month);
            item.setLastCheckedDay(current_day);
            item.setCheckedDays(item.getCheckedDays()+1);
            int i = dao.upDate(item);
            if(i!=-1)
                Toast.makeText(Home.this, "打卡成功!", Toast.LENGTH_SHORT).show();
            initDB();
        }
        else Toast.makeText(Home.this, "今天已经打卡过啦", Toast.LENGTH_SHORT).show();
    }


    //设置adapter
    class DDLAdapter extends BaseSwipeListAdapter{

        public int getCount() {return goalList.size();}

        public GoalItem getItem(int position) {
            return goalList.get(position);
        }

        public long getItemId(int position) {return position;}

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.goal_item, null);
                new ViewHolder(convertView);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            GoalItem item = getItem(position);
            holder.deadline_icon.setImageDrawable(item.getItemIcon());
            holder.deadline_name.setText(item.getGoalName());

            return convertView;
        }

        class ViewHolder {
            ImageView deadline_icon;
            TextView deadline_name;

            public ViewHolder(View view) {
                deadline_icon = (ImageView) view.findViewById(R.id.ddl_icon);
                deadline_name = (TextView) view.findViewById(R.id.ddl_name);
                view.setTag(this);
            }
        }


        private int dp2px(int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    getResources().getDisplayMetrics());
        }


        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        }
    }

    //使用context menu实现长按删除

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.add(0, 0, 0, "删除");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        GoalItem goal = goalList.get(info.position);
        SqlDao dao = new SqlDao(Home.this);
        dao.delete(goal.getGoalName());
        initDB();
        return true;
    }

    //从其他页返回时更新
    @Override
    protected void onResume() {
        super.onResume();
        initDB();
    }
}