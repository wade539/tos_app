package myapplication.example.com.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.object.Craft;
import myapplication.example.com.myapplication.view.CraftActivity;

/**
 * Created by user on 2018/10/24.
 */
public class ImageAdapter2 extends BaseAdapter {
    private Context context;
    private int screenWidrh;
    public ArrayList<Craft> craftList = new ArrayList<Craft>();

    public ImageAdapter2(Context c,int sw,ArrayList<Craft> cs){
        context = c;
        screenWidrh = sw;
        craftList = cs;
    }

    @Override
    public int getCount() {
        return craftList.size();
    }

    @Override
    public Object getItem(int i) {
        return craftList.get(i);
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
        imageView.setImageResource(craftList.get(i).get_img());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int set = (screenWidrh-10)/5-12;
        imageView.setLayoutParams(new LinearLayout.LayoutParams(set,set));
        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(),CraftActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",craftList.get(i).get_id());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }});
        TextView text = (TextView)myView.findViewById(R.id.txt);
        text.setText("No." + craftList.get(i).get_id());
        return myView;
    }
}
