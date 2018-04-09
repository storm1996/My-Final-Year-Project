package com.example.alann.fyp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alann on 31/03/2018.
 */

public class CustomListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;

    //to store the animal images
    private ArrayList<String> nameArray;

    //to store the list of countries
    private final Drawable[] mPlaceAvatars;


    public CustomListAdapter(Activity context, ArrayList<String> nameArray){

        super(context,R.layout.list_row , nameArray);
        this.context=context;
        this.nameArray = nameArray;

        Resources resources = context.getResources();
        TypedArray a = resources.obtainTypedArray(R.array.avatar);
        mPlaceAvatars = new Drawable[a.length()];
        for (int i = 0; i < mPlaceAvatars.length; i++) {
            mPlaceAvatars[i] = a.getDrawable(i);
        }
        a.recycle();

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row, null,true);

        //this code gets references to objects in the list_row.xml file
        TextView gameTextField = (TextView) rowView.findViewById(R.id.list_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_avatar);

        //this code sets the values of the objects to values from the arrays
        gameTextField.setText(nameArray.get(position % nameArray.size()));
        imageView.setImageDrawable(mPlaceAvatars[position % mPlaceAvatars.length]);

        return rowView;

    };

}
