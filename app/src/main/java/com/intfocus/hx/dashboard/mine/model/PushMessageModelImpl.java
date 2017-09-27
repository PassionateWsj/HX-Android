package com.intfocus.hx.dashboard.mine.model;

import android.content.Context;

import com.intfocus.hx.dashboard.mine.bean.PushMessageBean;
import com.intfocus.hx.db.OrmDBHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/01 下午4:56
 * e-mail: PassionateWsj@outlook.com
 * name: 推送消息数据处理类
 * desc: 根据 用户Id 查询数据库中存储的推送消息，结果回调 presenter
 * ****************************************************
 */

public class PushMessageModelImpl implements PushMessageModel {
    @Override
    public void loadData(Context context, final OnPushMessageDataResultListener listener, final int userId) {
        // TODO: RxJava 异步读取数据库
        try {
            final Dao<PushMessageBean, Long> pushMessageDao = OrmDBHelper.getInstance(context).getPushMessageDao();
            Observable.create(new Observable.OnSubscribe<List<PushMessageBean>>() {
                @Override
                public void call(Subscriber<? super List<PushMessageBean>> subscriber) {
                    List<PushMessageBean> data = null;
                    try {
                        data = pushMessageDao.queryBuilder().where().like("user_id", userId).query();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        listener.onPushMessageDataResultFailure();
                        return;
                    }
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<PushMessageBean>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onPushMessageDataResultFailure();
                        }

                        @Override
                        public void onNext(List<PushMessageBean> pushMessageBeen) {
                            listener.onPushMessageDataResultSuccess(pushMessageBeen);

                        }
                    });
        } catch (SQLException e) {
            listener.onPushMessageDataResultFailure();
        }
    }

}
