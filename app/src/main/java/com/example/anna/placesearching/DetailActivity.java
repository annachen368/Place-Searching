package com.example.anna.placesearching;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String placeId = intent.getStringExtra(Intent.EXTRA_TEXT);
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+
                placeId+"&key=AIzaSyC5uHROOrPPPbyCN40nIEx90laVue8pjYY";

        DownloadTask3 downloadTask3 = new DownloadTask3();
        downloadTask3.execute(url);
    }

    private class DownloadTask3 extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = params[0].replaceAll(" ","+");

                URL url = new URL(BASE_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    jsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    jsonStr = null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("DOWNLOAD", "Error ", e);
                jsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("DOWNLOAD", "Error closing stream", e);
                    }
                }
            }

            String[] rlt = new String[5];
            if(jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("result");
                    JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");
                    rlt[0] = jsonObject.getString("icon");
                    rlt[1] = location.getString("lat");
                    rlt[2] = location.getString("lng");
                    rlt[3] = jsonObject.getString("scope");
                    rlt[4] = jsonObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return rlt;
        }

        @Override
        protected void onPostExecute(String[] rlt) {
            if(rlt != null) {
                ImageView icon = (ImageView)findViewById(R.id.place_icon);
                Glide.with(getApplicationContext()).load(rlt[0]).into(icon);

                TextView lat = (TextView)findViewById(R.id.lat);
                lat.setText("Location lat = " + rlt[1]);

                TextView lng = (TextView)findViewById(R.id.lng);
                lng.setText("Location lng = " + rlt[2]);

                TextView scope = (TextView)findViewById(R.id.scope);
                scope.setText("Scope = " + rlt[3]);

                TextView name = (TextView)findViewById(R.id.name);
                name.setText(rlt[4]);
            }
        }
    }


}
