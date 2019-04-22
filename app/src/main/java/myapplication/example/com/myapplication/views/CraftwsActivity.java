package myapplication.example.com.myapplication.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.ArrayList;
import java.util.Collections;

import myapplication.example.com.myapplication.adapter.ImageAdapter3;
import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.object.Craftw;
import myapplication.example.com.myapplication.view.ActiveActivity;
import myapplication.example.com.myapplication.view.CraftTagActivity;
import myapplication.example.com.myapplication.view.LeaderActivity;

public class CraftwsActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "tos.db";
    public static final String Table_Name = "craftw";
    public SQLiteDatabase db;
    public ImageAdapter3 adapter;
    public int[]  id_race,id_star,id_mode,id_type;
    public ArrayList<Craftw> showList = new ArrayList<Craftw>();
    public ArrayList<Craftw> craftList = new ArrayList<Craftw>();
    public ArrayList<Integer> select_mode = new ArrayList<Integer>();
    public ArrayList<Integer> select_type = new ArrayList<Integer>();
    public ArrayList<Integer> select_star = new ArrayList<Integer>();
    public ArrayList<String> type_type = new ArrayList<String>();
    public String[] type_mode = {"連鎖龍紋", "轉動龍印", "破碎龍咒", "映照龍符", "裂空龍刃", "落影龍璃", "鏡像龍丸"};
    public String[] type_attr = {"水","火","木","光","暗"};
    public String[] icon_attr = {"water","fire","wood","light","dark"};
    public String[] type_race = {"神族","魔族","人類","獸類","龍類","妖精類","機械族"};
    public String[] icon_race = {"god","demon","human","beast","dragon","elf","machina"};
    public String[] type_star = {"1","2","3"};
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view_left;
    private TableLayout grid_type,grid_star,grid_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_craftw);

        // Set Items
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("龍刻武裝");
        toolbar.inflateMenu(R.menu.filter);
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
                    Intent intent = new Intent(CraftwsActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if (id == R.id.active_page) {
                    Intent intent = new Intent(CraftwsActivity.this,ActiveActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craft_page) {
                    Intent intent = new Intent(CraftwsActivity.this,CraftsActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craft_tag_page) {
                    Intent intent = new Intent(CraftwsActivity.this,CraftTagActivity.class);
                    startActivity(intent);
                }else if (id == R.id.leader_page) {
                    Intent intent = new Intent(CraftwsActivity.this,LeaderActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });navigation_view_left.getMenu().findItem(R.id.craftw_page).setChecked(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_filter:
                        drawerLayout.openDrawer(GravityCompat.END);
                        break;
                }
                return false;
            }
        });

        View v = findViewById(R.id.filter);
        v.getBackground().setAlpha(100);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        // Set Data
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;


        // Get Data
        db = openOrCreateDatabase(DATABASE_NAME,  Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("SELECT * FROM " + Table_Name, null);
        if (c.getCount()>0){
            String tmp_id,tmp_name,tmp_star,tmp_limit,tmp_set,tmp_type,tmp_tp,tmp_power,tmp_skill_1,tmp_skill_2,tmp_skill_3,tmp_up_1,tmp_up_2,tmp_up_3;;
            int tmp_img;
            Craftw tmp_craftw;
            c.moveToFirst();
            do{
                tmp_id =  c.getString(0);
                tmp_name =  c.getString(1);
                tmp_type =  c.getString(2);
                tmp_tp =  c.getString(3);
                tmp_star =  c.getString(4);
                tmp_power =  c.getString(5);
                tmp_limit =  c.getString(6);
                tmp_set =  c.getString(7);
                tmp_skill_1 =  c.getString(8);
                tmp_skill_2 =  c.getString(9);
                tmp_skill_3 =  c.getString(10);
                tmp_up_1 =  c.getString(11);
                tmp_up_2 =  c.getString(12);
                tmp_up_3 =  c.getString(13);
                tmp_img = getResources().getIdentifier("rk_" + tmp_id, "drawable", getPackageName());
                tmp_craftw = new Craftw(tmp_id,tmp_name,tmp_star,tmp_type,tmp_tp,tmp_limit,tmp_set,tmp_power,tmp_skill_1,tmp_skill_2,tmp_skill_3,tmp_up_1,tmp_up_2,tmp_up_3,tmp_img);
                craftList.add(tmp_craftw);
                if(type_type.contains(tmp_set) == false){
                    type_type.add(tmp_set);
                }
            } while(c.moveToNext());
            showList = craftList;
        }

        set_Id(); // Set Filter Items
        filter_Item(); // Set Filter items

        // Set Show List
        adapter = new ImageAdapter3(this,screenWidth,showList);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

    }

    public void filter_Item() {
        grid_mode = (TableLayout) findViewById(R.id.grid_craft);
        for (int i=0; i<type_mode.length/2+1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 2*i; j < 2*(i+1); j++) {
                if(j < type_mode.length){
                    Button b = new Button(this);
                    b.setText(type_mode[j]);
                    b.setTextSize(15);
                    int tmp_img = getResources().getIdentifier("mode_" + Integer.toString(j+1), "drawable", getPackageName());
                    b.setCompoundDrawablesWithIntrinsicBounds( tmp_img, 0, 0, 0);
                    b.setId(id_mode[j]);
                    b.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            set_Selected(v.getId(), 2, v);
                        }
                    });
                    tr.addView(b,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
            grid_mode.addView(tr);
        }
        grid_type = (TableLayout) findViewById(R.id.grid_attr);
        for (int i=0; i<type_type.size()/2+1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 2*i; j < 2*(i+1); j++) {
                if(j < type_type.size()){
                    Button b = new Button(this);
                    b.setText(type_type.get(j));
                    b.setTextSize(15);
                    b.setId(id_type[j]);
                    b.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            set_Selected(v.getId(), 0, v);
                        }
                    });
                    tr.addView(b,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
            grid_type.addView(tr);
        }

        grid_star = (TableLayout) findViewById(R.id.grid_star);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        for (int i=0;i<type_star.length;i++) {
            Button b = new Button(this);
            b.setText(type_star[i]);
            b.setTextSize(15);
            b.setTextColor(Color.parseColor("#dbcb88"));
            b.setBackgroundResource(R.drawable.item_btn);
            b.setId(id_star[i]);
            b.setPadding(10,10,10,10);
            b.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    set_Selected(v.getId(), 1, v);
                }
            });
            tr.addView(b,100,100);
        }
        grid_star.addView(tr);
    }

    public void filter_Card() {
        //int to string
        ArrayList<String> stars = new ArrayList<String>();
        ArrayList<String> modes = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        showList = new ArrayList<Craftw>();
        if(select_star.size() != 0){
            for(int i=0;i<select_star.size();i++){
                for(int j=0;j<id_star.length;j++){
                    if(select_star.get(i) == id_star[j]){
                        stars.add(type_star[j]);
                    }
                }
            }
        }else{
            for(int i=0;i<type_star.length;i++){
                stars.add(type_star[i]);
            }
        }
        if(select_mode.size() != 0){
            for(int i=0;i<select_mode.size();i++){
                for(int j=0;j<id_mode.length;j++){
                    if(select_mode.get(i) == id_mode[j]){
                        modes.add(type_mode[j]);
                    }
                }
            }
        }else{
            for(int i=0;i<type_mode.length;i++){
                modes.add(type_mode[i]);
            }
        }
        if(select_type.size() != 0){
            for(int i=0;i<select_type.size();i++){
                for(int j=0;j<id_type.length;j++){
                    if(select_type.get(i) == id_type[j]){
                        types.add(type_type.get(j));
                    }
                }
            }
        }else{
            for(int i=0;i<type_type.size();i++){
                types.add(type_type.get(i));
            }
        }
        for(int v=0;v<craftList.size();v++){ // all crafts
            for(int m=0;m<modes.size();m++){ // type mode
                for(int k=0;k<stars.size();k++){ // type star
                    for(int j=0;j<types.size();j++){ // type type
                        if(craftList.get(v).get_type().equals(modes.get(m)) &&
                                craftList.get(v).get_set().equals(types.get(j)) &&
                                craftList.get(v).get_star().equals(stars.get(k))){
                            showList.add(craftList.get(v));
                        }
                    }
                }
            }
        }
        adapter.craftwList = showList;
        adapter.notifyDataSetChanged();
    }

    public void set_Selected(int id,int type,View v) {
        switch (type){
            case 0:
                if(select_type.indexOf(id) != -1){ // remove
                    select_type.remove(select_type.indexOf(id));
                    v.setBackgroundResource(R.drawable.item_btn);
                }else{                              // checked
                    select_type.add(id);
                    v.setBackgroundResource(R.drawable.item_btn_checked);
                }
                Collections.sort(select_type);
                break;
            case 1:
                if(select_star.indexOf(id) != -1){
                    select_star.remove(select_star.indexOf(id));
                    v.setBackgroundResource(R.drawable.item_btn);
                }else{
                    select_star.add(id);
                    v.setBackgroundResource(R.drawable.item_btn_checked);
                }
                Collections.sort(select_star);
                break;
            case 2:
                if(select_mode.indexOf(id) != -1){
                    select_mode.remove(select_mode.indexOf(id));
                    v.setBackgroundResource(R.drawable.item_btn);
                }else{
                    select_mode.add(id);
                    v.setBackgroundResource(R.drawable.item_btn_checked);
                }
                Collections.sort(select_mode);
                break;
        }
        filter_Card();
    }

    public void set_Id() {
        id_mode = new int[type_mode.length];
        for(int i=0;i<type_mode.length;i++){
            id_mode[i] =  getResources().getIdentifier("mode_" + (i+1), "id", getPackageName());
        }
        id_race = new int[type_race.length];
        for(int i=0;i<type_race.length;i++){
            id_race[i] =  getResources().getIdentifier("race_" + (i+1), "id", getPackageName());
        }
        id_star = new int[type_star.length];
        for(int i=0;i<type_star.length;i++){
            id_star[i] =  getResources().getIdentifier("star_" + (i+1), "id", getPackageName());
        }
        id_type = new int[type_type.size()];
        for(int i=0;i<type_type.size();i++){
            id_type[i] =  getResources().getIdentifier("cw_" + (i+1), "id", getPackageName());
        }
    }

}

