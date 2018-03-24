package com.example.alann.fyp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alann on 02/03/2018.
 */

public class MatchRecordingSetup extends AppCompatActivity {

    private static final String TAG = "MatchRecordingSetup";
    private DrawerLayout mDrawerLayout;
    private Button submit_button;
    private Button home_button;
    private Button away_button;
    private Button venue_button;
    private Button date_button;
    List<String> list = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    List<String> venue_list = new ArrayList<String>();
    ArrayAdapter<String> VenuedataAdapter;
    String selectedItem;
    String selectedAwayItem;
    String venueSelectedItem;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_matchrecording);

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

        home_button = (Button) findViewById(R.id.btnHomeTeam);
        addItemsOnTeamButton();

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchRecordingSetup.this, R.style.AppCompatAlertDialogTheme);
                builder.setTitle("Select Home Team").setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectedItem = dataAdapter.getItem(which);
                        home_button.setText(selectedItem);
                    }
                });
                builder.show();
            }
        });

        away_button = (Button) findViewById(R.id.btnAwayTeam);
        away_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchRecordingSetup.this, R.style.AppCompatAlertDialogTheme);
                builder.setTitle("Select Away Team").setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectedAwayItem = dataAdapter.getItem(which);
                        away_button.setText(selectedAwayItem);
                    }
                });
                builder.show();
            }
        });

        venue_button = (Button) findViewById(R.id.btnVenueTeam);
        addItemsOnVenueButton();

        venue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchRecordingSetup.this, R.style.AppCompatAlertDialogTheme);
                builder.setTitle("Select Venue").setAdapter(VenuedataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        venueSelectedItem = VenuedataAdapter.getItem(which);
                        venue_button.setText(venueSelectedItem);
                    }
                });
                builder.show();
            }
        });

        date_button = (Button) findViewById(R.id.btnDatePickerTeam);
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setDateDialog();
            }
        });

        addListenerOnButton();
    }

    private void setDateDialog() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(MatchRecordingSetup.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                selectedDate = view.getYear() + "-" + (view.getMonth()+1) +
                        "-" + view.getDayOfMonth();
                //Toast.makeText(MatchRecordingSetup.this, "selected date is " + selectedDate, Toast.LENGTH_SHORT).show();

                date_button.setText(selectedDate);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
        datePickerDialog.show();
    }

    private void addItemsOnTeamButton() {
        String url = "http://178.62.2.33:8000/api/team/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject json_object = response.getJSONObject(i);
                                // Get the current team (json object) data
                                String type = json_object.getString("team_name");
                                //add to the dropdown
                                list.add(type);
                            }
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
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, list) {
            @Override
            public View getView( int position,View convertView, ViewGroup parent){
                // Cast list view each item as text view
                TextView text_view = (TextView) super.getView(position,convertView,parent);

                // Set text size
                text_view.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    // Set the list view one by one items text color
                    text_view.setTextColor(Color.parseColor("#424242"));

                // Finally, return the modified items
                return text_view;
            }
        };
    }

    private void addItemsOnVenueButton() {
        String url = "http://178.62.2.33:8000/api/fixture/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject json_object = response.getJSONObject(i);
                                // Get the current team (json object) data
                                String type = json_object.getString("venue");
                                //add to the dropdown
                                venue_list.add(type);
                            }
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
        VenuedataAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, venue_list) {
            @Override
            public View getView( int position,View convertView, ViewGroup parent){
                // Cast list view each item as text view
                TextView text_view = (TextView) super.getView(position,convertView,parent);

                // Set text size
                text_view.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                // Set the list view one by one items text color
                text_view.setTextColor(Color.parseColor("#424242"));

                // Finally, return the modified items
                return text_view;
            }
        };
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {
        submit_button = (Button) findViewById(R.id.btnSubmit);
        String url = "http://178.62.2.33:8000/api/fixture/post";
        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response", error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("home_team", selectedItem);
                params.put("away_team", selectedAwayItem);
                params.put("fixture_date", selectedDate);
                params.put("venue", venueSelectedItem);

                return params;
            }
        };

        submit_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(MatchRecordingSetup.this, "OnClickListener : " +
                        //"\nHome Team : "+ String.valueOf(home_spinner.getSelectedItem()) , Toast.LENGTH_SHORT).show();
                MySingleton.getInstance(MatchRecordingSetup.this).addToRequestQueue(postRequest);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         //Inflate the menu; this adds items to the action bar if it is present.
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
