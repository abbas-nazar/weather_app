package com.abbasnazar.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    ListView list;
    ArrayList<RowItem> rowitems;
    SharedPreferences sharedPreferences;
    String city,tempUnit;
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    Weather w=new Weather();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowitems=new ArrayList<RowItem>();
        list=(ListView)findViewById(R.id.weatherlist);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        city=sharedPreferences.getString("city","");
        /*if(city.equals(""))
        {
            Intent i=new Intent(this, setting.class);
            startActivity(i);

        }*/
        TempCheck("lahore");
        ImageView im=(ImageView) findViewById(R.id.imageView);
        /*Bitmap bmp = BitmapFactory.decodeByteArray(w.icon, 0, w.icon.length);
        im.setImageBitmap(Bitmap.createScaledBitmap(bmp, im.getWidth(),
                im.getHeight(), false));*/
        TextView t=(TextView)findViewById(R.id.temp);
        t.setText(w.average);


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
        Weathercheck weathercheck=new Weathercheck();
        weathercheck.execute();
    }


    public String getWeatherData(String location) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(BASE_URL + location)).openConnection();
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

    public byte[] getImage(String code) {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
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

    private class Weathercheck extends AsyncTask<String, Integer, Weather> {
        String icon;
        protected Weather doInBackground(String... strng) {

            String data=getWeatherData(strng[0]);
            try {
                JSONObject obj=new JSONObject(data);
                w.name=obj.getString("name");
                JSONArray arr=obj.getJSONArray("weather");
                JSONObject o=arr.getJSONObject(0);
                w.main=o.getString("main");
                w.average=o.getString("temp");
                w.min=o.getString("temp_min");
                w.max=o.getString("temp_max");
                icon=o.getString("icon");
            }
            catch (Exception e)
            {

            }
            w.icon=getImage(icon);
            return w;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }

}
