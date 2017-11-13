package com.abbasnazar.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Abbas Nazar on 11/13/2017.
 */

public class CustomAdapter extends BaseAdapter
{
    ArrayList<RowItem> row;
    LayoutInflater layoutInflater;

    public CustomAdapter(Context context, ArrayList<RowItem> row)
    {
        this.row=row;
        this.layoutInflater=(LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return row.size();
    }

    @Override
    public Object getItem(int i) {
        return row.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
            {
                view=layoutInflater.inflate(R.layout.listview_row,viewGroup,false);
                ImageView image=(ImageView)view.findViewById(R.id.status);
                TextView heading=(TextView) view.findViewById(R.id.headuing);
                TextView subheading=(TextView) view.findViewById(R.id.subheading);
                TextView temp=(TextView) view.findViewById(R.id.temp);

                RowItem item=(RowItem) getItem(i);
                heading.setText(item.heading);
                subheading.setText(item.subHeading);
                temp.setText(item.temp+" C");
                image.setImageResource(R.drawable.a);

            }
        return view;
    }
}
