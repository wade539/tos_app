package myapplication.example.com.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.object.Card;
import myapplication.example.com.myapplication.view.CardActivity;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int screenWidrh;
    public String showType;
    public ArrayList<Card> cardList = new ArrayList<Card>();

    public ImageAdapter(Context c,int sw,ArrayList<Card> cs,String st){
        context = c;
        screenWidrh = sw;
        showType = st;
        cardList = cs;
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int i) {
        return cardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView;
        if(view==null) {
            LayoutInflater li = ((Activity)context).getLayoutInflater();
            myView = li.inflate(R.layout.image, null);
            //myView.setLayoutParams(new GridView.LayoutParams(100,100));
        }else{
            myView = (View)view;
        }
        ImageView imageView = (ImageView)myView.findViewById(R.id.img);
        imageView.setImageResource(cardList.get(i).get_img());
        //Picasso.with(context).load("http://wade539.esy.es/tos/card_img/pic_" + cardList.get(i).get_id() + ".jpg").into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int set = (screenWidrh-10)/5-12;
        imageView.setLayoutParams(new LinearLayout.LayoutParams(set,set));
        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(),CardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",cardList.get(i).get_id());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }});
        TextView text = (TextView)myView.findViewById(R.id.txt);
        String showText = "";
        switch(showType){
            case "ById" : showText = "No." + cardList.get(i).get_id(); break;
            case "ByStar" : showText = cardList.get(i).get_star(); break;
            case "ByLife" : showText = cardList.get(i).get_life(); break;
            case "ByAtk" : showText = cardList.get(i).get_atk(); break;
            case "ByReply" : showText = cardList.get(i).get_reply(); break;
            case "BySum" : showText = cardList.get(i).get_sum(); break;
            case "BySpace" : showText = cardList.get(i).get_space(); break;
        }
        text.setGravity(Gravity.CENTER);
        text.setTextColor(Color.parseColor("#00AA88"));
        text.setText(showText);
        return myView;
    }
}