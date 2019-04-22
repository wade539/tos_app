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

import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.adapter.TagAdapter;
import myapplication.example.com.myapplication.views.CraftsActivity;
import myapplication.example.com.myapplication.views.CraftwsActivity;
import myapplication.example.com.myapplication.views.ListCraftActivity;
import myapplication.example.com.myapplication.views.MainActivity;

public class CraftTagActivity  extends AppCompatActivity {
    public static final String DATABASE_NAME = "tos.db";
    public static final String Active_Table = "leader";
    public ArrayList<Craft_Tag> showList_power = new ArrayList<Craft_Tag>();
    public ArrayList<Craft_Tag> showList_sub = new ArrayList<Craft_Tag>();
    public ArrayList<Craft_Tag> showList_skill = new ArrayList<Craft_Tag>();
    public SQLiteDatabase db;
    public TagAdapter adapter;
    public int tb_id = 1;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view_left;
    private TableLayout grid_power,grid_sub,grid_skill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craft_tag);

        // Set Items
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("龍刻標籤");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
                    Intent intent = new Intent(CraftTagActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if (id == R.id.active_page) {
                    Intent intent = new Intent(CraftTagActivity.this,ActiveActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craft_page) {
                    Intent intent = new Intent(CraftTagActivity.this,CraftsActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craftw_page) {
                    Intent intent = new Intent(CraftTagActivity.this,CraftwsActivity.class);
                    startActivity(intent);
                }else if (id == R.id.leader_page) {
                    Intent intent = new Intent(CraftTagActivity.this,LeaderActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });navigation_view_left.getMenu().findItem(R.id.craft_tag_page).setChecked(true);
        navigation_view_left.getMenu().getItem(0).setChecked(false);
        // Set Data
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;

        // Get Data
        db = openOrCreateDatabase(DATABASE_NAME,  Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("SELECT * FROM  craft_tag", null);
        if (c.getCount()>0){
            String tmp_id,tmp_name,tmp_type,tmp_monster;
            Craft_Tag tmp_craft;
            c.moveToFirst();
            do{
                tmp_id =  c.getString(0);
                tmp_name =  c.getString(1);
                tmp_type = c.getString(2);
                tmp_monster = c.getString(3);
                String[] tmp_monsters;
                int tmp_number = 0;
                if(tmp_monster != null){
                    tmp_monsters = tmp_monster.split(",");
                    tmp_number = tmp_monsters.length;
                }else{ tmp_number = 0;}
                tmp_craft = new Craft_Tag(tmp_id,tmp_name,tmp_type,Integer.toString(tmp_number));
                if(tmp_type.equals("power")){
                    showList_power.add(tmp_craft);
                }else if(tmp_type.equals("sub")){
                    showList_sub.add(tmp_craft);
                }else if(tmp_type.equals("skill")){
                    showList_skill.add(tmp_craft);
                }
            } while(c.moveToNext());
        }

        // Set Show List
        grid_power = (TableLayout) findViewById(R.id.grid_power);
        for (int i=0; i<showList_power.size()/2+1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 2*i; j < 2*(i+1); j++) {
                if(j < showList_power.size()){
                    TextView text = new TextView(this);
                    text.setText(showList_power.get(j).get_name() + "(" + showList_power.get(j).get_monster() + ")");
                    text.setTextSize(15);
                    text.setPadding(20, 20, 20, 20);
                    final String set_id = showList_power.get(j).get_id();
                    text.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(v.getContext(),ListCraftActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id",set_id);
                            intent.putExtras(bundle);
                            v.getContext().startActivity(intent);
                        }
                    });
                    tr.addView(text,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
            grid_power.addView(tr);
        }

        grid_sub = (TableLayout) findViewById(R.id.grid_sub);
        for (int i=0; i<showList_sub.size()/3+1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 3 * i; j < 3 * (i + 1); j++) {
                if (j < showList_sub.size()) {
                    TextView text = new TextView(this);
                    text.setText(showList_sub.get(j).get_name() + "(" + showList_sub.get(j).get_monster() + ")");
                    text.setTextSize(15);
                    text.setPadding(20, 20, 20, 20);
                    final String set_id = showList_sub.get(j).get_id();
                    text.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(v.getContext(),ListCraftActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id",set_id);
                            intent.putExtras(bundle);
                            v.getContext().startActivity(intent);
                        }
                    });
                    tr.addView(text, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
            grid_sub.addView(tr);
        }

        grid_skill = (TableLayout) findViewById(R.id.grid_skill);
        for (int i=0; i<showList_skill.size()/2+1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 2 * i; j < 2 * (i + 1); j++) {
                if (j < showList_skill.size()) {
                    TextView text = new TextView(this);
                    text.setText(showList_skill.get(j).get_name() + "(" + showList_skill.get(j).get_monster() + ")");
                    text.setTextSize(15);
                    text.setPadding(20, 20, 20, 20);
                    final String set_id = showList_skill.get(j).get_id();
                    text.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(v.getContext(),ListCraftActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id",set_id);
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

    public class Craft_Tag {
        private String id,name,type,monster;
        public Craft_Tag(String id,String name,String type,String monster) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.monster = monster;
        }
        public void set_id(String id){ this.id = id; }
        public String get_id(){ return id; }

        public void set_name(String name){ this.name = name; }
        public String get_name(){ return name; }

        public void set_type(String type){ this.type = type; }
        public String get_type(){ return type; }

        public void set_monster(String power){ this.monster = monster; }
        public String get_monster(){ return monster; }
    }
}
