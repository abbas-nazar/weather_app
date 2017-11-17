package com.abbasnazar.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    ListView list;
    ArrayList<RowItem> rowitems;
    String key="&appid=41401b36116d0ee4c2fdbef310679323";
    SharedPreferences sharedPreferences;
    String city,tempUnit;
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String multiweather="http//api.openweathermap.org/data/2.5/forecast/daily?q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    String cnt ="&cnt=7";
    Weather w=new Weather();;

    TextView cityname,temp,main,day;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowitems=new ArrayList<RowItem>();
        list=(ListView)findViewById(R.id.weatherlist);

        //bar=(ProgressBar)findViewById(R.id.progressBar);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        city=sharedPreferences.getString("city","");

        temp=(TextView)findViewById(R.id.temp);
        cityname=(TextView)findViewById(R.id.city);
        main=(TextView)findViewById(R.id.condition);
        day=(TextView)findViewById(R.id.current);

        icon=(ImageView)findViewById(R.id.imageView);


        /*if(city.equals(""))
        {
            Intent i=new Intent(this, setting.class);
            startActivity(i);

        }*/

        TempCheck("islamabad");
        ImageView im=(ImageView) findViewById(R.id.imageView);
        /*Bitmap bmp = BitmapFactory.decodeByteArray(w.icon, 0, w.icon.length);
        im.setImageBitmap(Bitmap.createScaledBitmap(bmp, im.getWidth(),
                im.getHeight(), false));*/



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

    private void TempCheck(String c)
    {

        Weathercheck weathercheck=new Weathercheck(MainActivity.this);
        weathercheck.execute(c);


    }


    public String getWeatherData(String location) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(BASE_URL + location+key)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ( (line = br.readLine()) != null )
                buffer.append(line + "rn");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

   /* public Bitmap getImage(String code) {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(IMG_URL + code+".png")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(is);
            return myBitmap;

            *//*while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();*//*
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }*/

    private class Weathercheck extends AsyncTask<String, Integer, Weather> {
        String icon;
        Bitmap a;
        ProgressDialog progress;

        public Weathercheck(Activity a)
        {
            progress=new ProgressDialog(a);
        }
        @Override
        protected void onPreExecute() {

            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }
        protected Weather doInBackground(String... strng) {

            String data=getWeatherData(strng[0]);
            try {
                JSONObject obj=new JSONObject(data);
                w.name=obj.getString("name");
                JSONArray arr=obj.getJSONArray("weather");
                JSONObject o=arr.getJSONObject(0);
                w.main=o.getString("main");
                JSONObject t=obj.getJSONObject("main");
                w.average=t.getString("temp");
                w.min=t.getString("temp_min");
                w.max=t.getString("temp_max");
                w.pressure=t.getString("pressure");
                w.humidity=t.getString("humidity");
                icon=o.getString("icon");
                w.name+=","+obj.getJSONObject("sys").getString("country");
                Log.d("assassin",w.name);
                String cod=obj.getJSONObject("sys").getString("cod");
                String s=getWeatherData(w.name+","+cod);
                Log.d("assassin",cod);
            }
            catch (Exception e)
            {

            }
          /* a=getImage(icon);*/
            return w;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Weather result)
        {
            progress.dismiss();
            update(icon);
        }
    }

    public void update(String url)
    {
        temp.setText(w.average);
        main.setText(w.main);
        cityname.setText(w.name);
        Picasso.with(getApplication()).load(IMG_URL+url+".png").fit().into(icon);
    }

}
