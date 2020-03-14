package com.example.glumcinovi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.glumcinovi.Adapteri.MyAdapter2;
import com.example.glumcinovi.model.DataBaseHelper;
import com.example.glumcinovi.model.FavoriteFIlmovi;
import com.example.glumcinovi.model.Glumac;
import com.example.glumcinovi.net.Example;
import com.example.glumcinovi.net.MyService;
import com.example.glumcinovi.net.Search;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaFilmova extends AppCompatActivity implements MyAdapter2.OnItemClickListener {

    private RecyclerView recyclerView;
    private MyAdapter2 adapter;
    private RecyclerView.LayoutManager layoutManager;
    Button btnSearch;
    EditText movieName;
    int position = 0;
    private Glumac glumac;
    private Toolbar toolbar;
    private AlertDialog dijalog;
//    Button btnSearch1;
//    EditText movieName1;
//    RecyclerView rvList1;


    private DataBaseHelper databaseHelper;

    public static String KEY = "KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_filmova);


        setupToolbar();

        int position = getIntent().getExtras().getInt("position", 0);

        btnSearch = findViewById(R.id.btn_search);
        movieName = findViewById(R.id.ime_filma);
        recyclerView = findViewById(R.id.rvLista);





        try {
            glumac = getDataBaseHelper().getmGlumacDao().queryForId(position);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovieByName(movieName.getText().toString());
            }
        });
    }

    private void getMovieByName(String name) {
        Map<String, String> query = new HashMap<>();
        query.put("apikey", "fd705d15");
        query.put("s", name.trim());

        Call<Example> call = MyService.apiInterface().getMovieByName(query);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                if (response.code() == 200) {
                    try {
                        Example searches = response.body();

                        ArrayList<Search> search = new ArrayList<>();

                        for (Search e : searches.getSearch()) {

                            if (e.getType().equals("movie") || e.getType().equals("series")) {
                                search.add(e);
                            }
                        }

                        layoutManager = new LinearLayoutManager(ListaFilmova.this);
                        recyclerView.setLayoutManager(layoutManager);

                        adapter = new MyAdapter2(ListaFilmova.this, search, ListaFilmova.this);
                        recyclerView.setAdapter(adapter);

                        Toast.makeText(ListaFilmova.this, "Prikaz filmova/serija.", Toast.LENGTH_SHORT).show();

                    } catch (NullPointerException e) {
                        Toast.makeText(ListaFilmova.this, "Ne postoji film/serija sa tim nazivom", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(ListaFilmova.this, "Greska sa serverom", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(ListaFilmova.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Search movie = adapter.get(position);
        FavoriteFIlmovi favoriteFIlmovi = new FavoriteFIlmovi();
        favoriteFIlmovi.setmNaziv(movie.getTitle());
        favoriteFIlmovi.setmImdbId(movie.getImdbID());
        favoriteFIlmovi.setmGodine(movie.getYear());
        favoriteFIlmovi.setmGlumac(glumac);

        try {
            getDataBaseHelper().getmFilmDao().create(favoriteFIlmovi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), " \"" + movie.getTitle() + "\"" + " je dodat u listu", Toast.LENGTH_LONG).show();

    }

    public DataBaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return databaseHelper;
    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_lista_filmova);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.favorites:
//                addFilm();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }






    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("position", position);

    }
}

