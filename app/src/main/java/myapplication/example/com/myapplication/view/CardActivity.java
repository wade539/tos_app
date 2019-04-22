package myapplication.example.com.myapplication.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

import myapplication.example.com.myapplication.adapter.ImageAdapter;
import myapplication.example.com.myapplication.adapter.ItemAdapter;
import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.object.Card;
import myapplication.example.com.myapplication.views.ListActivity;

public class CardActivity extends Activity {
    public TextView tv_id,tv_name,tv_series,tv_imgb,tv_evolution,tv_ame;
    public TextView tv_active,tv_active_skill,tv_cd,tv_leader,tv_leader_skill,tv_active_2,tv_active_skill_2,tv_cd_2;
    public String tmp_id,tmp_name,tmp_star,tmp_attr,tmp_race,tmp_space,tmp_series,tmp_life,tmp_reply,tmp_attack,tmp_sum;
    public String tmp_active,tmp_active_skill,tmp_cd,tmp_leader,tmp_leader_skill,tmp_active_tag,tmp_leader_tag,tmp_story,tmp_info,tmp_etp;
    public String tmp_soul_1,tmp_level_1,tmp_soul_2,tmp_level_2,tmp_soul_3,tmp_level_3,tmp_soul_4,tmp_level_4,tmp_ame_active,tmp_ame_active_skill,tmp_ame_cdmax,tmp_ame_leader,tmp_ame_leader_skill;
    public SQLiteDatabase db;
    public int screenWidth;
    public ItemAdapter adapter,adapter2;
    public static final String DATABASE_NAME = "tos.db";
    public static final String Table_Name = "card";
    public String[] acts,act_skills,cdmaxs,tmp_sources;
    public ArrayList<String> titleList = new ArrayList<String>();
    public ArrayList<String> dataList = new ArrayList<String>();
    public ArrayList<String> titleList2 = new ArrayList<String>();
    public ArrayList<String> dataList2 = new ArrayList<String>();
    public ArrayList<String> activeTag = new ArrayList<String>();
    public ArrayList<String> leaderTag = new ArrayList<String>();
    public ArrayList<String> activeTags = new ArrayList<String>();
    public ArrayList<String> leaderTags = new ArrayList<String>();
    public ArrayList<String> ameList = new ArrayList<String>();
    public ArrayList<String> tmp_cid = new ArrayList<String>();
    public ArrayList<String> tmp_before = new ArrayList<String>();
    public ArrayList<String> tmp_after = new ArrayList<String>();
    public ArrayList<String> tmp_source = new ArrayList<String>();
    public int tmp_img;
    public int use_color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //預設資料庫
        db = openOrCreateDatabase(DATABASE_NAME,  Context.MODE_PRIVATE, null);

        // 取得卡片ID
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

        // 取得使用介面視窗大小
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;

        // 設定數據標題顯示欄
        titleList.add("屬性");
        titleList.add("種族");
        titleList.add("稀有度");
        titleList.add("空間");
        titleList2.add("生命力");
        titleList2.add("攻擊力");
        titleList2.add("回復力");
        titleList2.add("三圍");
        ImageView imageStory = (ImageView) findViewById(R.id.cardStory);
        ImageView imageSkin = (ImageView) findViewById(R.id.cardSkin);

