package com.example.alann.fyp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alann on 09/04/2018.
 */

public class ResultsRecyclerAdapter extends RecyclerView.Adapter<ResultsRecyclerAdapter.ViewHolder> {

    fixture fixtures;
    private List<fixture> mFixtures;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder { //create and customize the recyclerView. RecyclerView is a container for displaying large data sets that can be scrolled very efficiently by maintaining a limited number of views
        public ImageView resultpic;
        public TextView fixture;
        public TextView result;

        public ViewHolder(View itemView) {
            super(itemView);
            resultpic = itemView.findViewById(R.id.card_resultpic);
            fixture = itemView.findViewById(R.id.card_fixture);
            result = itemView.findViewById(R.id.card_result);
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

    public ResultsRecyclerAdapter(Context context, List<fixture> fixtures) {
        this.mFixtures = fixtures;
    }

    @Override
    public ResultsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_results, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultsRecyclerAdapter.ViewHolder holder, int position) {
        holder.fixture.setText(mFixtures.get(position).getFixture_name());
    }

    @Override
    public int getItemCount() {
        //if (mFixtures != null) {
            return mFixtures.size();
        //}
        //return 0;
    }
}

