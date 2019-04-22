package myapplication.example.com.myapplication.views;

import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Collections;

import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.adapter.ImageAdapter2;
import myapplication.example.com.myapplication.object.Craft;

/**
 * Created by user on 2018/11/1.
 */
public class ListCraftActivity extends AppCompatActivity {
    public int[]  id_attr,id_race,id_star,id_mode;
    public ArrayList<Craft> showList = new ArrayList<Craft>();
    public ArrayList<Craft> craftList = new ArrayList<Craft>();
    public ArrayList<Integer> select_mode = new ArrayList<Integer>();
    public ArrayList<Integer> select_attr = new ArrayList<Integer>();
    public ArrayList<Integer> select_race = new ArrayList<Integer>();
    public ArrayList<Integer> select_star = new ArrayList<Integer>();
    public String[] type_mode = {"連鎖龍紋", "轉動龍印", "破碎龍咒", "映照龍符", "疾速龍玉", "裂空龍刃", "落影龍璃", "擴散龍結", "鏡像龍丸"};
    public String[] type_attr = {"水","火","木","光","暗"};
    public String[] icon_attr = {"water","fire","wood","light","dark"};
    public String[] type_race = {"神族","魔族","人類","獸類","龍類","妖精類","機械族"};
    public String[] icon_race = {"god","demon","human","beast","dragon","elf","machina"};
    public String[] type_star = {"1","2","3"};
    public ImageAdapter2 adapter;
    public SQLiteDatabase db;
    public static final String DATABASE_NAME = "tos.db";
    public static final String Craft_Table = "craft";
    public String tag_id,tag_name,tag_craft;
    public String tb_name = "craft_tag";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view_left;
    private TableLayout grid_attr,grid_race,grid_star,grid_mode;
    private CheckBox chkattr,chkrace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_craft);

        // Set Data
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

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
        chkattr = (CheckBox) findViewById(R.id.chkattr);
        chkrace = (CheckBox) findViewById(R.id.chkrace);
        chkattr.setChecked(true);
        chkrace.setChecked(true);

        // Set Data
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;

        set_Id(); // Set Filter Items
        filter_Item(); // Set Filter items

        // Get Data
        // Get Skill Data
        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("SELECT * FROM " + tb_name + " WHERE tag_id = '" + id + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                tag_id = c.getString(0);
                tag_name = c.getString(1);
                tag_craft = c.getString(3);
            } while (c.moveToNext());
        }
        toolbar.setTitle(tag_name);

        // Get Card Data
        c=db.rawQuery("SELECT * FROM " + Craft_Table + " WHERE craft_id IN (" +  tag_craft + ") ORDER BY CAST(craft_id AS INT) ASC", null);
        if (c.getCount() > 0) {
            String tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_type,tmp_power,tmp_skill_1,tmp_skill_2,tmp_skill_3;;
            int tmp_img;
            Craft tmp_craft;
            c.moveToFirst();
            do {
                tmp_id =  c.getString(0);
                tmp_name =  c.getString(1);
                tmp_attr =  c.getString(2);
                tmp_race =  c.getString(3);
                tmp_star =  c.getString(4);
                tmp_type =  c.getString(5);
                tmp_power =  c.getString(6);
                tmp_skill_1 =  c.getString(7);
                tmp_skill_2 =  c.getString(8);
                tmp_skill_3 =  c.getString(9);
                tmp_img = getResources().getIdentifier("rk_" + tmp_id, "drawable", getPackageName());
                tmp_craft = new Craft(tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_type,tmp_power,tmp_skill_1,tmp_skill_2,tmp_skill_3,tmp_img);
                craftList.add(tmp_craft);
            } while (c.moveToNext());
            showList = craftList;
        }

        adapter = new ImageAdapter2(this, screenWidth, showList);
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
                            set_Selected(v.getId(), 3, v);
                        }
                    });
                    tr.addView(b,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
            grid_mode.addView(tr);
        }
        grid_attr = (TableLayout) findViewById(R.id.grid_attr);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
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
        for (int i=0; i<type_race.length/4+1; i++) {
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 4*i; j < 4*(i+1); j++) {
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

            chkattr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    filter_Card();
                }
            });

            chkrace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    filter_Card();
                }
            });
        }

        grid_star = (TableLayout) findViewById(R.id.grid_star);
        tr = new TableRow(this);
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
                    set_Selected(v.getId(), 2, v);
                }
            });
            tr.addView(b,100,100);
        }
        grid_star.addView(tr);
    }

    public void filter_Card() {
        //int to string
        ArrayList<String> attrs = new ArrayList<String>();
        ArrayList<String> races = new ArrayList<String>();
        ArrayList<String> stars = new ArrayList<String>();
        ArrayList<String> modes = new ArrayList<String>();
        showList = new ArrayList<Craft>();
        if(select_attr.size() != 0){
            for(int i=0;i<select_attr.size();i++){
                for(int j=0;j<id_attr.length;j++){
                    if(select_attr.get(i) == id_attr[j]){
                        attrs.add("適用於"+type_attr[j]+"屬性成員");
                    }
                }
            }
        }else{
            for(int i=0;i<type_attr.length;i++){
                attrs.add("適用於"+type_attr[i]+"屬性成員");
            }
        }
        if(select_race.size() != 0){
            for(int i=0;i<select_race.size();i++){
                for(int j=0;j<id_race.length;j++){
                    if(select_race.get(i) == id_race[j]){
                        races.add("適用於"+type_race[j]+"成員");
                    }
                }
            }
        }else{
            for(int i=0;i<type_race.length;i++){
                races.add("適用於"+type_race[i]+"成員");
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
        if(chkattr.isChecked()){ attrs.add("沒有限制"); }
        if(chkrace.isChecked()){ races.add("沒有限制"); }
        for(int v=0;v<craftList.size();v++){ // all crafts
            for(int m=0;m<modes.size();m++){ // type mode
                for(int i=0;i<attrs.size();i++){ // type attr
                    for(int j=0;j<races.size();j++){ // type race
                        for(int k=0;k<stars.size();k++){ // type star
                            if(craftList.get(v).get_type().equals(modes.get(m)) &&
                                    craftList.get(v).get_attr().equals(attrs.get(i)) &&
                                    craftList.get(v).get_race().equals(races.get(j)) &&
                                    craftList.get(v).get_star().equals(stars.get(k))){
                                showList.add(craftList.get(v));
                            }
                        }
                    }
                }
            }
        }
        adapter.craftList = showList;
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
            case 3:
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

