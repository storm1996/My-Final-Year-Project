package com.example.alann.fyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alann on 04/04/2018.
 */

public class SubstitutionPage  extends AppCompatActivity {

    private static final String TAG = "SubstitutionPage";
    private DrawerLayout mDrawerLayout;
    private Button submit_button;
    private Button circle_button;
    private Button square_button;
    private Button triangle_button;
    private Button semicircle_button;
    private Button heart_button;
    List<String> list = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    String selectedCircle, team_id;
    String selectedSquare, selectedTriangle, selectedSemicircle, selectedHeart;
    String[] player_array, names_array;
    String circle_value, square_value, triangle_value, semicircle_value, heart_value = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_substitution);

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
        circle_value = " ";
        square_value = " ";
        triangle_value = " ";
        semicircle_value = " ";
        heart_value = " ";
        team_id = getIntent().getStringExtra("FIXTURE_TEAM_ID");
        Log.d(TAG, "team_id"+team_id);

        circle_button = (Button) findViewById(R.id.btnCircle);
        addPlayersButton();

        circle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(SubstitutionPage.this, R.style.AppCompatAlertDialogTheme);
                builder.setTitle("Select Player").setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectedCircle = dataAdapter.getItem(which);
                        for (int i = 0; i < names_array.length; i++) {
                            if ((selectedCircle.equals(names_array[i]))) {
                                int j = i;
                                circle_value = player_array[j];
                                Log.d(TAG, "Circle Player Value" + String.valueOf(circle_value));
                            }
                        }
                        if(square_value.equals(circle_value) || triangle_value.equals(circle_value) || semicircle_value.equals(circle_value) || heart_value.equals(circle_value)){
                            circle_value = " ";
                            Toast.makeText(SubstitutionPage.this, "Player Chosen Already", Toast.LENGTH_LONG).show();
                        }
                        else{
                            circle_button.setText(selectedCircle);
                        }
                    }
                });
                builder.show();
            }
        });

        square_button = (Button) findViewById(R.id.btnSquare);
        square_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(SubstitutionPage.this, R.style.AppCompatAlertDialogTheme);
                builder.setTitle("Select Player").setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectedSquare = dataAdapter.getItem(which);
                        for (int i = 0; i < names_array.length; i++) {
                            if ((selectedSquare.equals(names_array[i]))) {
                                int j = i;
                                square_value = player_array[j];
                                Log.d(TAG, "Square Player Value" + String.valueOf(square_value));
                            }
                        }
                        if(circle_value.equals(square_value) || triangle_value.equals(square_value) || semicircle_value.equals(square_value) || heart_value.equals(square_value)){
                            square_value = " ";
                            Log.d(TAG, "Square Player Value" + String.valueOf(square_value));
                            Toast.makeText(SubstitutionPage.this, "Player Chosen Already", Toast.LENGTH_LONG).show();
                        }
                        else{
                            square_button.setText(selectedSquare);
                        }
                    }
                });
                builder.show();
            }
        });

        triangle_button = (Button) findViewById(R.id.btnTriangle);
        triangle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(SubstitutionPage.this, R.style.AppCompatAlertDialogTheme);
                builder.setTitle("Select Player").setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectedTriangle = dataAdapter.getItem(which);
                        for (int i = 0; i < names_array.length; i++) {
                            if ((selectedTriangle.equals(names_array[i]))) {
                                int j = i;
                                triangle_value = player_array[j];
                                Log.d(TAG, "Triangle Player Value" + String.valueOf(triangle_value));
                            }
                        }
                        if(circle_value.equals(triangle_value) || square_value.equals(triangle_value) || semicircle_value.equals(triangle_value) || heart_value.equals(triangle_value)){
                            square_value = " ";
                            Log.d(TAG, "Triangle Player Value" + String.valueOf(square_value));
                            Toast.makeText(SubstitutionPage.this, "Player Chosen Already", Toast.LENGTH_LONG).show();
                        }
                        else{
                            triangle_button.setText(selectedTriangle);
                        }
                    }
                });
                builder.show();
            }
        });

        semicircle_button = (Button) findViewById(R.id.btnSemicircle);
        semicircle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(SubstitutionPage.this, R.style.AppCompatAlertDialogTheme);
                builder.setTitle("Select Player").setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectedSemicircle = dataAdapter.getItem(which);
                        for (int i = 0; i < names_array.length; i++) {
                            if ((selectedSemicircle.equals(names_array[i]))) {
                                int j = i;
                                semicircle_value = player_array[j];
                                Log.d(TAG, "SemiCircle Player Value" + String.valueOf(semicircle_value));
                            }
                        }
                        if(circle_value.equals(semicircle_value) || square_value.equals(semicircle_value) || triangle_value.equals(semicircle_value) || heart_value.equals(semicircle_value)){
                            semicircle_value = " ";
                            Log.d(TAG, "SemiCircle Player Value" + String.valueOf(semicircle_value));
                            Toast.makeText(SubstitutionPage.this, "Player Chosen Already", Toast.LENGTH_LONG).show();
                        }
                        else{
                            semicircle_button.setText(selectedSemicircle);
                        }
                    }
                });
                builder.show();
            }
        });

        heart_button = (Button) findViewById(R.id.btnHeart);
        heart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(SubstitutionPage.this, R.style.AppCompatAlertDialogTheme);
                builder.setTitle("Select Player").setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectedHeart = dataAdapter.getItem(which);
                        for (int i = 0; i < names_array.length; i++) {
                            if ((selectedHeart.equals(names_array[i]))) {
                                int j = i;
                                heart_value = player_array[j];
                                Log.d(TAG, "Heart Player Value" + String.valueOf(heart_value));
                            }
                        }
                        if(circle_value.equals(heart_value) || square_value.equals(heart_value) || triangle_value.equals(heart_value) || semicircle_value.equals(heart_value)){
                            heart_value = " ";
                            Log.d(TAG, "Heart Player Value" + String.valueOf(heart_value));
                            Toast.makeText(SubstitutionPage.this, "Player Chosen Already", Toast.LENGTH_LONG).show();
                        }
                        else{
                            heart_button.setText(selectedHeart);
                        }
                    }
                });
                builder.show();
            }
        });

        submit_button = (Button) findViewById(R.id.btnSubmit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent subintent = getIntent();
                // put the message to return as result in Intent
                subintent.putExtra("CIRCLE PLAYER",circle_value);
                subintent.putExtra("SQUARE PLAYER",square_value);
                subintent.putExtra("TRIANGLE PLAYER",triangle_value);
                subintent.putExtra("SEMICIRCLE PLAYER",semicircle_value);
                subintent.putExtra("HEART PLAYER",heart_value);
                Log.d(TAG, "heart _ value"+ heart_value);
                // Set The Result in Intent
                setResult(RESULT_OK,subintent);
                //SubstitutionPage.this.
                finish();
            }
        });
    }

    private void addPlayersButton() {
        String url = "http://178.62.2.33:8000/api/player/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        player_array = new String[(response.length())];
                        names_array = new String[(response.length())];
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject json_object = response.getJSONObject(i);
                                // Get the current team (json object) data
                                String name = json_object.getString("player_name");
                                String team = json_object.getString("team_id");
                                String id = json_object.getString("player_id");

                                Log.d(TAG, "team"+ (team));
                                Log.d(TAG, "team_id"+ (team_id));
                                //add to the array used for POST
                                if (team.equals(team_id)){
                                    int j = i;
                                    list.add(name);
                                    player_array[j] = id;
                                    names_array[j] = name;
                                }
                            }
                            Log.d(TAG, "player_array"+ Arrays.toString(player_array));
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
