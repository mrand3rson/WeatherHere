package com.example.weatherhere.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.weatherhere.mvp.models.QueryInfo;
import com.example.weatherhere.mvp.views.StatsView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.weatherhere.ui.fragments.StatsFragment.ITEMS_PER_PAGE;

/**
 * Created by Andrei on 07.04.2018.
 */

@InjectViewState
public class StatsPresenter extends MvpPresenter<StatsView> {

    public int getListSize() {
        return listSize;
    }

    private int listSize;

    public void getAllQueries() {
        List<QueryInfo> mData;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<QueryInfo> results = realm.where(QueryInfo.class).findAll();
        mData = realm.copyFromRealm(results);
        realm.commitTransaction();

        getViewState().showRecords(mData);
    }

    //page count starts from 0
    public void getStatsPage(int page) {
        List<QueryInfo> mData;

        int start = page* ITEMS_PER_PAGE;
        int end = start + ITEMS_PER_PAGE;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<QueryInfo> results = realm.where(QueryInfo.class)
                .findAll()
                .sort("dateTime", Sort.DESCENDING);
        realm.commitTransaction();

        listSize = results.size();

        if (end > listSize) {
            mData = realm.copyFromRealm(results).subList(start, listSize);
        } else {
            mData = realm.copyFromRealm(results).subList(start, end);
        }

        getViewState().showRecords(mData);
    }
}
