package com.example.alann.fyp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by alann on 30/03/2018.
 */

public class PickFixture extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "PickFixture";
    ListView listView;
    ArrayList<String> nameArray;
    String[] nameArraystring, name_array_home, name_array_away, team_array, home_array, away_array, date_array, id_array;
    String selected_home_id, selected_away_id, selected_fixture_id = new String();
    int length, team_length = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_fixture);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        // Create Navigation drawer and inflate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        // TODO: handle navigation

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        getAllTeams();
    }

    private void changeUrlsToNames(){
        name_array_home = new String[length];
        for(int i=0;i<length;i++) {
            for (int j = 0; j < (team_length*2); j++) {
                int k = i;
                int l = j;
                if (home_array[k].equals("http://178.62.2.33:8000/api/team/"+team_array[l]+"/?format=json")) {
                    name_array_home[k] = (team_array[l += 1]);
                }
            }
        }
        Log.d(TAG, "name_array_home"+Arrays.toString(name_array_home));

        name_array_away = new String[length];
        for(int i=0;i<length;i++) {
            for (int j = 0; j < (team_length*2); j++) {
                int k = i;
                int l = j;
                if (away_array[k].equals("http://178.62.2.33:8000/api/team/"+team_array[l]+"/?format=json")) {
                    name_array_away[k] = (team_array[l += 1]);
                }
            }
        }
        Log.d(TAG, "name_array_home"+Arrays.toString(name_array_away));

        nameArray= new ArrayList<>();
        nameArraystring = new String[length];
        for(int i=0;i<length;i++) {
            nameArraystring[i] = name_array_home[i]+" vs. "+name_array_away[i]+"\n"+date_array[i];
            nameArray.add(nameArraystring[i]);
        }

        CustomListAdapter listAdapter = new CustomListAdapter(this, nameArray);
        listView = (ListView) findViewById(R.id.listviewID);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_fixture_id = id_array[position];
                selected_home_id = home_array[position];
                selected_away_id = away_array[position];
                Intent intent = new Intent(PickFixture.this, StatInput.class);
                intent.putExtra("FIXTURE_TEAM_ID", selected_fixture_id);
                intent.putExtra("AWAY_TEAM_ID", selected_away_id);
                intent.putExtra("HOME_TEAM_ID", selected_home_id);
                startActivity(intent);
            }
        });
    }
    private void getAllTeams(){
        String url = "http://178.62.2.33:8000/api/team/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        team_array = new String[(response.length()*2)];
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject json_object = response.getJSONObject(i);
                                // Get the current team (json object) data
                                String id = json_object.getString("team_id");
                                String type = json_object.getString("team_name");
                                int j = i;
                                team_length += 1;
                                //add to the array used for POST
                                if (j == 0)
                                {
                                    team_array[j]= id;
                                    team_array[j+=1]= type;
                                }
                                if(j == 1)
                                {
                                    team_array[j+=1]= id;
                                    team_array[j+=1]= type;
                                }
                                if(j > 1)
                                {
                                    int k = (j+j);
                                    int l = (j+j+1);
                                    team_array[k]= id;
                                    team_array[l]= type;
                                }

                            }
                            Log.d(TAG, "team_array"+Arrays.toString(team_array));
                            Log.d(TAG, "team_length"+(team_length));
                            getAllFixtures();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
    private void getAllFixtures() {
        String url = "http://178.62.2.33:8000/api/fixture/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        home_array = new String[(response.length())];
                        away_array = new String[(response.length())];
                        date_array = new String[(response.length())];
                        id_array = new String[(response.length())];
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject fixture = response.getJSONObject(i);

                                // Get the current player (json object) data
                                String home = fixture.getString("home_team");
                                String away = fixture.getString("away_team");
                                String date = fixture.getString("fixture_date");
                                String id = fixture.getString("fixture_id");
                                int j = i;

                                //add to the array used for POST
                                home_array[j]= home;
                                away_array[j]= away;
                                date_array[j]= date;
                                id_array[j]= id;
                                length += 1;
                            }
                            Log.d(TAG, "home"+Arrays.toString(home_array));
                            Log.d(TAG, "away"+Arrays.toString(away_array));
                            Log.d(TAG, "date"+Arrays.toString(date_array));
                            Log.d(TAG, "id"+Arrays.toString(id_array));
                            Log.d(TAG, "length"+(length));
                            changeUrlsToNames();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "Error.Response: " + error.getMessage());
                    }
                });
        MySingleton.getInstance(PickFixture.this).addToRequestQueue(jsonArrayRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}