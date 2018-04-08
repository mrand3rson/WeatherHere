package com.example.weatherhere.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.weatherhere.mvp.models.QueryInfo;

import java.util.List;

/**
 * Created by Andrei on 07.04.2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StatsView extends MvpView {
    void showRecords(List<QueryInfo> dataFromDb);
}
