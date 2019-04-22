package myapplication.example.com.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import myapplication.example.com.myapplication.adapter.ItemAdapter;
import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.views.ListCraftActivity;

public class CraftActivity extends Activity {
    public TextView tv_id,tv_name,tv_star,tv_attr,tv_race,tv_type,tv_power,tv_skill_1,tv_skill_2,tv_skill_3;
    public String tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_type,tmp_power,tmp_skill_1,tmp_skill_2,tmp_skill_3,tmp_tag,tmp_tag_power;
    public SQLiteDatabase db;
    public ItemAdapter adapter,adapter2;
    public static final String DATABASE_NAME = "tos.db";
    public static final String Table_Name = "craft";
    public ArrayList<String> craftSkill = new ArrayList<String>();
    public ArrayList<String> craftSkills = new ArrayList<String>();
    public ArrayList<String> craftTag = new ArrayList<String>();
    public ArrayList<String> craftTags = new ArrayList<String>();
    public String table_skill[][];
    public int tmp_img;
    public int use_color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craft);

        // Get ID
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;

        // Set Items

        // Get Data
        db = openOrCreateDatabase(DATABASE_NAME,  Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("SELECT * FROM " + Table_Name + " WHERE craft_id = '" + id + "'", null);
        if (c.getCount()>0){
            c.moveToFirst();
            do{
                tmp_id =  c.getString(0);
                tmp_name =  c.getString(1);
                tmp_attr =  c.getString(2);
                tmp_race =  c.getString(3);
                tmp_star =  c.getString(4);
                tmp_type = c.getString(5);
                tmp_power = c.getString(6);
                tmp_skill_1 =  c.getString(7);
                tmp_skill_2 =  c.getString(8);
                tmp_skill_3 =  c.getString(9);
                tmp_tag = c.getString(10);
                tmp_img = getResources().getIdentifier("rk_" + tmp_id, "drawable", getPackageName());
            } while(c.moveToNext());
        }

        db = openOrCreateDatabase(DATABASE_NAME,  Context.MODE_PRIVATE, null);
        c=db.rawQuery("SELECT * FROM craft_tag WHERE tag_id IN (" + tmp_tag+ ") ORDER BY CAST(tag_id AS INT) ASC", null);
        if (c.getCount()>0){
            c.moveToFirst();
            do{
                if(c.getString(2).equals("skill")){
                    craftSkill.add(c.getString(0));
                    craftSkills.add(c.getString(1));
                }else{
                    craftTag.add(c.getString(0));
                    craftTags.add(c.getString(1));
                }
            } while(c.moveToNext());
        }

        // Set Image
        ImageView imageView = (ImageView) findViewById(R.id.cardImg);
        imageView.setImageResource(tmp_img);
        int set = (screenWidth-10)/5-12;
        imageView.getLayoutParams().width = set;
        imageView.getLayoutParams().height = set;
        tv_id = (TextView) findViewById(R.id.craftId);
        tv_name = (TextView) findViewById(R.id.craftName);
        tv_type =  (TextView) findViewById(R.id.craftType);
        tv_star = (TextView) findViewById(R.id.craftStar);
        tv_attr = (TextView) findViewById(R.id.craftAttr);
        tv_race = (TextView) findViewById(R.id.craftRace);
        tv_power = (TextView) findViewById(R.id.craftPower);
        tv_id.setText("No." + tmp_id);
        tv_name.setText(tmp_name);
        tv_type.setText(tmp_type);
        tv_star.setText(tmp_star + "★");
        tv_attr.setText(tmp_attr);
        tv_race.setText(tmp_race);
        tv_power.setText(tmp_power);

        int num = Integer.parseInt(tmp_star);
        table_skill = new String[num][3];
        for(int i=0;i<num;i++){
            String table_data[] = new String[2];
            String num_sign = "";
            if(i==0){ table_data = tmp_skill_1.split("#"); num_sign = "I";}
            else if(i==1){ table_data = tmp_skill_2.split("#"); num_sign = "II"; }
            else if(i==2){ table_data = tmp_skill_3.split("#"); num_sign = "III"; }
            for(int j=0;j<3;j++){
                if(j==0){ table_skill[i][j] = num_sign; }
                else if(j==1){ table_skill[i][j] = table_data[0]; }
                else if(j==2){ table_skill[i][j] = table_data[1]; }
            }
        }
        TableLayout skill_table = (TableLayout) findViewById(R.id.table_skill);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        String table_title[] = {"等級","效果","發動分數"};
        for (int i=0; i<table_title.length; i++) {
            TextView text = new TextView(this);
            text.setText(table_title[i]);
            text.setTextSize(14);
            text.setPadding(15,10,15,10);
            text.setBackgroundColor(Color.GRAY);
            text.setGravity(Gravity.CENTER);
            text.setTextColor(Color.WHITE);;
            tr.addView(text,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        }skill_table.addView(tr);

        for(int i=0;i<num;i++){
            tr = new TableRow(this);
            TableRow.LayoutParams trs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            trs.setMargins(2, 2, 2, 2);
            tr.setLayoutParams(trs);
            for(int j=0;j<3;j++){
                TextView text = new TextView(this);
                text.setText(table_skill[i][j]);
                text.setTextSize(15);
                text.setSingleLine(false);
                if(j==0) { text.setBackgroundColor(Color.parseColor("#AAAAAA")); text.setTextColor(Color.WHITE);}
                else{ text.setBackgroundColor(Color.WHITE); }
                text.setPadding(10,10,10,10);
                text.setMaxLines(20);
                TableRow.LayoutParams txs;
                if(j==1) {
                    text.setGravity(Gravity.CENTER_VERTICAL);
                    txs = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    txs.setMargins(1, 2, 1, 2);
                    text.setPadding(20,20,20,20);
                    tr.addView(text,txs);
                }else{
                    text.setGravity(Gravity.CENTER);
                    txs = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                    if(j==0) { txs.setMargins(4, 2, 2, 2); }
                    else{ txs.setMargins(2, 2, 4, 2); }
                    tr.addView(text,txs);
                }
                //Log.d("tag",table_skill[i][j]);
            } skill_table.addView(tr);
        }

        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.tagCraftSkill);
        for(int i=0;i<craftSkill.size();i++){
            TextView textview=new TextView(this);
            textview.setText(craftSkills.get(i));
            textview.setTextColor(Color.WHITE);
            textview.setBackgroundResource(R.drawable.chip_tag);
            GradientDrawable background = (GradientDrawable)textview.getBackground();
            background.setColor(Color.parseColor("#FF4081"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i==0){ params.setMargins(0, 0, 0, 0); }
            else{ params.setMargins(15, 0, 0, 0); }
            textview.setLayoutParams(params);
            final String tmp_tag_id = craftSkill.get(i);
            textview.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(),ListCraftActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",tmp_tag_id);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }});
            mainLinerLayout.addView(textview);
        }

        LinearLayout mainLinerLayout2 = (LinearLayout) this.findViewById(R.id.tagCraftPower);
        for(int i=0;i<craftTag.size();i++){
            TextView textview=new TextView(this);
            textview.setText(craftTags.get(i));
            textview.setTextColor(Color.WHITE);
            textview.setBackgroundResource(R.drawable.chip_tag);
            GradientDrawable background = (GradientDrawable)textview.getBackground();
            background.setColor(Color.parseColor("#FF4081"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i==0){ params.setMargins(0, 0, 0, 0); }
            else{ params.setMargins(15, 0, 0, 0); }
            textview.setLayoutParams(params);
            final String tmp_tag_id = craftTag.get(i);
            textview.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(),ListCraftActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",tmp_tag_id);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }});
            mainLinerLayout2.addView(textview);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}
