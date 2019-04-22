package myapplication.example.com.myapplication.views;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.ArrayList;
import java.util.Collections;

import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.adapter.ImageAdapter;
import myapplication.example.com.myapplication.object.Card;
import myapplication.example.com.myapplication.view.ActiveActivity;

public class ListActivity extends AppCompatActivity {
    public ArrayList<Card> showList = new ArrayList<Card>();
    public ArrayList<Card> cardList = new ArrayList<Card>();
    public ArrayList<Integer> select_attr = new ArrayList<Integer>();
    public ArrayList<Integer> select_race = new ArrayList<Integer>();
    public ArrayList<Integer> select_star = new ArrayList<Integer>();
    public String[] type_attr = {"水","火","木","光","暗"};
    public String[] icon_attr = {"water","fire","wood","light","dark"};
    public String[] type_race = {"神族","魔族","人類","獸類","龍類","妖精類","機械族","進化素材","強化素材"};
    public String[] icon_race = {"god","demon","human","beast","dragon","elf","machina","evolve","level"};
    public String[] type_star = {"1","2","3","4","5","6","7","8"};
    public int[] id_attr, id_race, id_star;
    public ImageAdapter adapter;
    public SQLiteDatabase db;
    public static final String DATABASE_NAME = "tos.db";
    public static final String Card_Table = "card";
    public String active_id,active_name,active_monster;
    public String tb_name;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view_left;
    private  TableLayout grid_attr,grid_race,grid_star;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Data
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        int type = bundle.getInt("type");
        if(type == 0){ tb_name = "active"; }
        else if(type == 1){ tb_name = "leader"; }

        // Set Items
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.filter);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigation_view_left = (NavigationView) findViewById(R.id.navigation_view_left);
        navigation_view_left.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.active_page) {
                    Intent intent = new Intent(ListActivity.this, ActiveActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
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

        set_Id(); // Set Filter Items
        filter_Item(); // Set Filter items

        // Get Data
        // Get Skill Data
        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("SELECT card_id, active.active_id, active.active_name FROM active_map LEFT OUTER JOIN active ON active.active_id = active_map.active_id WHERE active_map.active_id = " + id +";",null);
        //Cursor c=db.rawQuery("SELECT * FROM " + tb_name + " WHERE " + tb_name + "_id = '" + id + "'", null);
        if (c.getCount() > 0) {
            active_monster = "";
            c.moveToFirst();
            do {
                active_monster += "," + c.getString(0);
                active_id = c.getString(1);
                active_name = c.getString(2);
            } while (c.moveToNext());
            active_monster = active_monster.substring(1);
        }
        toolbar.setTitle(active_name);

        // Get Card Data
        c=db.rawQuery("SELECT * FROM " + Card_Table + " WHERE card_id IN (" +  active_monster + ") ORDER BY CAST(card_id AS INT) ASC", null);
        if (c.getCount() > 0) {
            String tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_space,tmp_life,tmp_atk,tmp_reply,tmp_sum;
            int tmp_img;
            Card tmp_card;
            c.moveToFirst();
            do {
                tmp_id =  c.getString(0);
                tmp_name =  c.getString(1);
                tmp_star =  c.getString(2);
                tmp_attr =  c.getString(3);
                tmp_race =  c.getString(4);
                tmp_space =  c.getString(5);
                tmp_life =  c.getString(7);
                tmp_atk =  c.getString(8);
                tmp_reply =  c.getString(9);
                tmp_sum = Integer.toString(Integer.parseInt(tmp_life) + Integer.parseInt(tmp_atk) + Integer.parseInt(tmp_reply));
                tmp_img = getResources().getIdentifier("pic_" + tmp_id, "drawable", getPackageName());
                tmp_card = new Card(tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_space,tmp_life,tmp_atk,tmp_reply,tmp_sum,tmp_img);
                cardList.add(tmp_card);
            } while (c.moveToNext());
            showList = cardList;
        }

        adapter = new ImageAdapter(this, screenWidth, showList,"ById");
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
    }

    public void filter_Item() {
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

    public void filter_Card() {
        //int to string
        ArrayList<String> attrs = new ArrayList<String>();
        ArrayList<String> races = new ArrayList<String>();
        ArrayList<String> stars = new ArrayList<String>();
        showList = new ArrayList<Card>();
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
        for(int v=0;v<cardList.size();v++){ // all cards
            for(int i=0;i<attrs.size();i++){ // type attr
                for(int j=0;j<races.size();j++){ // type race
                    for(int k=0;k<stars.size();k++){ // type star
                        if(cardList.get(v).get_attr().equals(attrs.get(i)) && cardList.get(v).get_race().equals(races.get(j)) && cardList.get(v).get_star().equals(stars.get(k))){
                            showList.add(cardList.get(v));
                        }
                    }
                }
            }
        }
        adapter.cardList = showList;
        adapter.notifyDataSetChanged();
    }

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
        filter_Card();
    }

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
}
