package com.example.glumcinovi.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = FavoriteFIlmovi.TABLE_NAME_USERS)

public class FavoriteFIlmovi {

    public static final String TABLE_NAME_USERS = "film";

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_NAZIV = "naziv";
    public static final String FIELD_NAME_IMDB_ID = "imdbID";
    public static final String FIELD_NAME_GODINE = "godine";
    public static final String FIELD_NAME_USERS = "glumac";


    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME_NAZIV)
    private String mNaziv;

    @DatabaseField(columnName = FIELD_NAME_IMDB_ID)
    private String mImdbId;

    @DatabaseField(columnName = FIELD_NAME_GODINE)
    private String mGodine;

    @DatabaseField(columnName = FIELD_NAME_USERS,foreign = true,foreignAutoRefresh = true)
    private Glumac mGlumac;


    public FavoriteFIlmovi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmNaziv() {
        return mNaziv;
    }

    public void setmNaziv(String mNaziv) {
        this.mNaziv = mNaziv;
    }

    public String getmImdbId() {
        return mImdbId;
    }

    public void setmImdbId(String mImdbId) {
        this.mImdbId = mImdbId;
    }

    public String getmGodine() {
        return mGodine;
    }

    public void setmGodine(String mGodine) {
        this.mGodine = mGodine;
    }

    public Glumac getmGlumac() {
        return mGlumac;
    }

    public void setmGlumac(Glumac mGlumac) {
        this.mGlumac = mGlumac;
    }

    @Override
    public String toString() {
        return mNaziv + " (" + mGodine + ") ";
    }
}
