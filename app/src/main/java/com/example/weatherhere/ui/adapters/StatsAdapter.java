package com.example.weatherhere.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherhere.R;
import com.example.weatherhere.mvp.models.QueryInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andrei on 07.04.2018.
 */

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder>{

    private final static String PATTERN_COORDS = "%f : %f";
    private final static String PATTERN_DATE = "dd.MM.yyyy hh:mm";
    private final Context mContext;
    private final int mResource;
    private final List<QueryInfo> mData;


    public StatsAdapter(Context context, int resource, List<QueryInfo> data) {
        this.mContext = context;
        this.mResource = resource;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mResource, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.city.setText(mData.get(position).city);
        holder.coords.setText(String.format(Locale.getDefault(),
                PATTERN_COORDS,
                mData.get(position).lat, mData.get(position).lng));
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE, Locale.getDefault());
        String dateByPattern = sdf.format(new Date(mData.get(position).dateTime));
        holder.date.setText(dateByPattern);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.city)
        TextView city;

        @BindView(R.id.coords)
        TextView coords;

        @BindView(R.id.date)
        TextView date;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
