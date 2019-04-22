package myapplication.example.com.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import myapplication.example.com.myapplication.R;
import myapplication.example.com.myapplication.object.Card;
import myapplication.example.com.myapplication.views.ListActivity;

public class TagAdapter extends BaseAdapter {
    private Context context;
    private int screenWidth;
    public ArrayList<Card> tagList = new ArrayList<Card>();
    private int type;

    public TagAdapter(Context c,int sw,ArrayList<Card> cs,int tp){
        context = c;
        screenWidth = sw;
        tagList = cs;
        type = tp;
    }
    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int i) {
        return tagList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (view == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.tag, null);
        } else {
            gridView = (View) view;
        }
        TextView textView = (TextView) gridView.findViewById(R.id.grid_tag);
        textView.setText(tagList.get(i).get_name() + "("+tagList.get(i).get_img()+")");
        textView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(),ListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",tagList.get(i).get_id());
                bundle.putInt("type",type);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }});
        textView.measure(0, 0);       //must call measure!
        textView.getMeasuredHeight(); //get height
        textView.getMeasuredWidth();  //get width
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth/24);
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        params.height = textView.getMeasuredHeight() + textView.getMeasuredHeight()/2 + 10;
        return gridView;
    }
}
