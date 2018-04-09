package com.example.alann.fyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alann on 30/03/2018.
 */

public class DeleteRecords extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "DeleteRecords";
    ListView listView;
    ArrayList<String> nameArray, action_id_arraylist_no_nulls;
    String[] nameArraystring, team_array, action_id_array, action_array, home_away_array, player_array, player_array_identifier, home_away_name_array;
    String[] player_action_array, player_array_no_nulls, action_array_no_nulls;
    String home_id, away_id, fixture_id, selected_action_id = new String();
    int  player_array_identifier_length, action_length, team_length, action_length_no_nulls = 0;

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

        fixture_id = getIntent().getStringExtra("FIXTURE_TEAM_ID");
        home_id = getIntent().getStringExtra("HOME_TEAM_ID");
        away_id = getIntent().getStringExtra("AWAY_TEAM_ID");
        Log.d(TAG, "FIXTURE_ID"+(fixture_id));
        getAllPlayers();
    }

    private void getAllPlayers(){
        String url = "http://178.62.2.33:8000/api/player/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        player_array_identifier = new String[(response.length()*2)];
                        home_away_array = new String[(response.length())];
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject json_object = response.getJSONObject(i);
                                // Get the current team (json object) data
                                String id = json_object.getString("player_id");
                                String name = json_object.getString("player_name");
                                String number = json_object.getString("player_number");
                                String team = json_object.getString("team_id");

                                player_array_identifier_length +=1;
                                int j= i;
                                int h= i;

                                home_away_array[h] = team;
                                //add to the array used for POST
                                if (j == 0)
                                {
                                    player_array_identifier[j]= id;
                                    player_array_identifier[j+=1]= number+" - "+name;
                                }
                                if(j == 1)
                                {
                                    player_array_identifier[j+=1]= id;
                                    player_array_identifier[j+=1]= number+" - "+name;
                                }
                                if(j > 1)
                                {
                                    int k = (j+j);
                                    int l = (j+j+1);
                                    player_array_identifier[k]= id;
                                    player_array_identifier[l]= number+" - "+name;
                                }

                            }
                            Log.d(TAG, "player_array_identifier"+Arrays.toString(player_array_identifier));
                            Log.d(TAG, "player_array_identifier_length"+(player_array_identifier_length));
                            Log.d(TAG, "home_away_array"+Arrays.toString((home_away_array)));
                            getAllActions();
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
    private void getAllActions(){
        String url = "http://178.62.2.33:8000/api/action/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        action_array = new String[(response.length())];
                        player_array = new String[(response.length())];
                        action_id_array = new String[(response.length())];
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject json_object = response.getJSONObject(i);
                                // Get the current team (json object) data
                                String id = json_object.getString("action_id");
                                String type = json_object.getString("action_type");
                                String player = json_object.getString("player_id");
                                String fixture = json_object.getString("fixture_id");
                                int j = i;
                                action_length += 1;
                                //add to the array used for POST
                                if(fixture.equals("http://178.62.2.33:8000/api/fixture/"+fixture_id+"/?format=json")){
                                    action_array[j]= type;
                                    player_array[j]= player;
                                    action_id_array[j]= id;
                                    action_length_no_nulls += 1;
                                }
                            }
                            getAllTeams();
                            //Log.d(TAG, "action_array"+Arrays.toString(action_array));
                            //Log.d(TAG, "player_array"+Arrays.toString(player_array));
                            //Log.d(TAG, "action_id_array"+Arrays.toString(action_id_array));
                            Log.d(TAG, "action_length"+(action_length));
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
                            changeUrlsToNames();
                            Log.d(TAG, "team_array"+Arrays.toString(team_array));
                            Log.d(TAG, "team_length"+(team_length));
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

    private void changeUrlsToNames() {
        home_away_name_array = new String[player_array_identifier_length];
        for (int i = 0; i < player_array_identifier_length; i++) {
            for (int j = 0; j < (team_length * 2); j++) {
                int k = i;
                int l = j;
                if (home_away_array[k].equals("http://178.62.2.33:8000/api/team/" + team_array[l] + "/?format=json")) {
                    home_away_name_array[k] = (team_array[l += 1]);
                }
            }
        }
        Log.d(TAG, "home_away_name_array"+Arrays.toString(home_away_name_array));

        action_id_arraylist_no_nulls= new ArrayList<>();

        player_array_no_nulls = new String[action_length_no_nulls];
        action_array_no_nulls = new String[action_length_no_nulls];
        int n =0;
        for (int i = 0; i < action_length; i++) {
            if(player_array[i] != null){
                player_array_no_nulls[n] = player_array[i];
                action_array_no_nulls[n] = action_array[i];
                action_id_arraylist_no_nulls.add(action_id_array[i]);
                n += 1;
            }
        }

        Log.d(TAG, "player_array_no_nulls"+Arrays.toString(player_array_no_nulls));
        Log.d(TAG, "action_array_no_nulls"+Arrays.toString(action_array_no_nulls));
        player_action_array = new String[action_length_no_nulls];
        for (int i = 0; i < action_length_no_nulls; i++) {
            for (int j = 0; j < (player_array_identifier_length * 2); j++) {
                int k = i;
                int l = j;
                if (player_array_no_nulls[k].equals("http://178.62.2.33:8000/api/player/" + player_array_identifier[l] + "/?format=json")) {
                    player_action_array[k] = (player_array_identifier[l += 1]);
                }
            }
        }
        Log.d(TAG, "player_action_array"+Arrays.toString(player_action_array));

        nameArray= new ArrayList<>();
        nameArraystring = new String[action_length_no_nulls];
        for(int i=0;i<action_length_no_nulls;i++) {
            nameArraystring[i] = player_action_array[i]+"\n"+action_array_no_nulls[i];
            nameArray.add(nameArraystring[i]);
        }

        CustomListAdapter listAdapter = new CustomListAdapter(this, nameArray);
        listView = (ListView) findViewById(R.id.listviewID);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DeleteRecords.this);
                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        selected_action_id = action_id_arraylist_no_nulls.get(position);

                        Log.d(TAG, "selected_action_id"+ selected_action_id);
                        String url = "http://178.62.2.33:8000/api/action/"+selected_action_id+"/?format=json";
                        StringRequest jsonArrayRequest = new StringRequest(Request.Method.DELETE, url, // PARSING THE JSON VALUES
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        nameArray.remove(position);
                                        action_id_arraylist_no_nulls.remove(position);
                                        listAdapter.notifyDataSetChanged();
                                        Log.d(TAG, "DONE");
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                            }
                        });
                        MySingleton.getInstance(DeleteRecords.this).addToRequestQueue(jsonArrayRequest);;
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
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
