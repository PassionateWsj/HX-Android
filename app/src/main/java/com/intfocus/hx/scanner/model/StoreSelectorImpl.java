package com.intfocus.hx.scanner.model;

import android.content.Context;

import com.intfocus.hx.data.response.scanner.StoreItem;
import com.intfocus.hx.db.OrmDBHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/22 上午10:00
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */

public class StoreSelectorImpl implements StoreSelectorModel {
    @Override
    public void loadData(Context mContext, final String keyWord, final OnStoreSelectorResultListener listener) {

        try {
            final Dao<StoreItem, Long> storeItemDao = OrmDBHelper.getInstance(mContext).getStoreItemDao();
            Observable.create(new Observable.OnSubscribe<List<StoreItem>>() {
                @Override
                public void call(Subscriber<? super List<StoreItem>> subscriber) {
                    try {
                        List<StoreItem> storeItems;
                        if ("".equals(keyWord))
                            storeItems = storeItemDao.queryForAll();
                        else
                            storeItems = storeItemDao.queryBuilder().where().like("name", "%" + keyWord + "%").query();
                        subscriber.onNext(storeItems);
                    } catch (SQLException e) {
                        subscriber.onError(e);
                    }
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<StoreItem>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onResultFailure(e);
                        }

                        @Override
                        public void onNext(List<StoreItem> storeListResult) {
                            listener.onResultSuccess(storeListResult);
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
