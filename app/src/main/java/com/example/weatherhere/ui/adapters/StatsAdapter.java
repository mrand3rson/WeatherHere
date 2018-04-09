package com.example.weatherhere.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherhere.R;
import com.example.weatherhere.mvp.models.QueryInfo;
import com.example.weatherhere.ui.activities.ResponseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.weatherhere.sources.Constants.QUERY_INFO;

/**
 * Created by Andrei on 07.04.2018.
 */

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder>{

    private final static String PATTERN_COORDS = "%f : %f";
    private final static String PATTERN_DATE = "dd.MM.yyyy HH:mm";
    private final Context mContext;
    private final int mResource;

    public List<QueryInfo> getData() {
        return mData;
    }
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
        String zonedDateByPattern = convertDateLongToString(mData.get(position).dateTime, PATTERN_DATE);
        holder.date.setText(zonedDateByPattern);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private String convertDateLongToString(long date, String format){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            //hardcoded timezone :(
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+03:00"));
            return sdf.format(new Date(date));
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.city)
        TextView city;

        @BindView(R.id.coords)
        TextView coords;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.card)
        CardView card;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            card.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ResponseActivity.class);
                QueryInfo info = mData.get(getAdapterPosition());
                intent.putExtra(QUERY_INFO, info);
                mContext.startActivity(intent);
            });
        }
    }
}
