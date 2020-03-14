package com.example.glumcinovi.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "ormlite.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<Filmovi,Integer> mFilmoviDao = null;
    private Dao<Glumac, Integer> mGlumacDao = null;
    private Dao<FavoriteFIlmovi, Integer> mFilmDao = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Filmovi.class);
            TableUtils.createTable(connectionSource, Glumac.class);
            TableUtils.createTable(connectionSource, FavoriteFIlmovi.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {


        try {
            TableUtils.dropTable(connectionSource, Filmovi.class,true);
            TableUtils.dropTable(connectionSource, Glumac.class, true);
            TableUtils.dropTable(connectionSource, FavoriteFIlmovi.class, true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Filmovi, Integer> getMovieDao() throws SQLException {
        if (mFilmoviDao == null) {
            mFilmoviDao = getDao(Filmovi.class);
        }

        return mFilmoviDao;
    }
    public Dao<Glumac, Integer> getmGlumacDao() throws SQLException {
        if (mGlumacDao == null) {
            mGlumacDao = getDao(Glumac.class);
        }
        return mGlumacDao;
    }

    public Dao<FavoriteFIlmovi, Integer> getmFilmDao() throws SQLException {
        if (mFilmDao == null) {
            mFilmDao = getDao(FavoriteFIlmovi.class);
        }
        return mFilmDao;
    }

    @Override
    public void close() {
        mFilmoviDao = null;
        mGlumacDao = null;
//        mFilmDao = null;

        super.close();
    }
}
