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

import static android.content.ContentValues.TAG;

/**
 * Created by alann on 23/01/2018.
 */

public class ResultsContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder { //create and customize the recyclerView. RecyclerView is a container for displaying large data sets that can be scrolled very efficiently by maintaining a limited number of views
        public ImageView resultpic;
        public TextView fixture;
        public TextView result;
        public Button win_or_lose;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_results, parent, false));
            resultpic = (ImageView) itemView.findViewById(R.id.card_resultpic);
            fixture = (TextView) itemView.findViewById(R.id.card_fixture);
            result = (TextView) itemView.findViewById(R.id.card_result);
            win_or_lose = (Button) itemView.findViewById(R.id.win_or_lose_button);
            //itemView.setOnClickListener(new View.OnClickListener() {
               // @Override
               // public void onClick(View v) {
                 //   Context context = v.getContext();
                 //   Intent intent = new Intent(context, DetailActivity.class);
                 //   intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                 //   context.startActivity(intent);
                //}
           // });
        }
    }

    //Adapter to display recycler view.
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        //private static final int LENGTH = 18;

        private final String[] mFixtures;
        private final String[] mResults;
        private final Drawable[] mResultPic;
        private final String[] mWin_or_Lose;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mFixtures = resources.getStringArray(R.array.fixtures);
            mResults = resources.getStringArray(R.array.results);
            mWin_or_Lose = resources.getStringArray(R.array.win_or_lose);
            mResultPic = new Drawable[mFixtures.length];

            int win = 0;
            int lose = 1;
            for (int j = 0; j < mWin_or_Lose.length; j++) {
                if (mWin_or_Lose[j].equals("Win")) {
                    mResultPic[j] = resources.getDrawable(R.drawable.star);
                }
                if (mWin_or_Lose[j].equals("Loss")) {
                    mResultPic[j] = resources.getDrawable(R.drawable.unchecked);
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.resultpic.setImageDrawable(mResultPic[position % mResultPic.length]);
            holder.fixture.setText(mFixtures[position % mFixtures.length]);
            holder.result.setText(mResults[position % mResults.length]);
            holder.win_or_lose.setText(mWin_or_Lose[position % mWin_or_Lose.length]);
        }

        @Override
        public int getItemCount() {
            return mFixtures.length;
        }
    }
}
