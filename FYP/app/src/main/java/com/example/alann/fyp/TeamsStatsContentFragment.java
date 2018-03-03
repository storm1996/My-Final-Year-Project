package com.example.alann.fyp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;


import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * Created by alann on 31/01/2018.
 */

public class TeamsStatsContentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView team_name;
        public BarChart barChart;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_teamstats, parent, false));
            team_name = (TextView) itemView.findViewById(R.id.page_title);
            barChart = (BarChart) itemView.findViewById(R.id.bar_chart);
            barChart.animateY(800);
            Description description = new Description();
            description.setText("Points Scored");
            barChart.setDescription(description);
            // data has AxisDependency.LEFT
            YAxis left = barChart.getAxisLeft();
            //left.setDrawLabels(false); // no axis labels
            //left.setDrawAxisLine(false); // no axis line
            left.setDrawGridLines(false); // no grid lines
            left.setDrawZeroLine(true); // draw a zero line
            barChart.getAxisRight().setEnabled(false); // no right axis
            barChart.moveViewToX(10);

            // data has AxisDependency.LEFT
            XAxis right = barChart.getXAxis();
            right.setDrawLabels(false); // no axis labels
            right.setDrawAxisLine(false); // no axis line
            right.setDrawGridLines(false); // no grid lines
            right.setAxisMinimum(0);
            right.setSpaceMax(5f);
        //itemView.setOnClickListener(new View.OnClickListener() {
             // @Override
             // public void onClick(View v) {
              // Context context = v.getContext();
            //   Intent intent = new Intent(context, DetailActivity.class);
            //   intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
            //   context.startActivity(intent);
            // }
            // });
        }
    }

    //Adapter to display recycler view.

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        // private static final int LENGTH = 18;

        private final String mTeam_name;
        BarDataSet set1;
        BarDataSet set2;
        BarData data;
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        float groupSpace = 0.08f;
        int startPos= 0;
        float barWidth= 0.4f;
        float barSpace = 0.03f; // x4 DataSet

        // private final Drawable[] mPlaceAvatars;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mTeam_name = resources.getString(R.string.team_name);
            entries1.add(new BarEntry(1f, 40));
            entries1.add(new BarEntry(2f, 80));
            entries1.add(new BarEntry(3f, 67));
            entries1.add(new BarEntry(4f, 78));
            entries1.add(new BarEntry(5f, 57));
            entries1.add(new BarEntry(6f, 90));

            set1 = new BarDataSet(entries1, "Home");

            entries2.add(new BarEntry(1f, 35));
            entries2.add(new BarEntry(2f, 65));
            entries2.add(new BarEntry(3f, 70));
            entries2.add(new BarEntry(4f, 50));
            entries2.add(new BarEntry(5f, 54));
            entries2.add(new BarEntry(6f, 75));

            set2 = new BarDataSet(entries2, "Away");

            //dataSets.add(set1);
            //dataSets.add(set2);

            //data = new BarData(dataSets);
            data = new BarData(set1, set2);
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set2.setColors(ColorTemplate.MATERIAL_COLORS);
            data.groupBars(startPos, groupSpace, barSpace);
            data.setBarWidth(barWidth);
            //TypedArray a = resources.obtainTypedArray(R.array.avatar);
            //mPlaceAvatars = new Drawable[a.length()];
            //for (int i = 0; i < mPlaceAvatars.length; i++) {
                //mPlaceAvatars[i] = a.getDrawable(i);
            //}
            //a.recycle();

        }
        @Override
        public TeamsStatsContentFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TeamsStatsContentFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(TeamsStatsContentFragment.ViewHolder holder, int position) {
            //holder.avatar.setImageDrawable(mPlaceAvatars[position % mPlaceAvatars.length]);
            holder.team_name.setText(mTeam_name);
            holder.barChart.setData(data);
        }

        @Override
        public int getItemCount() {return 1;}
    }


}
