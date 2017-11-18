package com.abbasnazar.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class detail extends AppCompatActivity {

    TextView ave,min,max,humidity,pressure,main,day;
    ImageView icon;
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ave=(TextView) findViewById(R.id.ave);
        min=(TextView) findViewById(R.id.min);
        max=(TextView) findViewById(R.id.max);
        humidity=(TextView) findViewById(R.id.humidity);
        pressure=(TextView) findViewById(R.id.pressure);
        main=(TextView) findViewById(R.id.main);
        day=(TextView) findViewById(R.id.textView11);
        icon=(ImageView)findViewById(R.id.imageView2);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i=getIntent();
        day.setText(i.getStringExtra("day"));
        min.setText("Min : "+i.getStringExtra("min"));
        max.setText("Max : "+i.getStringExtra("max"));
        ave.setText("Average : "+i.getStringExtra("ave"));
        main.setText(i.getStringExtra("main"));
        humidity.setText("Humidity : "+i.getStringExtra("humidity"));
        pressure.setText("Pressure : "+i.getStringExtra("pressure"));
        Picasso.with(getApplication()).load(IMG_URL+i.getStringExtra("icon")+".png").fit().into(icon);
    }
}
