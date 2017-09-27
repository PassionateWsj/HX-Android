package com.intfocus.hx.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.intfocus.hx.dashboard.mine.bean.PushMessageBean;
import com.intfocus.hx.data.response.scanner.StoreItem;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/01 下午5:29
 * e-mail: PassionateWsj@outlook.com
 * name: 数据库的帮助类
 * desc:
 * ****************************************************
 */

public class OrmDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "yh_orm.db";
    private static final int DB_VERSION = 1;

    private OrmDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static OrmDBHelper mInstance = null;

    // 双重校验锁单例模式
    public static synchronized OrmDBHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (OrmDBHelper.class) {
                if (mInstance == null)
                    mInstance = new OrmDBHelper(context);
            }
        }
        return mInstance;
    }

    /**
     * 初始化数据，创建表
     *
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // 创建一张 PushMessageBean 的表
            TableUtils.createTable(connectionSource, PushMessageBean.class);

            // 创建一张 StoreItem 的表
            TableUtils.createTable(connectionSource, StoreItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

    /**
     * 获得某个表的DAO类。。。 DAO也叫作数据库中的业务类,这个类里边会封装CURD的方法
     * 第一个: 表示你要操作哪张表 对应的 object对象
     * 第二个: 当前表对应的Object对象中id对应的类型
     * @return
     * @throws SQLException
     */
    public Dao<PushMessageBean, Long> getPushMessageDao() throws SQLException {
        return getDao(PushMessageBean.class);
    }

    public Dao<StoreItem, Long> getStoreItemDao() throws SQLException {
        return getDao(StoreItem.class);
    }


}
