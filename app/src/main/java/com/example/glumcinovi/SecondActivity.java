package com.example.glumcinovi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glumcinovi.Adapteri.MyAdapter2;
import com.example.glumcinovi.Adapteri.MyAdapter3;
import com.example.glumcinovi.model.DataBaseHelper;
import com.example.glumcinovi.model.FavoriteFIlmovi;
import com.example.glumcinovi.model.Glumac;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements MyAdapter3.OnItemClickListener {
    private DataBaseHelper databaseHelper;
    private SharedPreferences prefs;
    private Glumac glumac;

    public static final String NOTIF_CHANNEL_ID = "notif_channel_007";
    public static String KEY = "KEY";

    private static final String TAG = "PERMISSIONS";
    private static final int SELECT_PICTURE = 1;
    private String imagePath = null;
    private ImageView preview;


    private TextView ime;
    private TextView prezime;
    private TextView godinaRodjenja;
    private TextView biografija;
    private RatingBar ocena;
    private ImageView image;
    private ImageView imageSlika;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        createNotificationChannel();
        setupToolbar();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int key = getIntent().getExtras().getInt(MainActivity.GLUMAC_KEY);


        try {
            glumac = getDataBaseHelper().getmGlumacDao().queryForId(key);

            ime = findViewById(R.id.second_ime);
            prezime = findViewById(R.id.second_prezime);
            godinaRodjenja = findViewById(R.id.second_godina);
            biografija = findViewById(R.id.second_biografija);
            ocena = findViewById(R.id.second_ocena);
            image = findViewById(R.id.imageSlika);

            Uri mUri = Uri.parse(glumac.getImage());
            image.setImageURI(mUri);

            ime.setText(glumac.getmIme());
            prezime.setText(glumac.getmPrezime());
            godinaRodjenja.setText(glumac.getmDatum());
            biografija.setText(glumac.getmBiografija());
            ocena.setRating(glumac.getmRating());

            InputStream is = null;
            try {
                is = getApplicationContext().getAssets().open(glumac.getImage());
                Drawable drawable = Drawable.createFromStream(is, null);
                image.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ListView listView = findViewById(R.id.second_listView);

        try {
            List<FavoriteFIlmovi> list = getDataBaseHelper().getmFilmDao().queryBuilder()
                    .where()
                    .eq(FavoriteFIlmovi.FIELD_NAME_USERS, glumac.getmId())
                    .query();

            final ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FavoriteFIlmovi m = (FavoriteFIlmovi) listView.getItemAtPosition(position);

                    Intent i = new Intent(SecondActivity.this, FilmDetails.class);
                    i.putExtra(KEY, m.getmImdbId());
                    i.putExtra("id", m.getId());
                    startActivity(i);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                editGlumac();
                break;
            case R.id.delete:
                deleteGlumac();
                refresh();
                break;
            case R.id.add_film:
                addListaFilmova();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteGlumac() {
        try {
            getDataBaseHelper().getmGlumacDao().delete(glumac);
            finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean toast = prefs.getBoolean(getString(R.string.toast_key), false);
        boolean notif = prefs.getBoolean(getString(R.string.notif_key), false);

        if (toast) {
            Toast.makeText(this, "Glumac obrisan", Toast.LENGTH_LONG).show();
        }

        if (notif) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIF_CHANNEL_ID);

            builder.setSmallIcon(android.R.drawable.ic_menu_delete);
            builder.setContentTitle("Glumci");
            builder.setContentText("Glumac obrisan");

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_games_black_24dp);

            builder.setLargeIcon(bitmap);
            notificationManager.notify(1, builder.build());
        }


    }

    private void reset() {
        imagePath = "";
        preview = null;
    }

    private void editGlumac() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_layout);
        dialog.setTitle("Unesite podatke");
        dialog.setCanceledOnTouchOutside(false);

        Button chooseBtn = (Button) dialog.findViewById(R.id.choose1);
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preview = dialog.findViewById(R.id.preview_image1);
                selectPicture();
            }
        });

        Button add = dialog.findViewById(R.id.add_glumac);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText glumacPrezime = dialog.findViewById(R.id.glumac_prezime);
                EditText glumacIme = dialog.findViewById(R.id.glumac_ime);
                EditText glumacBiografija = dialog.findViewById(R.id.glumac_biografija);
                EditText glumacDatum = dialog.findViewById(R.id.glumac_datum);
                EditText glumacRating = dialog.findViewById(R.id.glumac_rating);

                if (preview == null || imagePath == null) {
                    Toast.makeText(SecondActivity.this, "Slika mora biti izabrana", Toast.LENGTH_SHORT).show();
                    return;

                }

                glumac.setmIme(glumacIme.getText().toString());
                glumac.setmPrezime(glumacPrezime.getText().toString());
                glumac.setmDatum(glumacDatum.getText().toString());
                glumac.setmRating(Float.parseFloat(glumacRating.getText().toString()));
                glumac.setmBiografija(glumacBiografija.getText().toString());
                glumac.setImage(imagePath);

                try {
                    getDataBaseHelper().getmGlumacDao().update(glumac);


                    boolean toast = prefs.getBoolean(getString(R.string.toast_key), false);
                    boolean notif = prefs.getBoolean(getString(R.string.notif_key), false);

                    if (toast) {
                        Toast.makeText(SecondActivity.this, "Update-ovani podaci o glumcu", Toast.LENGTH_LONG).show();

                    }

                    if (notif) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(SecondActivity.this, NOTIF_CHANNEL_ID);

                        builder.setSmallIcon(android.R.drawable.ic_menu_edit);
                        builder.setContentTitle("Glumci");
                        builder.setContentText("Update-ovani podaci o glumcu");

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_games_black_24dp);

                        builder.setLargeIcon(bitmap);
                        notificationManager.notify(1, builder.build());

                    }
                    reset();
                    refreshGlumac();


                } catch (NumberFormatException e) {
                    Toast.makeText(SecondActivity.this, "Rating mora biti broj", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();


            }

        });

        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addListaFilmova() {
        Intent i = new Intent(SecondActivity.this, ListaFilmova.class);
        i.putExtra("position", glumac.getmId());
        startActivity(i);
    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.show();
        }
    }

    private void refreshGlumac() {
        int key = getIntent().getExtras().getInt(MainActivity.GLUMAC_KEY);

        try {
            glumac = getDataBaseHelper().getmGlumacDao().queryForId(key);

            ime = findViewById(R.id.second_ime);
            prezime = findViewById(R.id.second_prezime);
            godinaRodjenja = findViewById(R.id.second_godina);
            biografija = findViewById(R.id.second_biografija);
            ocena = findViewById(R.id.second_ocena);
            imageSlika = findViewById(R.id.imageSlika);

            ime.setText(glumac.getmIme());
            prezime.setText(glumac.getmPrezime());
            godinaRodjenja.setText(glumac.getmDatum());
            biografija.setText(glumac.getmBiografija());
            ocena.setRating(glumac.getmRating());

            Uri mUri = Uri.parse(glumac.getImage());
            imageSlika.setImageURI(mUri);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.second_listView);

        if (listview != null) {
            ArrayAdapter<FavoriteFIlmovi> adapter = (ArrayAdapter<FavoriteFIlmovi>) listview.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();
                    List<FavoriteFIlmovi> list = getDataBaseHelper().getmFilmDao().queryBuilder()
                            .where()
                            .eq(FavoriteFIlmovi.FIELD_NAME_USERS, glumac.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Description of My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

    }

    public DataBaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();

    }

    @Override
    public void onItemClick(int position) {

        Intent i = new Intent(SecondActivity.this, FilmDetails.class);
        try {
            i.putExtra(KEY, getDataBaseHelper().getmFilmDao().queryForAll().get(position).getmImdbId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);
                    cursor.close();

                    if (preview != null) {
                        preview.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    }
                }
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    private void
    selectPicture() {
        if (isStoragePermissionGranted()) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_PICTURE);
        }
    }
}