        // 從資料庫抓取資料 : 召喚獸資料
        Cursor c_card=db.rawQuery("SELECT * FROM " + Table_Name + " WHERE card_id = '" + id + "'", null);
        if (c_card.getCount()>0){
            c_card.moveToFirst();
            do{
                tmp_id =  c_card.getString(0);
                tmp_name =  c_card.getString(1);
                tmp_star =  c_card.getString(2) + "★";
                tmp_attr =  c_card.getString(3);
                tmp_race =  c_card.getString(4);
                tmp_space = c_card.getString(5);
                tmp_series = c_card.getString(6);
                tmp_life =  c_card.getString(7);
                tmp_reply =  c_card.getString(8);
                tmp_attack =  c_card.getString(9);
                tmp_sum =  c_card.getString(10);
                tmp_active =  c_card.getString(11);
                tmp_active_skill = c_card.getString(12).replace("<br/>","\n");
                tmp_cd = c_card.getString(13);
                tmp_leader = c_card.getString(14);
                tmp_leader_skill = c_card.getString(15).replace("<br/>","\n");;
                tmp_active_tag = c_card.getString(16);
                tmp_leader_tag = c_card.getString(17);
                dataList.add(tmp_attr);
                dataList.add(tmp_race);
                dataList.add(tmp_star);
                dataList.add(tmp_space);
                dataList2.add(tmp_life);
                dataList2.add(tmp_reply);
                dataList2.add(tmp_attack);
                dataList2.add(tmp_sum);
                tmp_img = getResources().getIdentifier("pic_" + tmp_id, "drawable", getPackageName());
            } while(c_card.moveToNext());
        }
        acts = tmp_active.split("#");
        act_skills = tmp_active_skill.split("#");
        cdmaxs = tmp_cd.split("#");

        // 從資料庫抓取資料 : 主動技能資料
        Cursor c_active=db.rawQuery("SELECT * FROM active WHERE active_id IN (" + tmp_active_tag+ ") ORDER BY CAST(active_id AS INT) ASC", null);
        if (c_active.getCount()>0){
            c_active.moveToFirst();
            do{
                activeTag.add(c_active.getString(0));
                activeTags.add(c_active.getString(1));
            } while(c_active.moveToNext());
        }

        // 從資料庫抓取資料 : 隊長技能資料
        Cursor c_leader=db.rawQuery("SELECT * FROM leader WHERE leader_id IN (" + tmp_leader_tag + ") ORDER BY CAST(leader_id AS INT) ASC", null);
        if (c_leader.getCount()>0){
            c_leader.moveToFirst();
            do{
                leaderTag.add(c_leader.getString(0));
                leaderTags.add(c_leader.getString(1));
            } while(c_leader.moveToNext());
        }

        // 從資料庫抓取資料 : 故事資料
        Cursor c_story=db.rawQuery("SELECT * FROM story WHERE card_id = '" + id + "'", null);
        if (c_story.getCount()>0){
            c_story.moveToFirst();
            do{
                tmp_story = c_story.getString(1);
            } while(c_story.moveToNext());
        }else{ imageStory.setVisibility(View.GONE); }

        // 從資料庫抓取資料 : 進化資料
        TextView evo_tv =  (TextView) findViewById(R.id.cardEvolution);
        Cursor c_evo=db.rawQuery("SELECT * FROM evo WHERE card_id = '" + id + "'ORDER BY cast(after as int) ASC ", null);
        if (c_evo.getCount()>0){
            c_evo.moveToFirst();
            do{
                tmp_cid.add(c_evo.getString(0));
                tmp_before.add(c_evo.getString(1));
                tmp_after.add(c_evo.getString(2));
                tmp_source.add(c_evo.getString(3));
                tmp_etp =  c_evo.getString(4);
            } while(c_evo.moveToNext());
        }else{ evo_tv.setVisibility(View.GONE); }

        // 從資料庫抓取資料 : 其他資料
        CardView infoview = (CardView) findViewById(R.id.cardView7);
        TextView info_tv =  (TextView) findViewById(R.id.infoExtra);
        Cursor c_info=db.rawQuery("SELECT * FROM info WHERE card_id = '" + id + "'", null);
        if (c_info.getCount()>0){
            c_info.moveToFirst();
            do{
                tmp_info = c_info.getString(1);
            } while(c_info.moveToNext());
            info_tv.setText(tmp_info);
        }else{ infoview.setVisibility(View.GONE); }

