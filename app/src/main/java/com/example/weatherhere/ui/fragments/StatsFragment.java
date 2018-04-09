package com.example.weatherhere.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.weatherhere.R;
import com.example.weatherhere.mvp.models.QueryInfo;
import com.example.weatherhere.mvp.presenters.StatsPresenter;
import com.example.weatherhere.mvp.views.StatsView;
import com.example.weatherhere.ui.adapters.StatsAdapter;
import com.example.weatherhere.ui.adapters.VerticalSpaceItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andrei on 07.04.2018.
 */

public class StatsFragment extends MvpAppCompatFragment
        implements StatsView {

    public final static int ITEMS_PER_PAGE = 10;
    private final static int VERTICAL_ITEM_SPACING = 10;


    @BindView(R.id.rv)
    RecyclerView mRecycler;

    @InjectPresenter
    StatsPresenter mPresenter;

    private StatsAdapter mAdapter;


    public StatsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.bind(this, v);
        mPresenter.getStatsPage(0);
        return v;
    }

    @Override
    public void showRecords(List<QueryInfo> dataFromDb) {
        if (mAdapter == null) {
            mAdapter = new StatsAdapter(getActivity(), R.layout.recycler_row, dataFromDb);
            mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecycler.setAdapter(mAdapter);
            mRecycler.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACING));
            mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView,
                                                 int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!mRecycler.canScrollVertically(1)) {
                        final int adapterCount = mAdapter.getItemCount();

                        int unfinishedPage = (adapterCount % ITEMS_PER_PAGE > 0 ? 1 : 0);
                        int newPage = adapterCount / ITEMS_PER_PAGE + unfinishedPage;

                        if (adapterCount == mPresenter.getListSize())
                            Toast.makeText(getActivity(), R.string.string_end_of_list, Toast.LENGTH_SHORT).show();
                        else if (adapterCount >= newPage * ITEMS_PER_PAGE) {
                            mPresenter.getStatsPage(newPage);
                        }
                    }
                }
            });
        } else {
            mAdapter.getData().addAll(dataFromDb);
            mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), dataFromDb.size());
        }
    }
}
