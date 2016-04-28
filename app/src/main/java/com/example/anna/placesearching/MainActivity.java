package com.example.anna.placesearching;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pools;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private static ArrayAdapter<String> adapter;
    private static String[] itemArrayList = new String[] {};
    private static HashSet<String> set = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        set.clear();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        // autocomplete
        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)  searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_popup_background_mtrl_mult));

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, itemArrayList);
          searchAutoComplete.setAdapter(adapter);

        // check if users type something
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.replaceAll(" ", "\\+");
                if(!set.contains(newText)) {
                    set.add(newText);
                    // download places
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute
                            ("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + newText +
                                    "&types=geocode&language=en&key=AIzaSyC5uHROOrPPPbyCN40nIEx90laVue8pjYY");
                }
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Log.i("click","select");
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                String input = adapter.getItem(position);
                DownloadTask2 downloadTask = new DownloadTask2();
                downloadTask.execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + input +
                        "&types=geocode&language=en&key=AIzaSyC5uHROOrPPPbyCN40nIEx90laVue8pjYY");

                return true;
            }
        });


        return true;
    }

    private class DownloadTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = params[0];

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


            String[] rlt = null;
            if(jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONObject(jsonStr).getJSONArray("predictions");

                    rlt = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        rlt[i] = obj.getString("description");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return rlt;
        }

        @Override
        protected void onPostExecute(String[] rlt) {
            if(rlt != null) {
                adapter.addAll(rlt);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class DownloadTask2 extends AsyncTask<String, Void, String[]> {

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

            String[] rlt = null;
            if(jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONObject(jsonStr).getJSONArray("predictions");

                    rlt = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        rlt[i] = obj.getString("place_id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return rlt;
        }

        @Override
        protected void onPostExecute(String[] rlt) {
            if(rlt != null) {
                Intent intent = new Intent(getApplication(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, rlt[0]);
                startActivity(intent);
            }
        }
    }


}
