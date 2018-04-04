package com.example.alann.fyp;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alann on 03/04/2018.
 */

public class StatInput extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private static final String TAG = "StatInput";
    private DrawerLayout mDrawerLayout;
    String fixture_id, home_id, away_id = new String();
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
        fixture_id = getIntent().getStringExtra("FIXTURE_TEAM_ID");
        home_id = getIntent().getStringExtra("HOME_TEAM_ID");
        away_id = getIntent().getStringExtra("AWAY_TEAM_ID");
        Log.d(TAG, "fixture_id"+(fixture_id));
        Log.d(TAG, "home_id"+(home_id));
        Log.d(TAG, "away_id"+(away_id));
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
            String result = predictions.get(0).name;
            Log.d(TAG, "result:"+result);
            switch (result){
                case ("assist"):
                    Log.d(TAG, "ASSIST");
                    break;
                case ("square"):
                    Log.d(TAG, "SQUARE");
                    break;
                case ("left"):
                    Log.d(TAG, "LEFT");
                    break;
                case ("right"):
                    Log.d(TAG, "right");
                    break;
                default:
                    Log.d(TAG, "didn't work");
                    break;
            }
        }
    }
}
