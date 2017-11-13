package com.abbasnazar.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list;
    ArrayList<RowItem> rowitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowitems=new ArrayList<RowItem>();
        list=(ListView)findViewById(R.id.weatherlist);
        fillArray();

        CustomAdapter adapter=new CustomAdapter(getApplicationContext(),rowitems);
        list.setAdapter(adapter);
    }

    private void fillArray()
    {
        for (int i=0;i<6;i++)
        {
            RowItem row=new RowItem();
            row.heading="heading "+i;
            row.subHeading="Sub heading "+i;
            row.temp="40";
            rowitems.add(row);
        }
    }
}
