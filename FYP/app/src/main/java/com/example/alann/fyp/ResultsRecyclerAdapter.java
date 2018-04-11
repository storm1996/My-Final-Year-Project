package com.example.alann.fyp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by alann on 09/04/2018.
 */

public class ResultsRecyclerAdapter extends RecyclerView.Adapter<ResultsContentFragment.ViewHolder> {

    fixture fixtures;
    fixture result;
    private List<fixture> mFixtures;
    private List<fixture> mResults;
    private Context mContext;

    public ResultsRecyclerAdapter(Context context, List<fixture> fixtures, List<fixture> result) {
        this.mFixtures = fixtures;
        this.mResults = result;
    }

    @Override
    public ResultsContentFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_results, parent, false);
        return new ResultsContentFragment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultsContentFragment.ViewHolder holder, int position) {
        holder.fixture.setText(mFixtures.get(position).getFixture_name());
        holder.result.setText(mResults.get(position).getResult());
    }

    @Override
    public int getItemCount() {
        //if (mFixtures != null) {
        return mFixtures.size();
        //}
        //return 0;
    }
}
