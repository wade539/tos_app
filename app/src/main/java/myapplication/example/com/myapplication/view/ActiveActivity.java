package myapplication.example.com.myapplication.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

import myapplication.example.com.myapplication.object.Card;
import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.adapter.TagAdapter;
import myapplication.example.com.myapplication.views.CraftsActivity;
import myapplication.example.com.myapplication.views.CraftwsActivity;
import myapplication.example.com.myapplication.views.ListActivity;
import myapplication.example.com.myapplication.views.MainActivity;

public class ActiveActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "tos.db";
    public static final String Table_Name = "active";
    public ArrayList<Card> showList = new ArrayList<Card>();
    public SQLiteDatabase db;
    public TagAdapter adapter;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view_left;
    private TableLayout grid_skill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        // 設定UI :
        // 預設資料庫
        db = openOrCreateDatabase(DATABASE_NAME,  Context.MODE_PRIVATE, null);
        // 標題欄
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("主動技標籤");
        // 滑動側欄框架
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // 滑動側欄(左)
        navigation_view_left = (NavigationView) findViewById(R.id.navigation_view_left);
        int[][] states = new int[][]{new int[]{android.R.attr.state_checked},new int[]{-android.R.attr.state_checked} };
        int[] colors = new int[]{ getResources().getColor(R.color.checked_color),  getResources().getColor(R.color.uncheck_color) };
        ColorStateList csl = new ColorStateList(states, colors);
        navigation_view_left.setItemTextColor(csl);
        navigation_view_left.setItemIconTintList(csl);
        navigation_view_left.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // 功能列表
                drawerLayout.closeDrawer(GravityCompat.START);
                int id = item.getItemId();
                if (id == R.id.home_page) {
                    Intent intent = new Intent(ActiveActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if (id == R.id.leader_page) {
                    Intent intent = new Intent(ActiveActivity.this,LeaderActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craft_page) {
                    Intent intent = new Intent(ActiveActivity.this,CraftsActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craft_tag_page) {
                    Intent intent = new Intent(ActiveActivity.this,CraftTagActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craftw_page) {
                    Intent intent = new Intent(ActiveActivity.this,CraftwsActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });navigation_view_left.getMenu().getItem(1).setChecked(true);

        // 取得使用介面視窗大小
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;

        // 從資料庫抓取資料 : 主動技能資料
        Cursor c=db.rawQuery("SELECT * FROM " + Table_Name, null);
        if (c.getCount()>0){
            String tmp_id,tmp_name,tmp_monster;
            Card tmp_active;
            c.moveToFirst();
            do{
                tmp_id =  c.getString(0);
                tmp_name =  c.getString(1);
                tmp_monster = c.getString(2);
                String[] tmp_monsters;
                int tmp_number = 0;
                if(tmp_monster != null){
                    tmp_monsters = tmp_monster.split(",");
                    tmp_number = tmp_monsters.length;
                }else{ tmp_number = 0;}
                tmp_active = new Card(tmp_id,tmp_name,null,null,null,null,null,null,null,null,tmp_number);
                showList.add(tmp_active);
            } while(c.moveToNext());
        }

        // 呈現表格
        grid_skill = (TableLayout) findViewById(R.id.grid_skill);
        for (int i=0; i<showList.size()/2+1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 2 * i; j < 2 * (i + 1); j++) {
                if (j < showList.size()) {
                    TextView text = new TextView(this);
                    text.setText(showList.get(j).get_name() + "(" + showList.get(j).get_img() + ")");
                    text.setTextSize(15);
                    text.setPadding(20, 20, 20, 20);
                    final String set_id = showList.get(j).get_id();
                    text.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(v.getContext(),ListActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id",set_id);
                            bundle.putInt("type",0);
                            intent.putExtras(bundle);
                            v.getContext().startActivity(intent);
                        }
                    });
                    tr.addView(text, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
            grid_skill.addView(tr);
        }
    }
}
