package com.example.alann.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by alann on 23/01/2018.
 */

public class FunctionsContentFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // Set padding for Tiles
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView function;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_functions, parent, false));
            avatar = (ImageView) itemView.findViewById(R.id.tile_avatar);
            function = (TextView) itemView.findViewById(R.id.tile_function);
            //itemView.setOnClickListener(new View.OnClickListener() {
                //@Override
               // public void onClick(View v) {
                //    Context context = v.getContext();
                 //   Intent intent = new Intent(context, DetailActivity.class);
                //    intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                //    context.startActivity(intent);
               // }
            //});
        }
    }


    //Adapter to display recycler view.

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Tiles in RecyclerView.
        //private static final int LENGTH = 18;

        private final String[] mFunction;
        //private final Drawable[] mPlaceAvatars;
        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mFunction = resources.getStringArray(R.array.functions);
            TypedArray a = resources.obtainTypedArray(R.array.avatar);
            //mPlaceAvatars = new Drawable[a.length()];
            //for (int i = 0; i < mPlaceAvatars.length; i++) {
                //mPlaceAvatars[i] = a.getDrawable(i);
            //}
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //holder.avatar.setImageDrawable(mPlaceAvatars[position % mPlaceAvatars.length]);
            holder.function.setText(mFunction[position % mFunction.length]);
        }

        @Override
        public int getItemCount() {
            return mFunction.length;
        }
    }
}
