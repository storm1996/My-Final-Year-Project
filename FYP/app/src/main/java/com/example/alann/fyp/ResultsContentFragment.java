package com.example.alann.fyp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by alann on 23/01/2018.
 */

public class ResultsContentFragment extends Fragment {
    private static final String TAG = "ResultsContentFragment";
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    List<fixture> fixturesData = new ArrayList<>();
    List<fixture> resultsData = new ArrayList<>();
    RecyclerView recyclerView;
    ResultsRecyclerAdapter adapter;
    String[] nameArraystring, name_array_home, name_array_away, team_array, home_array, away_array, date_array, id_array;
    String[] action_array, fixture_array, action_id_array, player_array_identifier, home_away_array, player_id, action_player_array, resultArraystring;
    String current_away_team, current_home_team, current_player_team;
    int length, team_length, player_array_identifier_length, action_length = 0;
    int[] home_score, away_score;

    public ResultsContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getAllTeams();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_results, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvResults);
        setAdapterWithData();
        return rootView;
    }

    public void setAdapterWithData(){
        Log.d(TAG, "fixturesData" +String.valueOf(fixturesData));
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layout);
        adapter = new ResultsRecyclerAdapter(getContext(), fixturesData, resultsData);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Updating List ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();
                }
            }
        }).start();

        fixturesData.clear();
        resultsData.clear();
        getAllTeams();
        setAdapterWithData();
        progressBarStatus =  100;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder { //create and customize the recyclerView. RecyclerView is a container for displaying large data sets that can be scrolled very efficiently by maintaining a limited number of views
        public ImageView resultpic;
        public TextView fixture;
        public TextView result;
        public String position;

        public ViewHolder(View itemView) {
            super(itemView);
            resultpic = itemView.findViewById(R.id.card_resultpic);
            resultpic.setBackgroundColor(Color.rgb(255, 184, 102));
            fixture = itemView.findViewById(R.id.card_fixture);
            result = itemView.findViewById(R.id.card_result);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, IndividualResult.class);
                    position = String.valueOf(getAdapterPosition());
                    intent.putExtra("POSITION", position);
                    context.startActivity(intent);
                }
            });
        }
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

        nameArraystring = new String[length];
        for (int i = 0; i < length; i++) {
            nameArraystring[i] = name_array_home[i] + " vs. " + name_array_away[i] + "\n" + date_array[i];
        }

        home_score = new int[id_array.length];
        away_score = new int[id_array.length];
        for (int i = 0; i < action_array.length; i++) {//go through all actions
            int k = i;
            //if the action was a score
            if(action_array[k].equals("one") || action_array[k].equals("two") || action_array[k].equals("three")) {
                for (int m = 0; m < id_array.length; m++) {
                    //which fixture does the action belong to
                    if (fixture_array[k].equals("http://178.62.2.33:8000/api/fixture/" + id_array[m] + "/?format=json")) {
                        //the teams involved in the fixture
                        current_home_team = home_array[m];
                        current_away_team = away_array[m];
                        //go through each player
                        for (int j = 0; j < player_id.length; j++) {
                            int l = j;
                            //what team is the player associated with
                            if (action_player_array[k].equals("http://178.62.2.33:8000/api/player/" + player_id[l] + "/?format=json")) {
                                current_player_team = home_away_array[l];
                                if (current_player_team.equals(current_home_team)) {//record score if it's home
                                    switch (action_array[k]) {
                                        case ("one"):
                                            home_score[m] = home_score[m] + 1;
                                            break;
                                        case ("two"):
                                            home_score[m] = home_score[m] + 2;
                                            break;
                                        case ("three"):
                                            home_score[m] = home_score[m] + 3;
                                            break;
                                    }
                                }
                                if (current_player_team.equals(current_away_team)) {//record score if it's away
                                    switch (action_array[k]) {
                                        case ("one"):
                                            away_score[m] = away_score[m] + 1;
                                            break;
                                        case ("two"):
                                            away_score[m] = away_score[m] + 2;
                                            break;
                                        case ("three"):
                                            away_score[m] = away_score[m] + 3;
                                            break;
                                    }
                                }
                            }

                        }

                    }
                }
            }
        }
        resultArraystring = new String[length];
        for (int i = 0; i < length; i++) {
            resultArraystring[i] = home_score[i] + " - " + away_score[i];
        }
        for (int i = 0; i < length; i++) {
            fixture f = new fixture();
            f.fixture_name = nameArraystring[i];
            fixturesData.add(f);
        }
        for (int i = 0; i < length; i++) {
            fixture f = new fixture();
            f.result = resultArraystring[i];
            resultsData.add(f);
        }

        setAdapterWithData();
    }

    private void getAllTeams(){
        Log.d(TAG, "team_length"+(team_length));
        String url = "http://178.62.2.33:8000/api/team/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        team_length = 0;
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
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
                        length = 0;
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
                            //Log.d(TAG, "home"+Arrays.toString(home_array));
                            //Log.d(TAG, "away"+Arrays.toString(away_array));
                            //Log.d(TAG, "date"+Arrays.toString(date_array));
                            //Log.d(TAG, "id"+Arrays.toString(id_array));
                            //Log.d(TAG, "length"+(length));
                            getAllPlayers();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void getAllPlayers(){
        String url = "http://178.62.2.33:8000/api/player/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        player_array_identifier = new String[(response.length()*2)];
                        home_away_array = new String[(response.length())];
                        player_id = new String[(response.length())];
                        player_array_identifier_length = 0;
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
                                player_id[h] = id;
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
                            //Log.d(TAG, "player_array_identifier"+Arrays.toString(player_array_identifier));
                            //Log.d(TAG, "player_array_identifier_length"+(player_array_identifier_length));
                            //Log.d(TAG, "home_away_array"+Arrays.toString((home_away_array)));
                            //Log.d(TAG, "player_id"+Arrays.toString((player_id)));
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);

    }
    private void getAllActions() {
        String url = "http://178.62.2.33:8000/api/action/?format=json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        action_length = 0;
                        action_array = new String[(response.length())];
                        fixture_array = new String[(response.length())];
                        action_id_array = new String[(response.length())];
                        action_player_array = new String[(response.length())];
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
                                int j = i;
                                action_length = action_length +1;
                                //add to the array used for POST
                                    action_array[j]= type;
                                    fixture_array[j]= fixture;
                                    action_id_array[j]= id;
                                    action_player_array[j]= player;
                            }
                            //Log.d(TAG, "action_array"+Arrays.toString(action_array));
                            //Log.d(TAG, "fixture_array"+Arrays.toString(fixture_array));
                            //Log.d(TAG, "action_id_array"+Arrays.toString(action_id_array));
                            //Log.d(TAG, "action_player_array"+Arrays.toString(action_player_array));
                            //Log.d(TAG, "action_length"+(action_length));
                            changeUrlsToNames();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }


}