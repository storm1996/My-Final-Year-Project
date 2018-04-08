package com.example.alann.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by alann on 03/04/2018.
 */

public class StatInput extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private static final String TAG = "StatInput";
    private DrawerLayout mDrawerLayout;
    TextView teamSelectedView, playerSelectedView, actionSelectedView;
    TextView circlePlayerView, squarePlayerView, trianglePlayerView, semicirclePlayerView, heartPlayerView;
    String fixture_id, home_id, away_id, selectedPlayer, result, teamSelected, selectedType = new String();
    String circle_player_home, square_player_home, triangle_player_home, semicircle_player_home, heart_player_home = new String();
    String circle_player_away, square_player_away, triangle_player_away, semicircle_player_away, heart_player_away = new String();
    String circle_player_home_name, square_player_home_name, triangle_player_home_name, semicircle_player_home_name, heart_player_home_name = new String();
    String circle_player_away_name, square_player_away_name, triangle_player_away_name, semicircle_player_away_name, heart_player_away_name = new String();
    private GestureLibrary mGestureLibrary;
    ArrayList<Prediction> predictions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_stat);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);

        if (!mGestureLibrary.load()) {
            Log.e(TAG, "unable to load the custom gestures");
            finish();
        }

        GestureOverlayView gestureOverlay = (GestureOverlayView) findViewById(R.id.gestures);
        gestureOverlay.addOnGesturePerformedListener(StatInput.this);

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
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
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
        // Adding Floating Action Button to bottom right of main view
        FloatingActionMenu materialDesignFAM;
        FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.fab);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent subintent = new Intent(StatInput.this, SubstitutionPage.class);
                subintent.putExtra("FIXTURE_TEAM_ID", teamSelected);
                startActivityForResult(subintent, 1);
                //context.startActivity(intent);
            }
        });

        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(StatInput.this, SubstitutionPage.class);
                intent.putExtra("FIXTURE_TEAM_ID", fixture_id);
                intent.putExtra("HOME_TEAM_ID", home_id);
                intent.putExtra("AWAY_TEAM_ID", away_id);
                //context.startActivity(intent);
            }
        });
        fixture_id = getIntent().getStringExtra("FIXTURE_TEAM_ID");
        home_id = getIntent().getStringExtra("HOME_TEAM_ID");
        away_id = getIntent().getStringExtra("AWAY_TEAM_ID");
        Log.d(TAG, "fixture_id"+(fixture_id));
        Log.d(TAG, "home_id"+(home_id));
        Log.d(TAG, "away_id"+(away_id));
        selectedPlayer = "0";
        selectedType = "0";

        teamSelectedView = (TextView) findViewById(R.id.selectedTeam);

        circlePlayerView = (TextView) findViewById(R.id.circleView);
        squarePlayerView = (TextView) findViewById(R.id.squareView);
        trianglePlayerView = (TextView) findViewById(R.id.triangleView);
        semicirclePlayerView = (TextView) findViewById(R.id.semicircleView);
        heartPlayerView = (TextView) findViewById(R.id.heartView);

        playerSelectedView = (TextView) findViewById(R.id.playerSelectedView);
        actionSelectedView = (TextView) findViewById(R.id.actionSelectedView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) {
                // fetch the message String
                if(teamSelected.equals(home_id)){
                    circle_player_home = data.getStringExtra("CIRCLE PLAYER");
                    square_player_home = data.getStringExtra("SQUARE PLAYER");
                    triangle_player_home = data.getStringExtra("TRIANGLE PLAYER");
                    semicircle_player_home = data.getStringExtra("SEMICIRCLE PLAYER");
                    heart_player_home = data.getStringExtra("HEART PLAYER");

                    circle_player_home_name = data.getStringExtra("CIRCLE PLAYER NAME");
                    square_player_home_name = data.getStringExtra("SQUARE PLAYER NAME");
                    triangle_player_home_name = data.getStringExtra("TRIANGLE PLAYER NAME");
                    semicircle_player_home_name = data.getStringExtra("SEMICIRCLE PLAYER NAME");
                    heart_player_home_name = data.getStringExtra("HEART PLAYER NAME");

                    circlePlayerView.setText("Circle: "+circle_player_home_name);
                    squarePlayerView.setText("Square: "+square_player_home_name);
                    trianglePlayerView.setText("Triangle: "+triangle_player_home_name);
                    semicirclePlayerView.setText("Semi-Circle: "+semicircle_player_home_name);
                    heartPlayerView.setText("Heart: "+heart_player_home_name);
                }
                else if(teamSelected.equals(away_id)){
                    circle_player_away = data.getStringExtra("CIRCLE PLAYER");
                    square_player_away = data.getStringExtra("SQUARE PLAYER");
                    triangle_player_away = data.getStringExtra("TRIANGLE PLAYER");
                    semicircle_player_away = data.getStringExtra("SEMICIRCLE PLAYER");
                    heart_player_away = data.getStringExtra("HEART PLAYER");

                    circle_player_away_name = data.getStringExtra("CIRCLE PLAYER NAME");
                    square_player_away_name = data.getStringExtra("SQUARE PLAYER NAME");
                    triangle_player_away_name = data.getStringExtra("TRIANGLE PLAYER NAME");
                    semicircle_player_away_name = data.getStringExtra("SEMICIRCLE PLAYER NAME");
                    heart_player_away_name = data.getStringExtra("HEART PLAYER NAME");

                    circlePlayerView.setText("Circle: "+circle_player_away_name);
                    squarePlayerView.setText("Square: "+square_player_away_name);
                    trianglePlayerView.setText("Triangle: "+triangle_player_away_name);
                    semicirclePlayerView.setText("Semi-Circle: "+semicircle_player_away_name);
                    heartPlayerView.setText("Heart: "+heart_player_away_name);
                }
            }
        } catch (Exception ex) {
        Toast.makeText(StatInput.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        predictions = mGestureLibrary.recognize(gesture);
        if (predictions.size() > 0 && predictions.get(0).score > 1.0) { // GETTING MATCH PREDICTIONS FOR THE GESTURES DRAWN BY USERS AND COMPARED TO STORED PATTERNS IN RAW
            result = predictions.get(0).name;
            Log.d(TAG, "result:"+result);
            makeDecisions();
        }
    }

    private void makeDecisions() {
        //selectedPlayer = "0"; move to input
        if(result.equals("left") || result.equals("right")) {
            switch (result) {
                case ("left"):
                    Log.d(TAG, "left");
                    teamSelected = away_id;
                    teamSelectedView.setText("Away");
                    circlePlayerView.setText("Circle: "+circle_player_away_name);
                    squarePlayerView.setText("Square: "+square_player_away_name);
                    trianglePlayerView.setText("Triangle: "+triangle_player_away_name);
                    semicirclePlayerView.setText("Semi-Circle: "+semicircle_player_away_name);
                    heartPlayerView.setText("Heart: "+heart_player_away_name);
                    break;
                case ("right"):
                    Log.d(TAG, "right");
                    teamSelected = home_id;
                    teamSelectedView.setText("Home");
                    circlePlayerView.setText("Circle: "+circle_player_home_name);
                    squarePlayerView.setText("Square: "+square_player_home_name);
                    trianglePlayerView.setText("Triangle: "+triangle_player_home_name);
                    semicirclePlayerView.setText("Semi-Circle: "+semicircle_player_home_name);
                    heartPlayerView.setText("Heart: "+heart_player_home_name);
                    break;
            }
        }

        else if(result.equals("circle") || result.equals("square") || result.equals("triangle") || result.equals("semicircle") || result.equals("heart")) {
            switch (result) {
                case ("circle"):
                    Log.d(TAG, "circle");
                    if(teamSelected == away_id){
                        selectedPlayer = circle_player_away;
                        playerSelectedView.setText("Chosen Player: "+circle_player_away_name);
                    }
                    if(teamSelected == home_id){
                        selectedPlayer = circle_player_home;
                        playerSelectedView.setText("Chosen Player: "+circle_player_home_name);
                    }
                    Log.d(TAG, "selected_player"+(selectedPlayer));
                    break;
                case ("square"):
                    Log.d(TAG, "square");
                    if(teamSelected == away_id){
                        selectedPlayer = square_player_away;
                        playerSelectedView.setText("Chosen Player: "+square_player_away_name);
                    }
                    if(teamSelected == home_id){
                        selectedPlayer = square_player_home;
                        playerSelectedView.setText("Chosen Player: "+square_player_home_name);
                    }
                    Log.d(TAG, "selected_player"+(selectedPlayer));
                    break;
                case ("triangle"):
                    Log.d(TAG, "triangle");
                    if(teamSelected == away_id){
                        selectedPlayer = triangle_player_away;
                        playerSelectedView.setText("Chosen Player: "+triangle_player_away_name);
                    }
                    if(teamSelected == home_id){
                        selectedPlayer = triangle_player_home;
                        playerSelectedView.setText("Chosen Player: "+triangle_player_home_name);
                    }
                    Log.d(TAG, "selected_player"+(selectedPlayer));
                    break;
                case ("semicircle"):
                    Log.d(TAG, "semi-circle");
                    if(teamSelected == away_id){
                        selectedPlayer = semicircle_player_away;
                        playerSelectedView.setText("Chosen Player: "+semicircle_player_away_name);
                    }
                    if(teamSelected == home_id){
                        selectedPlayer = semicircle_player_home;
                        playerSelectedView.setText("Chosen Player: "+semicircle_player_home_name);
                    }
                    Log.d(TAG, "selected_player"+(selectedPlayer));
                    break;
                case ("heart"):
                    Log.d(TAG, "heart");
                    if(teamSelected == away_id){
                        selectedPlayer = heart_player_away;
                        playerSelectedView.setText("Chosen Player: "+heart_player_away_name);
                    }
                    if(teamSelected == home_id){
                        selectedPlayer = heart_player_home;
                        playerSelectedView.setText("Chosen Player: "+heart_player_home_name);
                    }
                    Log.d(TAG, "selected_player"+(selectedPlayer));
                    break;
            }
        }

        else if((result.equals("assist") || result.equals("block") || result.equals("rebound") || result.equals("defensive-rebound") || result.equals("foul") || result.equals("steal") || result.equals("turnover") || result.equals("one") || result.equals("two") || result.equals("three")) && (selectedPlayer != "0") && (selectedType == "0")){
            switch (result) {
                case ("assist"):
                    Log.d(TAG, "assist");
                    selectedType ="assist";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("block"):
                    Log.d(TAG, "block");
                    selectedType ="block";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("rebound"):
                    Log.d(TAG, "rebound");
                    selectedType ="rebound";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("defensive-rebound"):
                    Log.d(TAG, "defensive-rebound");
                    selectedType ="defensive-rebound";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("foul"):
                    Log.d(TAG, "foul");
                    selectedType ="foul";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("steal"):
                    Log.d(TAG, "steal");
                    selectedType ="steal";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("turnover"):
                    Log.d(TAG, "turnover");
                    selectedType ="turnover";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("one"):
                    Log.d(TAG, "one");
                    selectedType ="one";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("two"):
                    Log.d(TAG, "two");
                    selectedType ="two";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
                case ("three"):
                    Log.d(TAG, "three");
                    selectedType ="three";
                    actionSelectedView.setText("Chosen Stat: "+selectedType);
                    insertTheStat();
                    break;
            }
        }
        else if(selectedPlayer.equals("0")){
            Log.d(TAG, "Choose Player First");
        }
        else{
            Log.d(TAG, "Something went wrong");
        }
    }

    private void insertTheStat() {
        String url = "http://178.62.2.33:8000/api/action/?format=json";
        JSONObject json = new JSONObject();
        try {
            json.put("action_type", selectedType);
            json.put("player_id", "http://178.62.2.33:8000/api/player/"+selectedPlayer+"/?format=json");
            json.put("fixture_id", "http://178.62.2.33:8000/api/fixture/"+fixture_id+"/?format=json");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest postRequest = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d(TAG, String.valueOf(response));
                        selectedPlayer = "0";
                        selectedType = "0";
                        playerSelectedView.setText("Chosen Player: ");
                        actionSelectedView.setText("Chosen Stat: ");
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response: " + error.getMessage());
                    }
                }
        );
        MySingleton.getInstance(StatInput.this).addToRequestQueue(postRequest);
    }
}