        // 設定屬性顏色
        switch(tmp_attr){
            case "水": use_color = (getResources().getColor(R.color.waterStyle)); break;
            case "火": use_color = (getResources().getColor(R.color.fireStyle)); break;
            case "木": use_color = (getResources().getColor(R.color.woodStyle)); break;
            case "光": use_color = (getResources().getColor(R.color.lightStyle)); break;
            case "暗": use_color = (getResources().getColor(R.color.darkStyle)); break;
        }
        // 設定狀態列顏色
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch(tmp_attr){
                case "水": window.setStatusBarColor(use_color); break;
                case "火": window.setStatusBarColor(use_color); break;
                case "木": window.setStatusBarColor(use_color); break;
                case "光": window.setStatusBarColor(use_color); break;
                case "暗": window.setStatusBarColor(use_color); break;
            }
        }

        // 設置顯示UI :
        // 設置召喚獸圖示
        ImageView imageView = (ImageView) findViewById(R.id.cardImg);
        imageView.setImageResource(tmp_img);
        int set = (screenWidth-10)/5-12;
        imageView.getLayoutParams().width = set;
        imageView.getLayoutParams().height = set;
        int iH = imageStory.getDrawable().getIntrinsicHeight();
        int iW = imageStory.getDrawable().getIntrinsicWidth();
        imageStory.getLayoutParams().width = set/3*iW/iH;
        imageStory.getLayoutParams().height = set/3;
        imageStory.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                showStory(view);
            }});
        imageSkin.getLayoutParams().width = set/3*iW/iH;
        imageSkin.getLayoutParams().height = set/3;
        imageSkin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                showStory(view);
            }});


        // 基本資訊顯示 : ID,名稱,系列,昇華資訊
        tv_id = (TextView) findViewById(R.id.cardId);
        tv_name = (TextView) findViewById(R.id.cardName);
        tv_imgb = (TextView) findViewById(R.id.cardImgB);
        tv_evolution = (TextView) findViewById(R.id.cardEvolution);
        ViewGroup.LayoutParams param = tv_imgb.getLayoutParams();
        param.width = set;
        tv_imgb.setLayoutParams(param);
        tv_series =  (TextView) findViewById(R.id.cardSeries);
        tv_ame =  (TextView) findViewById(R.id.cardAme);
        GradientDrawable bg = (GradientDrawable)tv_series.getBackground();
        bg.setColor(use_color);
        // 技能項目顯示 : 主動技,CD,隊長技
        tv_active = (TextView) findViewById(R.id.nameActive);
        tv_active_skill = (TextView) findViewById(R.id.skillActive);
        tv_cd = (TextView) findViewById(R.id.cdmax);
        tv_active_2 = (TextView) findViewById(R.id.nameActive2);
        tv_active_skill_2 = (TextView) findViewById(R.id.skillActive2);
        tv_cd_2 = (TextView) findViewById(R.id.cdmax2);
        tv_leader = (TextView) findViewById(R.id.nameLeader);
        tv_leader_skill = (TextView) findViewById(R.id.skillLeader);
        Switch ame_active_switch = (Switch) findViewById(R.id.activeSwitch);
        Switch ame_leader_switch = (Switch) findViewById(R.id.leaderSwitch);
        // 顯示項目設置文字
        tv_id.setText("No." + tmp_id);
        tv_name.setText(tmp_name);
        tv_leader.setText(tmp_leader);
        tv_leader_skill.setText(tmp_leader_skill);
        tv_series.setText(tmp_series);
        tv_series.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                showSeries(view);
            }});
        tv_imgb.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                showImgB(view);
            }});
        tv_evolution.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                showEvolution(view);
            }});

        // 設置主動技顯示 技能是否為雙技
        if(tmp_active.indexOf("#") == -1){
            LinearLayout title_con2  = (LinearLayout) this.findViewById(R.id.title_con2);
            LinearLayout cd_con2  = (LinearLayout) this.findViewById(R.id.cd_con2);
            tv_active_skill_2 = (TextView) findViewById(R.id.skillActive2);
            title_con2.setVisibility(View.GONE);
            tv_active_skill_2.setVisibility(View.GONE);
            cd_con2.setVisibility(View.GONE);
            tv_active.setText(tmp_active);
            tv_active_skill.setText(tmp_active_skill);
            tv_cd.setText(tv_cd.getText() + " " + tmp_cd);
        }else{
            // 技能若為雙技 則添加顯示
            tv_active.setText(acts[0]);
            tv_active_skill.setText(act_skills[0]);
            tv_cd.setText(tv_cd.getText() + " " + cdmaxs[0]);
            tv_active_2.setText(acts[1]);
            tv_active_skill_2.setText(act_skills[1]);
            tv_cd_2.setText(tv_cd_2.getText() + " " + cdmaxs[1]);
        }


        // 取得昇華資料
        Cursor c_ame=db.rawQuery("SELECT * FROM amelioration WHERE card_id = " + tmp_id, null);
        if (c_ame.getCount()>0){
            c_ame.moveToFirst();
            do{
                tmp_soul_1 = c_ame.getString(2);  ameList.add(tmp_soul_1);
                tmp_level_1 = c_ame.getString(3);  ameList.add(tmp_level_1);
                tmp_soul_2 = c_ame.getString(4);  ameList.add(tmp_soul_2);
                tmp_level_2 = c_ame.getString(5);  ameList.add(tmp_level_2);
                tmp_soul_3 = c_ame.getString(6);  ameList.add(tmp_soul_3);
                tmp_level_3 = c_ame.getString(7);  ameList.add(tmp_level_3);
                tmp_soul_4 = c_ame.getString(8);  ameList.add(tmp_soul_4);
                tmp_level_4 = c_ame.getString(9);  ameList.add(tmp_level_4);
                tmp_ame_active = c_ame.getString(10);
                tmp_ame_active_skill =  c_ame.getString(11).replace("<br/>","\n");
                tmp_ame_cdmax =  c_ame.getString(12);
                tmp_ame_leader =  c_ame.getString(13);
                tmp_ame_leader_skill =  c_ame.getString(14);
            } while(c_ame.moveToNext());
            tv_ame.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    showAme(view,ameList);
                }});
            if(tmp_ame_active.equals("") != true){
                ame_active_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            TextView tmp_tv = (TextView) findViewById(R.id.txvActive);
                            tmp_tv.setText("昇華主動技 ACTIVE");
                            tv_active.setText(tmp_ame_active);
                            tv_active_skill.setText(tmp_ame_active_skill);
                            tv_cd.setText("最短CD " + tmp_ame_cdmax);
                        } else {
                            TextView tmp_tv = (TextView) findViewById(R.id.txvActive);
                            tmp_tv.setText("主動技 ACTIVE");
                            tv_active.setText(acts[0]);
                            tv_active_skill.setText(act_skills[0]);
                            tv_cd.setText("最短CD " + cdmaxs[0]);
                        }
                    }
                });
            }else{ ame_active_switch.setVisibility(View.GONE); }
            if(tmp_ame_leader.equals("") != true){
                ame_leader_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            TextView tmp_tv = (TextView) findViewById(R.id.txvLeader);
                            tmp_tv.setText("昇華隊長技 LEADER");
                            tv_leader.setText(tmp_ame_leader);
                            tv_leader_skill.setText(tmp_ame_leader_skill);
                        } else {
                            TextView tmp_tv = (TextView) findViewById(R.id.txvLeader);
                            tmp_tv.setText("隊長技 LEADER");
                            tv_leader.setText(tmp_leader);
                            tv_leader_skill.setText(tmp_leader_skill);
                        }
                    }
                });
            }else{ ame_leader_switch.setVisibility(View.GONE); }
        }else{
            tv_ame.setVisibility(View.GONE);
            ame_active_switch.setVisibility(View.GONE);
            ame_leader_switch.setVisibility(View.GONE);
        }


        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.tagActive);
        for(int i=0;i<activeTag.size();i++){
            TextView textview=new TextView(this);
            textview.setText(activeTags.get(i));
            textview.setTextColor(Color.WHITE);
            textview.setBackgroundResource(R.drawable.chip_tag);
            GradientDrawable background = (GradientDrawable)textview.getBackground();
            background.setColor(use_color);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i==0){ params.setMargins(0, 0, 0, 0); }
            else{ params.setMargins(15, 0, 0, 0); }
            textview.setLayoutParams(params);
            final String tmp_tag_id = activeTag.get(i);
            textview.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(),ListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",tmp_tag_id);
                    bundle.putInt("type",0);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }});
            mainLinerLayout.addView(textview);
        }
        LinearLayout mainLinerLayout2 = (LinearLayout) this.findViewById(R.id.tagLeader);
        for(int i=0;i<leaderTag.size();i++){
            TextView textview=new TextView(this);
            textview.setText(leaderTags.get(i));
            textview.setTextColor(Color.WHITE);
            textview.setBackgroundResource(R.drawable.chip_tag);
            GradientDrawable background = (GradientDrawable)textview.getBackground();
            background.setColor(use_color);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i==0){ params.setMargins(0, 0, 0, 0); }
            else{ params.setMargins(15, 0, 0, 0); }
            textview.setLayoutParams(params);
            final String tmp_tag_id = leaderTag.get(i);
            textview.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(),ListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",tmp_tag_id);
                    bundle.putInt("type",1);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }});
            mainLinerLayout2.addView(textview);
        }

        // Set Show List
        adapter = new ItemAdapter(this,screenWidth,titleList,dataList);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        adapter2 = new ItemAdapter(this,screenWidth,titleList2,dataList2);
        GridView gridView2 = (GridView) findViewById(R.id.gridView2);
        gridView2.setAdapter(adapter2);
    }
    public void showSeries(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.show_series, null);
        builder.setNegativeButton("關閉",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        builder.setView(alertView);
        TextView title_series = (TextView) alertView.findViewById(R.id.title_series);
        title_series.setText(tmp_series);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        ArrayList<Card> showList = new ArrayList<Card>();
        Cursor c=db.rawQuery("SELECT * FROM " + Table_Name + " WHERE series = '" + tmp_series + "'", null);
        if (c.getCount()>0){
            String show_id,show_name;
            int show_img;
            Card tmp_card;
            c.moveToFirst();
            do{
                show_id =  c.getString(0);
                show_name =  c.getString(1);
                show_img = getResources().getIdentifier("pic_" + show_id, "drawable", getPackageName());
                tmp_card = new Card(show_id,show_name,null,null,null,null,null,null,null,null,show_img);
                showList.add(tmp_card);
            } while(c.moveToNext());
        }
        int screenWidth = metrics.widthPixels;
        ImageAdapter show_adapter = new ImageAdapter(this,screenWidth,showList,"ById");
        GridView gridShow = (GridView) alertView.findViewById(R.id.gridShow);
        gridShow.setAdapter(show_adapter);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void showAme(View view,ArrayList<String> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.show_ame, null);
        builder.setPositiveButton("關閉",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        }).setNegativeButton("昇華關卡",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Uri uri = Uri.parse("https://tos.fandom.com/zh/wiki/%E6%B6%93%E6%B5%81%E7%9A%84%E8%BF%BD%E6%86%B6");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        builder.setView(alertView);
        TableLayout tableLayout = (TableLayout)alertView.findViewById(R.id.table_ame);
        int rows= 0;
        if(list.get(4).equals("")){ rows = 4; }
        else{ rows = 8; }
        for(int i=0; i < rows; i++ ){
            TableRow tableRow = new TableRow(dialogContext);
            TableRow.LayoutParams trs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            trs.setMargins(2, 2, 2, 2);
            tableRow.setLayoutParams(trs);
            TextView textView1 = new TextView(dialogContext);
            textView1.setTextSize(15);
            textView1.setSingleLine(false);
            textView1.setMaxLines(20);
            if(i%2 == 0){
                textView1.setText("昇華 " + Integer.toString(i/2+1) + " - 所需靈魂 " + list.get(i) );
                textView1.setBackgroundColor(Color.parseColor("#888888"));
                textView1.setPadding(10,10,10,10);
                textView1.setTextColor(Color.WHITE);
                tableRow.addView(textView1);
            }else{
                textView1.setText(list.get(i) );
                textView1.setPadding(10,20,10,40);
                textView1.setBackgroundColor(Color.WHITE);
                TableRow.LayoutParams txs;
                txs = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                tableRow.addView(textView1,txs);
            }

            tableLayout.addView(tableRow);
        }
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void showImgB(View view) {
        final Dialog dialog = new Dialog(this,R.style.edit_AlertDialog_style);
        ImageView imgView = new ImageView(this);
        imgView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        imgView.setImageDrawable(getResources().getDrawable( R.drawable.pet004));
        imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        dialog.setContentView(imgView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        imgView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
    }

    public void showStory(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.show_story, null);
        builder.setNegativeButton("關閉",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        builder.setView(alertView);
        TextView txv_story = (TextView) alertView.findViewById(R.id.txt_story);
        txv_story.setText(tmp_story);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showEvolution(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.show_evolution, null);
        builder.setNegativeButton("關閉",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        builder.setView(alertView);
        LinearLayout evo_con = (LinearLayout) alertView.findViewById(R.id.evo_con);
        int set = (screenWidth-10)/6-12;
        Drawable drawable = getResources().getDrawable(R.drawable.border);
        ImageView cimg = new ImageView(getApplicationContext());
        ImageView bimg = new ImageView(getApplicationContext());
        ImageView aimg = new ImageView(getApplicationContext());
        if(tmp_before.get(0).equals("") == false ){
            bimg.setImageResource(getResources().getIdentifier("pic_" + tmp_before.get(0), "drawable", getPackageName()));
            evo_con.addView(bimg);
            bimg.getLayoutParams().width = set;
            bimg.getLayoutParams().height = set;
            LinearLayout evo_t1 = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            evo_t1.setLayoutParams(layoutParams);
            evo_t1.setBackgroundDrawable(drawable);
            evo_t1.setPadding(50,20,0,20);
            layoutParams.setMargins(set/2-10, 0, 0, 0);
            evo_con.addView(evo_t1,layoutParams);
        }
        cimg.setImageResource(getResources().getIdentifier("pic_" + tmp_cid.get(0), "drawable", getPackageName()));
        evo_con.addView(cimg);
        cimg.getLayoutParams().width = set;
        cimg.getLayoutParams().height = set;

            LinearLayout evo_t2 = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            evo_t2.setLayoutParams(layoutParams);
            evo_t2.setBackgroundDrawable(drawable);
            evo_t2.setPadding(50,20,50,20);
            evo_t2.setOrientation(LinearLayout.VERTICAL);
            layoutParams.setMargins(set/2-10, 0, 0, 0);
                TextView evo_tv = new TextView(this);
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if(tmp_etp.equals("進化") ){ evo_tv.setText("進化素材");}
                else if(tmp_etp.equals("潛能解放") || tmp_etp.equals("異空轉生") ){ evo_tv.setText(tmp_etp + "素材與關卡");}
                evo_tv.setLayoutParams(layoutParams3);
                evo_tv.setTextSize(15);
                evo_t2.addView(evo_tv);
                LinearLayout evo_sc = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                evo_sc.setLayoutParams(layoutParams2);
                evo_sc.setOrientation(LinearLayout.HORIZONTAL);
                tmp_sources = tmp_source.get(0).split(",");
                for(int i=0;i<tmp_sources.length;i++){
                    ImageView simg = new ImageView(getApplicationContext());
                    simg.setImageResource(getResources().getIdentifier("pic_" + tmp_sources[i], "drawable", getPackageName()));
                    evo_sc.addView(simg);
                    simg.getLayoutParams().width = set*2/3;
                    simg.getLayoutParams().height = set*2/3;
                }
            if(tmp_etp.equals("潛能解放")){
                ImageView limg = new ImageView(getApplicationContext());
                limg.setImageResource(getResources().getIdentifier("quest", "drawable", getPackageName()));
                evo_sc.addView(limg);
                limg.getLayoutParams().width = set*2/3;
                limg.getLayoutParams().height = set*2/3;
            }evo_t2.addView(evo_sc);
            evo_con.addView(evo_t2,layoutParams);
            aimg.setImageResource(getResources().getIdentifier("pic_" + tmp_after.get(0), "drawable", getPackageName()));
        Log.d("hh",tmp_after.get(0));
            evo_con.addView(aimg);
            aimg.getLayoutParams().width = set;
            aimg.getLayoutParams().height = set;

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static float cpx(float px, Context context){
        float dp = px / getDensity(context);
        return dp;
    }

    public static float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}
