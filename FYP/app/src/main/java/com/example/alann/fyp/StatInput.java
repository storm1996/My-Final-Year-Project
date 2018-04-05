package com.example.alann.fyp;

import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by alann on 03/04/2018.
 */

public class StatInput extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private static final String TAG = "StatInput";
    private DrawerLayout mDrawerLayout;
    String fixture_id, home_id, away_id, selectedPlayer, result, teamSelected = new String();
    String circle_player, square_player, triangle_player, semicircle_player, heart_player = new String();
    private GestureLibrary mGestureLibrary;
    ArrayList<Prediction> predictions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_stat);
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
                //Context context = v.getContext();
                //Intent intent = new Intent(context, PickFixture.class);
                //context.startActivity(intent);
            }
        });
        fixture_id = getIntent().getStringExtra("FIXTURE_TEAM_ID");
        home_id = getIntent().getStringExtra("HOME_TEAM_ID");
        away_id = getIntent().getStringExtra("AWAY_TEAM_ID");
        Log.d(TAG, "fixture_id"+(fixture_id));
        Log.d(TAG, "home_id"+(home_id));
        Log.d(TAG, "away_id"+(away_id));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1  && resultCode  == RESULT_OK) {
                // fetch the message String
                circle_player = data.getStringExtra("CIRCLE PLAYER");
                square_player = data.getStringExtra("SQUARE PLAYER");
                triangle_player = data.getStringExtra("TRIANGLE PLAYER");
                semicircle_player = data.getStringExtra("SEMICIRCLE PLAYER");
                heart_player = data.getStringExtra("HEART PLAYER");
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
        selectedPlayer = "0";
        if(result.equals("left") || result.equals("right")) {
            switch (result) {
                case ("left"):
                    Log.d(TAG, "left");
                    teamSelected = away_id;
                    Log.d(TAG, "circle_player"+(circle_player));
                    Log.d(TAG, "square_player"+(square_player));
                    Log.d(TAG, "triangle_player"+(triangle_player));
                    Log.d(TAG, "semicircle_player"+(semicircle_player));
                    Log.d(TAG, "heart_player"+(heart_player));
                    break;
                case ("right"):
                    Log.d(TAG, "right");
                    teamSelected = home_id;
                    break;
            }
        }

        else if(result.equals("circle") || result.equals("square") || result.equals("triangle") || result.equals("semicircle") || result.equals("heart")) {
            switch (result) {
                case ("left"):
                    Log.d(TAG, "left");
                    break;
                case ("right"):
                    Log.d(TAG, "right");
                    break;
                case ("circle"):
                    Log.d(TAG, "circle");
                    break;
                case ("square"):
                    Log.d(TAG, "square");
                    break;
                case ("triangle"):
                    Log.d(TAG, "triangle");
                    break;
                case ("semicircle"):
                    Log.d(TAG, "semi-circle");
                    break;
                case ("heart"):
                    Log.d(TAG, "heart");
                    break;
            }
        }

        else if((result.equals("assist") || result.equals("block") || result.equals("rebound") || result.equals("defensive-rebound") || result.equals("foul") || result.equals("steal") || result.equals("turnover") || result.equals("one") || result.equals("two") || result.equals("three")) && selectedPlayer != ("0")){
            switch (result) {
                case ("assist"):
                    Log.d(TAG, "assist");
                    break;
                case ("block"):
                    Log.d(TAG, "block");
                    break;
                case ("rebound"):
                    Log.d(TAG, "rebound");
                    break;
                case ("defensive-rebound"):
                    Log.d(TAG, "defensive-rebound");
                    break;
                case ("foul"):
                    Log.d(TAG, "foul");
                    break;
                case ("steal"):
                    Log.d(TAG, "steal");
                    break;
                case ("turnover"):
                    Log.d(TAG, "turnover");
                    break;
                case ("one"):
                    Log.d(TAG, "one");
                    break;
                case ("two"):
                    Log.d(TAG, "two");
                    break;
                case ("three"):
                    Log.d(TAG, "three");
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
}
