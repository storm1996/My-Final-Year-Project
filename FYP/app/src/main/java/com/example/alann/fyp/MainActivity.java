package com.example.alann.fyp;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// CODE FOR THIS CLASS WAS TAKEN FROM https://developer.android.com/training/volley/index.html and http://www.techotopia.com/index.php/Implementing_Android_Custom_Gesture_Recognition_with_Android_Studio
// FOR MORE DETAIL ON THESE REFERENCES PLEASE SEE REPORT

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener{
    GestureLibrary mLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if(!mLibrary.load()){ // GETS CUSTOM PATTERNS I MADE AND PUT IN RAW FILE
            finish();
        }

        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(this); // LISTENER FOR PATTERNS BEING DRAWN BY USER
    }

    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture){
        ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
        final TextView mTxtDisplay1;
        final TextView mTxtDisplay2;
        final TextView mTxtDisplay3;
        final TextView mTxtDisplay4;

        mTxtDisplay1 = (TextView) findViewById(R.id.text1);
        mTxtDisplay2 = (TextView) findViewById(R.id.text2);
        mTxtDisplay3 = (TextView) findViewById(R.id.text3);
        mTxtDisplay4 = (TextView) findViewById(R.id.text4);

        String url = "http://178.62.2.33:8000/api/statsHolder/?format=json"; // URL FOR MY DJANGO APP

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest // PARSING THE JSON VALUES
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject player = response.getJSONObject(i);

                                // Get the current player (json object) data
                                Integer id = player.getInt("id");
                                String playerName = player.getString("playerName");
                                Integer gearNumber = player.getInt("gearNumber");
                                Integer twoPointer = player.getInt("twoPointer");

                                mTxtDisplay1.setText("id: "+ id);
                                mTxtDisplay2.setText("Player Name: "+ playerName);
                                mTxtDisplay3.setText("Gear Number: "+ gearNumber);
                                mTxtDisplay4.setText("2 Pointers Scored: "+ twoPointer);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        mTxtDisplay1.setText("That didn't work!");
                    }
                });

        if (predictions.size() > 0 && predictions.get(0).score > 1.5) { // GETTING MATCH PREDICTIONS FOR THE GESTURES DRAWN BY USERS AND COMPARED TO STORED PATTERNS IN RAW
            String result = predictions.get(0).name;

            if ("Assist".equalsIgnoreCase(result)) {
                //Toast.makeText(this, "Assist Recorded", Toast.LENGTH_LONG).show();
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest); // SENDS REQUEST TO SERVER FOR DATA IN THE SAMPLE TABLE
            }
            if ("A_Loop_Exception".equalsIgnoreCase(result)) {
                Toast.makeText(this, "A Loop Exception Caught", Toast.LENGTH_LONG).show();
            }
        }

    }

}
