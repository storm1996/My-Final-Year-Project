package com.example.alann.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

import static android.content.ContentValues.TAG;

/**
 * Created by alann on 23/01/2018.
 */

public class ResultsContentFragment extends Fragment {
    List<fixture> fixturesData = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<String> nameArray;
    String[] nameArraystring, name_array_home, name_array_away, team_array, home_array, away_array, date_array, id_array;
    int length, team_length = 0;

    public ResultsContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllTeams();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_results, container, false);

        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvResults);

        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);

        ResultsRecyclerAdapter adapter = new ResultsRecyclerAdapter(getContext(), fixturesData);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        Log.d(TAG, "fixturesData" + (fixturesData));
        return rootView;
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
        Log.d(TAG, "name_array_home" + Arrays.toString(name_array_home));

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
        Log.d(TAG, "name_array_home" + Arrays.toString(name_array_away));

        nameArraystring = new String[length];
        for (int i = 0; i < length; i++) {
            nameArraystring[i] = name_array_home[i] + " vs. " + name_array_away[i] + "\n" + date_array[i];
        }

        for (int i = 0; i < length; i++) {
            fixture f = new fixture();
            f.fixture_name = nameArraystring[i];
            fixturesData.add(f);
        }

        ResultsRecyclerAdapter adapter = new ResultsRecyclerAdapter(getContext(), fixturesData);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }


}