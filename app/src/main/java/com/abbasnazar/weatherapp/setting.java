package com.abbasnazar.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class setting extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String url="http://api.openweathermap.org/data/2.5/weather?q=";
    String key="&appid=ea574594b9d36ab688642d5fbeab847e";
    EditText city;
    Spinner spinner;
    String c,t;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        city=(EditText)findViewById(R.id.editCity);
    }

    public String getWeatherData(String location,String url) {
        data=null;
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
        catch(Exception e) {
            data="filenotfound";
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

    public void check(View view)
    {
        Weathercheck w=new Weathercheck(setting.this);
        w.execute(city.getText().toString());
    }
    public void showResult()
    {
        if(data==null)
        {
            Toast.makeText(getApplicationContext(),"Check your city",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
            editor.putString("city",city.getText().toString());
            editor.putString("temp",t);
            editor.commit();
        }
    }
    private class Weathercheck extends AsyncTask<String, Integer, Weather> {
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

            data=getWeatherData(strng[0],url);

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Weather result)
        {
            progress.dismiss();
            showResult();
        }
    }
}
