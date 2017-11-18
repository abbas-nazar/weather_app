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
import android.widget.AdapterView;
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
    String key="&appid=ea574594b9d36ab688642d5fbeab847e";
    SharedPreferences sharedPreferences;
    String city,tempUnit;
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String multiweather="http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    String cnt ="&cnt=7";
    Weather w=new Weather();;
    ArrayList<Weather> weather;
    TextView cityname,temp,main,day;
    ImageView icon;
    String nameC;
    JSONArray arr;
    Intent io;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rowitems=new ArrayList<RowItem>();
        list=(ListView)findViewById(R.id.weatherlist);

        //bar=(ProgressBar)findViewById(R.id.progressBar);

        io=new Intent(this,detail.class);

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



        //fillArray();

        CustomAdapter adapter=new CustomAdapter(getApplicationContext(),rowitems);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                io.putExtra("day",rowitems.get(i).heading);
                io.putExtra("min",weather.get(i+1).min);
                io.putExtra("max",weather.get(i+1).max);
                io.putExtra("ave",weather.get(i+1).average);
                io.putExtra("main",weather.get(i+1).main);
                io.putExtra("humidity",weather.get(i+1).humidity);
                io.putExtra("pressure",weather.get(i+1).humidity);
                io.putExtra("icon",weather.get(i+1).icon);
                startActivity(io);
            }
        });
    }

    private void fillArray()
    {
        for (int i=0;i<weather.size();i++)
        {
            RowItem row=new RowItem();
            row.heading="heading "+i;
            row.subHeading=weather.get(i).main;
            row.temp=weather.get(i).average;
            row.image=weather.get(i).icon;
            rowitems.add(row);
            //Log.d("tayyab",""+weather.size());
        }
    }

    private void TempCheck(String c)
    {
        weather=new ArrayList<Weather>();
        Weathercheck weathercheck=new Weathercheck(MainActivity.this);
        weathercheck.execute(c);


    }


    public String getWeatherData(String location,String url) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(url + location+key)).openConnection();
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

    public void settings(View view)
    {
        Intent i=new Intent(this, setting.class);
        startActivity(i);
    }

    public void details(View view)
    {
        Intent i=new Intent(this,detail.class);
        i.putExtra("day",day.getText());
        i.putExtra("min",weather.get(0).min);
        i.putExtra("max",weather.get(0).max);
        i.putExtra("ave",weather.get(0).average);
        i.putExtra("main",weather.get(0).main);
        i.putExtra("humidity",weather.get(0).humidity);
        i.putExtra("pressure",weather.get(0).humidity);
        i.putExtra("icon",weather.get(0).icon);
        startActivity(i);



    }

    public void refresh(View view)
    {
        TempCheck(city);
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

            String data=getWeatherData(strng[0],BASE_URL);
            try {
                JSONObject obj=new JSONObject(data);
                nameC=obj.getString("name");
                nameC+=","+obj.getJSONObject("sys").getString("country");

                String s=getWeatherData(nameC,multiweather);
                JSONObject mulObj=new JSONObject(s);
                arr=mulObj.getJSONArray("list");

                //Log.d("assassin",arr.toString());
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
            update();
            progress.dismiss();
        }
    }

    public void update()
    {
        for (int i=1;i<arr.length();i++)
        {
            try {
                Weather w=new Weather();
                JSONObject temp=arr.getJSONObject(i);
                w.min=temp.getJSONObject("temp").getString("min");
                w.min=w.min.substring(0,Math.min(w.min.length(),6));
                w.max=temp.getJSONObject("temp").getString("max");
                w.max=w.max.substring(0,Math.min(w.max.length(),6));
                w.average=""+((Double.parseDouble(w.min)+Double.parseDouble(w.max))/2);
                w.average=w.average.substring(0,Math.min(w.average.length(),6));
                w.main=temp.getJSONArray("weather").getJSONObject(0).getString("main");
                w.icon=temp.getJSONArray("weather").getJSONObject(0).getString("icon");
                w.pressure=temp.getString("pressure");
                w.humidity=temp.getString("humidity");
                weather.add(w);
                //Log.d("tayab",""+arr.length());
            }
            catch (Exception e)
            {
                Log.d("Templar",e.toString());
            }

        }
        fillArray();

        temp.setText(weather.get(0).average);
        main.setText(weather.get(0).main);
        cityname.setText(nameC);
        Picasso.with(getApplication()).load(IMG_URL+weather.get(0).icon+".png").fit().into(icon);
    }

}
