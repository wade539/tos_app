package myapplication.example.com.myapplication.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import myapplication.example.com.myapplication.R;

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private int screenWidth;
    public ArrayList<String> titleList = new ArrayList<String>();
    public ArrayList<String> dataList = new ArrayList<String>();

    public ItemAdapter(Context c,int sw,ArrayList<String> ts,ArrayList<String> ds){
        context = c;
        screenWidth = sw;
        dataList = ds;
        titleList = ts;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
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
            gridView = inflater.inflate(R.layout.item, null);
        } else {
            gridView = (View) view;
        }
        TextView itemTitle = (TextView) gridView.findViewById(R.id.grid_title);
        itemTitle.setText(titleList.get(i));
        itemTitle.measure(0, 0);       //must call measure!
        itemTitle.getMeasuredHeight(); //get height
        itemTitle.getMeasuredWidth();  //get width
        itemTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth/28);

        TextView itemData = (TextView) gridView.findViewById(R.id.grid_data);
        itemData.setText(dataList.get(i));
        itemData.measure(0, 0);       //must call measure!
        itemData.getMeasuredHeight(); //get height
        itemData.getMeasuredWidth();  //get width
        itemData.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth/28);


        return gridView;
    }
}
