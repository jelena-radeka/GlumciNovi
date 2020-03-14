package com.example.glumcinovi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.glumcinovi.Adapteri.MyAdapter2;
import com.example.glumcinovi.Adapteri.MyAdapter4;
import com.example.glumcinovi.model.DataBaseHelper;
import com.example.glumcinovi.model.FavoriteFIlmovi;
import com.example.glumcinovi.model.Filmovi;
import com.example.glumcinovi.model.Glumac;
import com.example.glumcinovi.net.Example;
import com.example.glumcinovi.net.MyService;
import com.example.glumcinovi.net.Search;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends AppCompatActivity implements MyAdapter4.OnItemClickListener {

    private RecyclerView recyclerView;
    private MyAdapter4 adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DataBaseHelper databaseHelper;

    private List<FavoriteFIlmovi> filmovi;
    private SharedPreferences prefs;




    public static String KEY = "KEY";


    @Override
    public void onItemClick(int position) {
//        Search movie = adapter.get(position);
//        FavoriteFIlmovi favoriteFIlmovi = new FavoriteFIlmovi();
//        favoriteFIlmovi.setmNaziv(movie.getTitle());
//        favoriteFIlmovi.setmImdbId(movie.getImdbID());
//        favoriteFIlmovi.setmGodine(movie.getYear());
//        favoriteFIlmovi.setmGlumac(glumac);
//
//        try {
//            getDataBaseHelper().getmFilmDao().create(favoriteFIlmovi);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Toast.makeText(getApplicationContext(), " \"" + movie.getTitle() + "\"" + " je dodat u listu", Toast.LENGTH_LONG).show();
//
    }

    public DataBaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        recyclerView = findViewById(R.id.rvList1);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        try {
            filmovi = getDataBaseHelper().getmFilmDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        adapter = new MyAdapter4(this, filmovi, this);
        recyclerView.setAdapter(adapter);
    }
}






