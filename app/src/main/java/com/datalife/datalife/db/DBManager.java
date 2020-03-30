package com.datalife.datalife.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.datalife.datalife.dao.DaoMaster;
import com.datalife.datalife.dao.DaoSession;
import com.datalife.datalife.dao.Spo2hDao;
import com.datalife.datalife.dao.Spo2hDaoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by LG on 2018/2/10.
 */

public class DBManager {
    private final static String dbName = "health_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     * @param spo2hDao
     */
    public void insertUser(Spo2hDao spo2hDao) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        userDao.insert(spo2hDao);
    }

    /**
     * 插入用户集合
     *
     * @param spo2hDao
     */
    public void insertUserList(List<Spo2hDao> spo2hDao) {
        if (spo2hDao == null || spo2hDao.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        userDao.insertInTx(spo2hDao);
    }

    /**
     * 删除一条记录
     */
    public void deleteUser(Spo2hDao spo2hDao) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        userDao.delete(spo2hDao);
    }

    /**
     * 更新一条记录
     *
     * @param spo2hDao
     */
    public void updateUser(Spo2hDao spo2hDao) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        userDao.update(spo2hDao);
    }


    /**
     * 查询用户列表
     */
    public List<Spo2hDao> queryUserList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        QueryBuilder<Spo2hDao> qb = userDao.queryBuilder();
        List<Spo2hDao> list = qb.list();
        return list;
    }

    /**
     * 查询用户列表
     */
    public List<Spo2hDao> queryUserList(int age) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        QueryBuilder<Spo2hDao> qb = userDao.queryBuilder();
        qb.where(Spo2hDaoDao.Properties.Name.gt(age)).orderAsc(Spo2hDaoDao.Properties.Name);
        List<Spo2hDao> list = qb.list();
        return list;
    }


}