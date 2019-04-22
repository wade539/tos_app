package myapplication.example.com.myapplication.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import myapplication.example.com.myapplication.object.Card;
import myapplication.example.com.myapplication.adapter.ImageAdapter;
import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.view.ActiveActivity;
import myapplication.example.com.myapplication.view.CraftTagActivity;
import myapplication.example.com.myapplication.view.LeaderActivity;

public class MainActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "tos.db";
    public static final String Table_Name = "card";
    public SQLiteDatabase db;
    public ImageAdapter adapter;
    public int[]  id_attr,id_race,id_star;
    public ArrayList<Card> showList = new ArrayList<Card>();
    public ArrayList<Card> sortList = new ArrayList<Card>();
    public ArrayList<Card> cardList = new ArrayList<Card>();
    public ArrayList<Integer> select_attr = new ArrayList<Integer>();
    public ArrayList<Integer> select_race = new ArrayList<Integer>();
    public ArrayList<Integer> select_star = new ArrayList<Integer>();
    public String[] type_attr = {"水","火","木","光","暗"};
    public String[] icon_attr = {"water","fire","wood","light","dark"};
    public String[] type_race = {"神族","魔族","人類","獸類","龍類","妖精類","機械族","進化素材","強化素材"};
    public String[] icon_race = {"god","demon","human","beast","dragon","elf","machina","evolve","level"};
    public String[] type_star = {"1","2","3","4","5","6","7","8"};
    public String sortType = "ById";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view_left;
    private TableLayout grid_attr,grid_race,grid_star;
    private RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 設定UI :
        // 預設資料庫
        db = openOrCreateDatabase(DATABASE_NAME,  Context.MODE_PRIVATE, null);
        // 標題欄
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("簡易圖鑑");
        toolbar.inflateMenu(R.menu.filter);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    // 觸發滑動側欄(右)
                    case R.id.menu_filter:
                        drawerLayout.openDrawer(GravityCompat.END);
                        break;
                }
                return false;
            }
        });
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
                if (id == R.id.active_page) {
                    Intent intent = new Intent(MainActivity.this,ActiveActivity.class);
                    startActivity(intent);
                }else if (id == R.id.leader_page) {
                    Intent intent = new Intent(MainActivity.this,LeaderActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craft_page) {
                    Intent intent = new Intent(MainActivity.this,CraftsActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craft_tag_page) {
                    Intent intent = new Intent(MainActivity.this,CraftTagActivity.class);
                    startActivity(intent);
                }else if (id == R.id.craftw_page) {
                    Intent intent = new Intent(MainActivity.this,CraftwsActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        // 右側欄背景設定
        View v = findViewById(R.id.filter);
        v.getBackground().setAlpha(100);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
        // 右側欄選定按鈕設定
        rg = (RadioGroup)findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sort_List();
            }
        });

        // 取得使用介面視窗大小
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;

        set_Id(); // 預設選擇按鈕之ID
        filter_Item(); // 預設圖鑑篩選介面
        imporDatabase(); // 引入資料庫

        // 從資料庫抓取資料 : 卡片資料
        Cursor c=db.rawQuery("SELECT * FROM " + Table_Name, null);
        if (c.getCount()>0){
            String tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_space,tmp_life,tmp_atk,tmp_reply,tmp_sum;
            int tmp_img;
            Card tmp_card;
            c.moveToFirst();
            do{
                tmp_id =  c.getString(0);
                tmp_name =  c.getString(1);
                tmp_star =  c.getString(2);
                tmp_attr =  c.getString(3);
                tmp_race =  c.getString(4);
                tmp_space =  c.getString(5);
                tmp_life =  c.getString(7);
                tmp_atk =  c.getString(8);
                tmp_reply =  c.getString(9);
                tmp_sum = c.getString(10);
                tmp_img = getResources().getIdentifier("pic_" + tmp_id, "drawable", getPackageName());
                tmp_card = new Card(tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_space,tmp_life,tmp_atk,tmp_reply,tmp_sum,tmp_img);
                cardList.add(tmp_card);
            } while(c.moveToNext());
            showList = cardList;
        }

        // 利用圖示列表呈現圖鑑介面
        adapter = new ImageAdapter(this,screenWidth,showList,sortType);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
    }
    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawerLayout);
        if (layout.isDrawerOpen(GravityCompat.START) ||layout.isDrawerOpen(GravityCompat.END)) {
            layout.closeDrawer(GravityCompat.START);
            layout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
    // 預設圖鑑篩選介面 : 表格與選擇按鈕功能觸發
    public void filter_Item() {
        // 屬性篩選介面
        grid_attr = (TableLayout) findViewById(R.id.grid_attr);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        for (int i=0; i<type_attr.length; i++) {
            ImageView b = new ImageView (this);
            b.setId(id_attr[i]);
            b.setScaleType(ImageView.ScaleType.CENTER_CROP);
            b.setPadding(10, 10, 10, 10);
            b.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("attr_" + icon_attr[i], "drawable", getPackageName())));
            b.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    set_Selected(v.getId(),0,v);
                }
            });
            tr.addView(b,120,120);
        }
        grid_attr.addView(tr);
        // 種族篩選介面
        grid_race = (TableLayout) findViewById(R.id.grid_race);
        for (int i=0; i<type_race.length/5+1; i++) {
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 5*i; j < 5*(i+1); j++) {
                if(j < type_race.length){
                    ImageView b = new ImageView (this);
                    b.setId(id_race[j]);
                    b.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    b.setPadding(10, 10, 10, 10);
                    b.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("race_" + icon_race[j], "drawable", getPackageName())));
                    b.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            set_Selected(v.getId(), 1, v);
                        }
                    });
                    tr.addView(b,120,120);
                }
            }
            grid_race.addView(tr);
        }
        // 稀有度篩選介面
        grid_star = (TableLayout) findViewById(R.id.grid_star);
        for (int i=0; i<type_star.length/4; i++) {
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 4*i; j < 4*(i+1); j++) {
                Button b = new Button(this);
                b.setText(type_star[j]);
                b.setTextSize(15);
                b.setTextColor(Color.parseColor("#dbcb88"));
                b.setBackgroundResource(R.drawable.item_btn);
                b.setId(id_star[j]);
                b.setPadding(10,10,10,10);
                b.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        set_Selected(v.getId(), 2, v);
                    }
                });
                tr.addView(b,100,100);
            }
            grid_star.addView(tr);
        }
    }

    // 選擇按鈕功能觸發 : 依所選條件篩選並顯示
    public void filter_Card() {
        ArrayList<String> attrs = new ArrayList<String>();
        ArrayList<String> races = new ArrayList<String>();
        ArrayList<String> stars = new ArrayList<String>();
        showList = new ArrayList<Card>();
        // 屬性項目記錄
        if(select_attr.size() != 0){
            for(int i=0;i<select_attr.size();i++){
                for(int j=0;j<id_attr.length;j++){
                    if(select_attr.get(i) == id_attr[j]){
                        attrs.add(type_attr[j]);
                    }
                }
            }
        }else{
            for(int i=0;i<type_attr.length;i++){
                attrs.add(type_attr[i]);
            }
        }
        // 種族項目記錄
        if(select_race.size() != 0){
            for(int i=0;i<select_race.size();i++){
                for(int j=0;j<id_race.length;j++){
                    if(select_race.get(i) == id_race[j]){
                        races.add(type_race[j]);
                    }
                }
            }
        }else{
            for(int i=0;i<type_race.length;i++){
                races.add(type_race[i]);
            }
        }
        // 稀有度項目記錄
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
        // 記錄所選項目進行篩選
        for(int v=0;v<cardList.size();v++){ // all cards
            for(int i=0;i<attrs.size();i++){ // type attr
                for(int j=0;j<races.size();j++){ // type race
                    for(int k=0;k<stars.size();k++){ // type star
                        if(cardList.get(v).get_attr().equals(attrs.get(i)) &&
                                cardList.get(v).get_race().equals(races.get(j)) &&
                                cardList.get(v).get_star().equals(stars.get(k))){
                            showList.add(cardList.get(v));
                        }
                    }
                }
            }
        }
        // 更新顯示列表
        adapter.cardList = showList;
        adapter.showType = sortType;
        adapter.notifyDataSetChanged();
    }

    // 按鈕選擇觸發 : 介面顯示變化並執行篩選
    public void set_Selected(int id,int type,View v) {
        switch (type){
            case 0:
                if(select_attr.indexOf(id) != -1){ // remove
                    select_attr.remove(select_attr.indexOf(id));
                    v.setBackgroundColor(0x00000000);
                    v.getBackground().setAlpha(100);
                }else{                              // checked
                    select_attr.add(id);
                    v.setBackgroundColor(Color.parseColor("#009FCC"));
                }
                Collections.sort(select_attr);
                break;
            case 1:
                if(select_race.indexOf(id) != -1){
                    select_race.remove(select_race.indexOf(id));
                    v.setBackgroundColor(0x00000000);
                    v.getBackground().setAlpha(100);
                }else{
                    select_race.add(id);
                    v.setBackgroundColor(Color.parseColor("#009FCC"));
                }
                Collections.sort(select_race);
                break;
            case 2:
                if(select_star.indexOf(id) != -1){
                    select_star.remove(select_star.indexOf(id));
                    v.setBackgroundResource(R.drawable.item_btn);
                }else{
                    select_star.add(id);
                    v.setBackgroundResource(R.drawable.item_btn_checked);
                }
                Collections.sort(select_star);
                break;
        }
        // 執行篩選功能
        filter_Card();
    }

    // 預設選擇按鈕之ID
    public void set_Id() {
        id_attr = new int[type_attr.length];
        for(int i=0;i<type_attr.length;i++){
            id_attr[i] =  getResources().getIdentifier("attr_" + (i+1), "id", getPackageName());
        }
        id_race = new int[type_race.length];
        for(int i=0;i<type_race.length;i++){
            id_race[i] =  getResources().getIdentifier("race_" + (i+1), "id", getPackageName());
        }
        id_star = new int[type_star.length];
        for(int i=0;i<type_star.length;i++){
            id_star[i] =  getResources().getIdentifier("star_" + (i+1), "id", getPackageName());
        }
    }

    // 排序方式選擇 : 可選選擇列表顯示之依據
   public  void sort_List(){
        String sql_set = "";
        // 依選擇項目執行 : 設置資料庫排序條件
        switch(rg.getCheckedRadioButtonId()){
            case R.id.sort_id:
                sql_set = ""; sortType = "ById";
                break;
            case R.id.sort_star:
                sql_set = " ORDER BY CAST(star AS INT) DESC";  sortType = "ByStar";
                break;
            case R.id.sort_life:
                sql_set = " ORDER BY CAST(life AS INT) DESC";  sortType = "ByLife";
                break;
            case R.id.sort_atk:
                sql_set = " ORDER BY CAST(attack AS INT) DESC";  sortType = "ByAtk";
                break;
            case R.id.sort_reply:
                sql_set = " ORDER BY CAST(reply AS INT) DESC";  sortType = "ByReply";
                break;
            case R.id.sort_sum:
                sql_set = " ORDER BY CAST(sum AS INT) DESC";  sortType = "BySum";
                break;
            case R.id.sort_space:
                sql_set = " ORDER BY CAST(space AS INT) DESC";  sortType = "BySpace";
                break;
        }
        // 資料庫依排序條件抓取資料
        Cursor c=db.rawQuery("SELECT * FROM " + Table_Name + sql_set, null);
        ArrayList<Card> tmpList = new ArrayList<Card>();
        if (c.getCount()>0){
            String tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_space,tmp_life,tmp_atk,tmp_reply,tmp_sum;
            int tmp_img;
            Card tmp_card;
            c.moveToFirst();
            do{
                tmp_id =  c.getString(0);
                tmp_name =  c.getString(1);
                tmp_star =  c.getString(2);
                tmp_attr =  c.getString(3);
                tmp_race =  c.getString(4);
                tmp_space =  c.getString(5);
                tmp_life =  c.getString(7);
                tmp_atk =  c.getString(8);
                tmp_reply =  c.getString(9);
                tmp_sum =  c.getString(10);
                tmp_img = getResources().getIdentifier("pic_" + tmp_id, "drawable", getPackageName());
                tmp_card = new Card(tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_space,tmp_life,tmp_atk,tmp_reply,tmp_sum,tmp_img);
                tmpList.add(tmp_card);
            } while(c.moveToNext());
            cardList = tmpList;
        }
        // 執行篩選功能
        filter_Card();
    }
    // 引入資料庫
    public void imporDatabase() {
        // File Path
        String dirPath="/data/data/myapplication.example.com.myapplication/databases";
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdir();
        }
        // DateBase
        File file = new File(dir, "tos.db");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            // Import DataBase
            InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.tos);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffere=new byte[is.available()];
            is.read(buffere);
            fos.write(buffere);
            is.close();
            fos.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
