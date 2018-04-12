package com.example.alann.fyp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alann on 11/04/2018.
 */

public class IndividualResult extends AppCompatActivity implements OnChartValueSelectedListener{
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "IndividualResult";
    TextView selectedHome, selectedAway, selectedHomePts, selectedAwayPts, selectedHomeReb, selectedAwayReb;
    TextView selectedHomeBlk, selectedAwayBlk, selectedHomeAst, selectedAwayAst, selectedHomeStl, selectedAwayStl;
    String position;
    PieChart pieChart, pieChartAway, pieChartDef, pieChartDefAway;
    int team_length, length, player_array_identifier_length_home, action_length_home, action_length_away, player_array_identifier_length_away = 0;
    int away_score, home_score, home_rebound, home_block, home_assist, home_steal, away_rebound, away_block, away_assist, away_steal;
    String[] name_array_home, name_array_away, team_array, home_array, away_array, date_array, id_array;
    String selected_home_id, selected_away_id, selected_fixture_id, selected_home_name, selected_away_name = new String();
    String[] action_array_home, action_id_array_home, player_array_identifier_home, player_array_identifier_away, player_id_home, player_id_away, action_player_array_home, action_player_array_away;
    String[] player_array_identifier_home_no_nulls, player_array_identifier_away_no_nulls, player_id_away_no_nulls, player_id_home_no_nulls;
    String[] action_array_away, action_id_array_away, action_array_home_no_nulls, action_id_array_home_no_nulls, action_player_array_home_no_nulls;
    String[] action_array_away_no_nulls, action_id_array_away_no_nulls, action_player_array_away_no_nulls;
    int[] players_scored_home, players_scored_away, players_def_home, players_def_away;
    List<PieEntry> entries = new ArrayList<>();
    List<PieEntry> entries_away = new ArrayList<>();
    List<PieEntry> entriesdef = new ArrayList<>();
    List<PieEntry> entries_defaway = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_individual);

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
        position = getIntent().getStringExtra("POSITION");
        Log.d(TAG, "position" +(position));

        selectedHome = (TextView) findViewById(R.id.selectedHome);
        selectedAway = (TextView) findViewById(R.id.selectedAway);
        selectedHomePts = (TextView) findViewById(R.id.selectedHomePts);
        selectedAwayPts = (TextView) findViewById(R.id.selectedAwayPts);
        selectedHomeReb = (TextView) findViewById(R.id.selectedHomeReb);
        selectedAwayReb = (TextView) findViewById(R.id.selectedAwayReb);
        selectedHomeBlk = (TextView) findViewById(R.id.selectedHomeBlk);
        selectedAwayBlk = (TextView) findViewById(R.id.selectedAwayBlk);
        selectedHomeAst = (TextView) findViewById(R.id.selectedHomeAst);
        selectedAwayAst = (TextView) findViewById(R.id.selectedAwayAst);
        selectedHomeStl = (TextView) findViewById(R.id.selectedHomeStl);
        selectedAwayStl = (TextView) findViewById(R.id.selectedAwayStl);
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        pieChartAway = (PieChart) findViewById(R.id.pie_chartaway);
        pieChartDef = (PieChart) findViewById(R.id.pie_chartdef);
        pieChartDefAway = (PieChart) findViewById(R.id.pie_chartdefaway);
        getAllTeams();
    }

    private void changeUrlsToNames() {
        name_array_home = new String[length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < (team_length * 2); j++) {
                int k = i;
                int l = j;
                if (home_array[k].equals("http://178.62.2.33:8000/api/team/" + team_array[l] + "/?format=json")) {
                    name_array_home[k] = (team_array[l += 1]);
                }
            }
        }
        //Log.d(TAG, "name_array_home" + Arrays.toString(name_array_home));

        name_array_away = new String[length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < (team_length * 2); j++) {
                int k = i;
                int l = j;
                if (away_array[k].equals("http://178.62.2.33:8000/api/team/" + team_array[l] + "/?format=json")) {
                    name_array_away[k] = (team_array[l += 1]);
                }
            }
        }
        //Log.d(TAG, "name_array_away" + Arrays.toString(name_array_away));
        selected_fixture_id = id_array[Integer.parseInt(position)];
        selected_home_id = home_array[Integer.parseInt(position)];
        selected_away_id = away_array[Integer.parseInt(position)];
        selected_home_name = name_array_home[Integer.parseInt(position)];
        selected_away_name = name_array_away[Integer.parseInt(position)];

        //Log.d(TAG, "selected_fixture_id" + (selected_fixture_id));
        //Log.d(TAG, "selected_home_id" + (selected_home_id));
        //Log.d(TAG, "selected_away_id" + (selected_away_id));
        getAllPlayers();
    }

    private void restOfPlayer() {
        player_array_identifier_away_no_nulls = new String[player_array_identifier_length_away*2];
        player_array_identifier_home_no_nulls = new String[player_array_identifier_length_home*2];
        player_id_away_no_nulls = new String[player_array_identifier_length_away];
        player_id_home_no_nulls = new String[player_array_identifier_length_home];
        int n = 0;
        int m = 0;
        int q = 0;
        int r = 0;
        for (int i = 0; i < player_array_identifier_away.length; i++) {
            if(player_array_identifier_away[i] != null){
                player_array_identifier_away_no_nulls[n] = player_array_identifier_away[i];
                n += 1;
            }
        }

        for (int i = 0; i < player_array_identifier_away.length; i++) {
            if(player_id_away[i] != null){
                player_id_away_no_nulls[q] = player_id_away[i];
                q += 1;
            }
        }

        for (int i = 0; i < player_array_identifier_home.length; i++) {
            if(player_array_identifier_home[i] != null){
                player_array_identifier_home_no_nulls[m] = player_array_identifier_home[i];
                m += 1;
            }
        }

        for (int i = 0; i < player_array_identifier_home.length; i++) {
            if(player_id_home[i] != null){
                player_id_home_no_nulls[r] = player_id_home[i];
                r += 1;
            }
        }
        //Log.d(TAG, "player_array_identifier_home_no_nulls"+Arrays.toString(player_array_identifier_home_no_nulls));
        //Log.d(TAG, "player_id_home_no_nulls"+Arrays.toString(player_id_home_no_nulls));
        //Log.d(TAG, "player_array_identifier_away_no_nulls"+Arrays.toString(player_array_identifier_away_no_nulls));
        //Log.d(TAG, "player_id_away_no_nulls"+Arrays.toString(player_id_away_no_nulls));
        getAllActions();
    }
    private void restOfActions(){
        action_array_home_no_nulls = new String[action_length_home];
        action_id_array_home_no_nulls = new String[action_length_home];
        action_player_array_home_no_nulls = new String[action_length_home];
        int n = 0;
        int o = 0;
        int q = 0;
        for (int i = 0; i < action_array_home.length; i++) {
            if(action_array_home[i] != null){
                action_array_home_no_nulls[n] = action_array_home[i];
                n += 1;
            }
        }
        for (int i = 0; i < action_id_array_home.length; i++) {
            if(action_id_array_home[i] != null){
                action_id_array_home_no_nulls[o] = action_id_array_home[i];
                o += 1;
            }
        }
        for (int i = 0; i < action_player_array_home.length; i++) {
            if(action_player_array_home[i] != null){
                action_player_array_home_no_nulls[q] = action_player_array_home[i];
                q += 1;
            }
        }
        //Log.d(TAG, "action_array_home_no_nulls"+Arrays.toString(action_array_home_no_nulls));
        //Log.d(TAG, "action_id_array_home_no_nulls"+Arrays.toString(action_id_array_home_no_nulls));
        //Log.d(TAG, "action_player_array_home_no_nulls"+Arrays.toString(action_player_array_home_no_nulls));

        action_array_away_no_nulls = new String[action_length_away];
        action_id_array_away_no_nulls = new String[action_length_away];
        action_player_array_away_no_nulls = new String[action_length_away];
        int s = 0;
        int t = 0;
        int v = 0;
        for (int i = 0; i < action_array_away.length; i++) {
            if(action_array_away[i] != null){
                action_array_away_no_nulls[s] = action_array_away[i];
                s += 1;
            }
        }
        for (int i = 0; i < action_id_array_away.length; i++) {
            if(action_id_array_away[i] != null){
                action_id_array_away_no_nulls[t] = action_id_array_away[i];
                t += 1;
            }
        }
        for (int i = 0; i < action_player_array_away.length; i++) {
            if(action_player_array_away[i] != null){
                action_player_array_away_no_nulls[v] = action_player_array_away[i];
                v += 1;
            }
        }
        //Log.d(TAG, "action_array_away_no_nulls"+Arrays.toString(action_array_away_no_nulls));
        //Log.d(TAG, "action_id_array_away_no_nulls"+Arrays.toString(action_id_array_away_no_nulls));
        //Log.d(TAG, "action_player_array_away_no_nulls"+Arrays.toString(action_player_array_away_no_nulls));

        for (int i = 0; i < action_array_home_no_nulls.length; i++) {//go through all actions
            int k = i;
            //if the action was a score
            switch (action_array_home_no_nulls[k]) {
                case ("one"):
                    home_score = home_score + 1;
                    break;
                case ("two"):
                    home_score = home_score + 2;
                    break;
                case ("three"):
                    home_score = home_score + 3;
                    break;
                case ("rebound"):
                    home_rebound = home_rebound + 1;
                    break;
                case ("block"):
                    home_block = home_block + 1;
                    break;
                case ("assist"):
                    home_assist = home_assist + 1;
                    break;
                case ("steal"):
                    home_steal = home_steal + 1;
                    break;
            }
        }

        for (int i = 0; i < action_array_away_no_nulls.length; i++) {//go through all actions
            int k = i;
            switch (action_array_away_no_nulls[k]) {
                case ("one"):
                    away_score = away_score + 1;
                    break;
                case ("two"):
                    away_score = away_score + 2;
                    break;
                case ("three"):
                    away_score = away_score + 3;
                    break;
                case ("rebound"):
                    away_rebound = away_rebound + 1;
                    break;
                case ("block"):
                    away_block = away_block + 1;
                    break;
                case ("assist"):
                    away_assist = away_assist + 1;
                    break;
                case ("steal"):
                    away_steal = away_steal + 1;
                    break;
            }
        }

        selectedHome.setText(selected_home_name);
        selectedAway.setText(selected_away_name);
        selectedHomePts.setText(String.valueOf(home_score));
        selectedAwayPts.setText(String.valueOf(away_score));
        selectedHomeReb.setText(String.valueOf(home_rebound));
        selectedAwayReb.setText(String.valueOf(away_rebound));
        selectedHomeBlk.setText(String.valueOf(home_block));
        selectedAwayBlk.setText(String.valueOf(away_block));
        selectedHomeAst.setText(String.valueOf(home_assist));
        selectedAwayAst.setText(String.valueOf(away_assist));
        selectedHomeStl.setText(String.valueOf(home_steal));
        selectedAwayStl.setText(String.valueOf(away_steal));

        players_scored_home = new int[player_id_home_no_nulls.length];
        for (int i = 0; i < action_array_home_no_nulls.length; i++) {
            int k = i;
            if(action_array_home_no_nulls[k].equals("one") || action_array_home_no_nulls[k].equals("two") || action_array_home_no_nulls[k].equals("three")) {
                for (int j = 0; j < player_id_home_no_nulls.length; j++) {
                    int m = j;
                    if(action_player_array_home_no_nulls[k].equals("http://178.62.2.33:8000/api/player/"+player_id_home_no_nulls[m]+ "/?format=json")) {
                        switch (action_array_home_no_nulls[k]) {
                            case ("one"):
                                players_scored_home[m] = players_scored_home[m] + 1;
                                break;
                            case ("two"):
                                players_scored_home[m] = players_scored_home[m] + 2;
                                break;
                            case ("three"):
                                players_scored_home[m] = players_scored_home[m] + 3;
                                break;
                        }
                    }
                }

            }
        }
        //Log.d(TAG, "players_scored_home" + Arrays.toString(players_scored_home));
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < players_scored_home.length; i++) {
        int b = i;
            if(players_scored_home[b] != 0) {
                    entries.add(new PieEntry(Float.parseFloat((players_scored_home[b] + "f")), (player_array_identifier_home_no_nulls[b + b +1])));
            }
        }
        //entries.add(new PieEntry(18.5f, "Green"));
        //Log.d(TAG, "entries" + (entries));
        //Log.d(TAG, "name_array_home" + Arrays.toString(player_array_identifier_home_no_nulls));
        PieDataSet set = new PieDataSet(entries, "Players");
        PieData data = new PieData(set);
        //data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        final int[] MY_COLORS = {Color.rgb(2, 168, 244), Color.rgb(45, 204, 112), Color.rgb(241, 196, 15),
                Color.rgb(233, 77, 55), Color.rgb(243, 66, 126), Color.rgb(156, 68, 243), Color.rgb(76, 235, 76)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);
        set.setColors(colors);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Home Score Breakdown");
        pieChart.setCenterTextSize(20);
        pieChart.setEntryLabelTextSize(10);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        data.setValueTextSize(13f);
        pieChart.invalidate(); // refresh
        pieChart.animateXY(1400, 1400);

        players_scored_away = new int[player_id_away_no_nulls.length];
        for (int i = 0; i < action_array_away_no_nulls.length; i++) {
            int k = i;
            if(action_array_away_no_nulls[k].equals("one") || action_array_away_no_nulls[k].equals("two") || action_array_away_no_nulls[k].equals("three")) {
                for (int j = 0; j < player_id_away_no_nulls.length; j++) {
                    int m = j;
                    if(action_player_array_away_no_nulls[k].equals("http://178.62.2.33:8000/api/player/"+player_id_away_no_nulls[m]+ "/?format=json")) {
                        switch (action_array_away_no_nulls[k]) {
                            case ("one"):
                                players_scored_away[m] = players_scored_away[m] + 1;
                                break;
                            case ("two"):
                                players_scored_away[m] = players_scored_away[m] + 2;
                                break;
                            case ("three"):
                                players_scored_away[m] = players_scored_away[m] + 3;
                                break;
                        }
                    }
                }

            }
        }
        //Log.d(TAG, "players_scored_home" + Arrays.toString(players_scored_home));
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < players_scored_away.length; i++) {
            int b = i;
            if(players_scored_away[b] != 0) {
                entries_away.add(new PieEntry(Float.parseFloat((players_scored_away[b] + "f")), (player_array_identifier_away_no_nulls[b + b +1])));
            }
        }
        //entries.add(new PieEntry(18.5f, "Green"));
        //Log.d(TAG, "entries" + (entries));
        //Log.d(TAG, "name_array_home" + Arrays.toString(player_array_identifier_home_no_nulls));
        PieDataSet set_away = new PieDataSet(entries_away, "Players");
        PieData data_away = new PieData(set_away);
        //data.setValueFormatter(new PercentFormatter());
        pieChartAway.setData(data_away);
        set_away.setColors(colors);
        pieChartAway.setEntryLabelColor(Color.BLACK);
        pieChartAway.setCenterText("Away Score Breakdown");
        pieChartAway.setCenterTextSize(20);
        pieChartAway.setEntryLabelTextSize(10);
        pieChartAway.getLegend().setEnabled(false);
        pieChartAway.getDescription().setEnabled(false);
        data_away.setValueTextSize(13f);
        pieChartAway.invalidate(); // refresh
        pieChartAway.animateXY(1400, 1400);

        players_def_home = new int[player_id_home_no_nulls.length];
        for (int i = 0; i < action_array_home_no_nulls.length; i++) {
            int k = i;
            if(action_array_home_no_nulls[k].equals("block") || action_array_home_no_nulls[k].equals("steal") || action_array_home_no_nulls[k].equals("rebound") || action_array_home_no_nulls[k].equals("assist")) {
                for (int j = 0; j < player_id_home_no_nulls.length; j++) {
                    int m = j;
                    if(action_player_array_home_no_nulls[k].equals("http://178.62.2.33:8000/api/player/"+player_id_home_no_nulls[m]+ "/?format=json")) {
                        players_def_home[m] = players_def_home[m] + 1;
                    }
                }

            }
        }
        //Log.d(TAG, "players_scored_home" + Arrays.toString(players_scored_home));
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < players_def_home.length; i++) {
            int b = i;
            if(players_def_home[b] != 0) {
                entriesdef.add(new PieEntry(Float.parseFloat((players_def_home[b] + "f")), (player_array_identifier_home_no_nulls[b + b +1])));
            }
        }
        //entries.add(new PieEntry(18.5f, "Green"));
        //Log.d(TAG, "entries" + (entries));
        //Log.d(TAG, "name_array_home" + Arrays.toString(player_array_identifier_home_no_nulls));
        PieDataSet set_homedef = new PieDataSet(entriesdef, "Players");
        PieData data_homedef = new PieData(set_homedef);
        //data.setValueFormatter(new PercentFormatter());
        pieChartDef.setData(data_homedef);
        set_homedef.setColors(colors);
        pieChartDef.setEntryLabelColor(Color.BLACK);
        pieChartDef.setCenterText("Defensive Breakdown");
        pieChartDef.setCenterTextSize(20);
        pieChartDef.setEntryLabelTextSize(10);
        pieChartDef.getLegend().setEnabled(false);
        pieChartDef.getDescription().setEnabled(false);
        data_homedef.setValueTextSize(13f);
        pieChartDef.invalidate(); // refresh
        pieChartDef.animateXY(1400, 1400);

        players_def_away = new int[player_id_away_no_nulls.length];
        for (int i = 0; i < action_array_away_no_nulls.length; i++) {
            int k = i;
            if(action_array_away_no_nulls[k].equals("block") || action_array_away_no_nulls[k].equals("steal") || action_array_away_no_nulls[k].equals("rebound") || action_array_away_no_nulls[k].equals("assist")) {
                for (int j = 0; j < player_id_away_no_nulls.length; j++) {
                    int m = j;
                    if(action_player_array_away_no_nulls[k].equals("http://178.62.2.33:8000/api/player/"+player_id_away_no_nulls[m]+ "/?format=json")) {
                        players_def_away[m] = players_def_away[m] + 1;
                    }
                }

            }
        }
        //Log.d(TAG, "players_scored_home" + Arrays.toString(players_scored_home));
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < players_def_away.length; i++) {
            int b = i;
            if(players_def_away[b] != 0) {
                entries_defaway.add(new PieEntry(Float.parseFloat((players_def_away[b] + "f")), (player_array_identifier_away_no_nulls[b + b +1])));
            }
        }
        //entries.add(new PieEntry(18.5f, "Green"));
        //Log.d(TAG, "entries" + (entries));
        //Log.d(TAG, "name_array_home" + Arrays.toString(player_array_identifier_home_no_nulls));
        PieDataSet set_homedefaway = new PieDataSet(entries_defaway, "Players");
        PieData data_homedefaway = new PieData(set_homedefaway);
        //data.setValueFormatter(new PercentFormatter());
        pieChartDefAway.setData(data_homedefaway);
        set_homedefaway.setColors(colors);
        pieChartDefAway.setEntryLabelColor(Color.BLACK);
        pieChartDefAway.setCenterText("Defensive Breakdown");
        pieChartDefAway.setCenterTextSize(20);
        pieChartDefAway.setEntryLabelTextSize(10);
        pieChartDefAway.getLegend().setEnabled(false);
        pieChartDefAway.getDescription().setEnabled(false);
        data_homedefaway.setValueTextSize(13f);
        pieChartDefAway.invalidate(); // refresh
        pieChartDefAway.animateXY(1400, 1400);
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
                            //Log.d(TAG, "team_length"+(team_length));
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
        MySingleton.getInstance(IndividualResult.this).addToRequestQueue(jsonArrayRequest);
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
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject fixture = response.getJSONObject(i);

                                // Get the current player (json object) data
                                String home = fixture.getString("home_team");
                                String away = fixture.getString("away_team");
                                String date = fixture.getString("fixture_date");
                                String id = fixture.getString("fixture_id");
                                int j = i;

                                //add to the array used for POST
                                home_array[j] = home;
                                away_array[j] = away;
                                date_array[j] = date;
                                id_array[j] = id;
                                length += 1;
                            }
                            //Log.d(TAG, "home" + Arrays.toString(home_array));
                            //Log.d(TAG, "away" + Arrays.toString(away_array));
                            //Log.d(TAG, "date" + Arrays.toString(date_array));
                            //Log.d(TAG, "id" + Arrays.toString(id_array));
                            //Log.d(TAG, "length" + (length));
                            changeUrlsToNames();
                        } catch (JSONException e) {
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
        MySingleton.getInstance(IndividualResult.this).addToRequestQueue(jsonArrayRequest);
    }

    private void getAllPlayers(){
        String url = "http://178.62.2.33:8000/api/player/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        player_array_identifier_home = new String[(response.length()*2)];
                        player_id_home = new String[(response.length()*2)];
                        player_array_identifier_length_home = 0;

                        player_array_identifier_away = new String[(response.length()*2)];
                        player_id_away = new String[(response.length()*2)];
                        player_array_identifier_length_away = 0;
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

                                if(selected_home_id.equals(team)) {
                                    player_array_identifier_length_home += 1;
                                    int j = i;
                                    int h = i;
                                    player_id_home[h] = id;
                                    //add to the array used for POST
                                    if (j == 0) {
                                        player_array_identifier_home[j] = id;
                                        player_array_identifier_home[j += 1] = number + " - " + name;
                                    }
                                    if (j == 1) {
                                        player_array_identifier_home[j += 1] = id;
                                        player_array_identifier_home[j += 1] = number + " - " + name;
                                    }
                                    if (j > 1) {
                                        int k = (j + j);
                                        int l = (j + j + 1);
                                        player_array_identifier_home[k] = id;
                                        player_array_identifier_home[l] = number + " - " + name;
                                    }
                                }

                                if(selected_away_id.equals(team)) {
                                    player_array_identifier_length_away += 1;
                                    int j = i;
                                    int h = i;
                                    player_id_away[h] = id;
                                    //add to the array used for POST
                                    if (j == 0) {
                                        player_array_identifier_away[j] = id;
                                        player_array_identifier_away[j += 1] = number + " - " + name;
                                    }
                                    if (j == 1) {
                                        player_array_identifier_away[j += 1] = id;
                                        player_array_identifier_away[j += 1] = number + " - " + name;
                                    }
                                    if (j > 1) {
                                        int k = (j + j);
                                        int l = (j + j + 1);
                                        player_array_identifier_away[k] = id;
                                        player_array_identifier_away[l] = number + " - " + name;
                                    }
                                }

                            }
                            //Log.d(TAG, "player_array_identifier_away"+Arrays.toString(player_array_identifier_away));
                            //Log.d(TAG, "player_array_identifier_home"+Arrays.toString(player_array_identifier_home));
                            //Log.d(TAG, "player_array_identifier_length_away"+(player_array_identifier_length_away));
                            //Log.d(TAG, "player_array_identifier_length_home"+(player_array_identifier_length_home));
                            //Log.d(TAG, "player_id_home"+Arrays.toString((player_id_home)));
                            //Log.d(TAG, "player_id_away"+Arrays.toString((player_id_away)));
                            restOfPlayer();
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
        MySingleton.getInstance(IndividualResult.this).addToRequestQueue(jsonArrayRequest);

    }
    private void getAllActions() {
        String url = "http://178.62.2.33:8000/api/action/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        action_length_home = 0;
                        action_array_home = new String[(response.length())];
                        action_id_array_home = new String[(response.length())];
                        action_player_array_home = new String[(response.length())];

                        action_length_away = 0;
                        action_array_away = new String[(response.length())];
                        action_id_array_away = new String[(response.length())];
                        action_player_array_away = new String[(response.length())];
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject json_object = response.getJSONObject(i);
                                // Get the current team (json object) data
                                String id = json_object.getString("action_id");
                                String type = json_object.getString("action_type");
                                String fixture = json_object.getString("fixture_id");
                                String player = json_object.getString("player_id");

                                if(fixture.equals("http://178.62.2.33:8000/api/fixture/"+selected_fixture_id+"/?format=json")) {
                                    int j = i;
                                    for(int n=0;n<player_id_away_no_nulls.length;n++) {
                                        if (player.equals("http://178.62.2.33:8000/api/player/"+player_id_home_no_nulls[n]+"/?format=json")) {
                                            action_length_home = action_length_home + 1;
                                            action_array_home[j] = type;
                                            action_id_array_home[j] = id;
                                            action_player_array_home[j] = player;
                                        }
                                        if (player.equals("http://178.62.2.33:8000/api/player/"+player_id_away_no_nulls[n]+"/?format=json")) {
                                            action_length_away = action_length_away + 1;
                                            action_array_away[j] = type;
                                            action_id_array_away[j] = id;
                                            action_player_array_away[j] = player;
                                        }
                                    }
                                }
                            }
                            //Log.d(TAG, "action_array_home"+Arrays.toString(action_array_home));
                            //Log.d(TAG, "action_id_array_home"+Arrays.toString(action_id_array_home));
                            //Log.d(TAG, "action_player_array_home"+Arrays.toString(action_player_array_home));
                            //Log.d(TAG, "action_length_home"+(action_length_home));
                            restOfActions();
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
        MySingleton.getInstance(IndividualResult.this).addToRequestQueue(jsonArrayRequest);
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
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
