package com.example.alann.fyp;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;

/**
 * Created by alann on 31/01/2018.
 */

public class TeamsStatsContentFragment extends Fragment {

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView team_name;
        public XYPlot plot;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_teamstats, parent, false));
            team_name = (TextView) itemView.findViewById(R.id.page_title);
            // initialize our XYPlot reference:
            plot = (XYPlot) itemView.findViewById(R.id.plot);

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

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        // private static final int LENGTH = 18;

        private final String mTeam_name;
        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format;
        private final Number[] xVals;
        private final Number[] yVals;
        public XYPlot plot;
        public XYSeries series1;
        // private final Drawable[] mPlaceAvatars;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            PixelUtils.init(context);
            xVals = new Number[]{35, 73, 78, 88, 65, 68, 79, 77, 55, 53};
            yVals = new Number[]{40, 82, 44, 20, 64, 61, 63, 55, 63, 72};
            mTeam_name = resources.getString(R.string.team_name);
            //TypedArray a = resources.obtainTypedArray(R.array.avatar);
            //mPlaceAvatars = new Drawable[a.length()];
            //for (int i = 0; i < mPlaceAvatars.length; i++) {
                //mPlaceAvatars[i] = a.getDrawable(i);
            //}
            //a.recycle();
            // just for fun, add some smoothing to the lines:

            // turn the above arrays into XYSeries':
            series1 = new SimpleXYSeries(Arrays.asList(xVals), Arrays.asList(yVals), mTeam_name);
            series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);
            series1Format.setInterpolationParams(
                    new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
            // add a new series' to the xyplot:
            plot.addSeries(series1, series1Format);
        }
        @Override
        public TeamsStatsContentFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TeamsStatsContentFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(TeamsStatsContentFragment.ViewHolder holder, int position) {
            //holder.avatar.setImageDrawable(mPlaceAvatars[position % mPlaceAvatars.length]);
            holder.team_name.setText(mTeam_name);
            plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
                @Override
                public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                    int i = Math.round(((Number) obj).floatValue());
                    return toAppendTo.append(xVals[i]);
                }
                @Override
                public Object parseObject(String source, ParsePosition pos) {
                    return null;
                }
            });
        }

        @Override
        public int getItemCount() {return 1;}
    }


}
