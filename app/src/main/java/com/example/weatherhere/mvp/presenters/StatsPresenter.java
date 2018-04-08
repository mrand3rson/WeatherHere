package com.example.weatherhere.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.MvpPresenter;
import com.example.weatherhere.mvp.models.QueryInfo;
import com.example.weatherhere.mvp.views.StatsView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Andrei on 07.04.2018.
 */

@InjectViewState
public class StatsPresenter extends MvpPresenter<StatsView> {

    private List<QueryInfo> mData;


    public void getAllQueries() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<QueryInfo> results = realm.where(QueryInfo.class).findAll();
        mData = realm.copyFromRealm(results);
        realm.commitTransaction();

        getViewState().showRecords(mData);
    }
}
