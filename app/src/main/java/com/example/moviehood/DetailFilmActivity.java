package com.example.moviehood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DetailFilmActivity extends AppCompatActivity {
    private ImageView imagePoster;
    private TextView textJudul;
    private TextView textRating;
    private SharedPreferences sharedPreferences;
    private TextView textDurasi;
    private TextView textTahun;
    private TextView textGenre;
    private TextView textNegara;
    private TextView textDeskripsi;
    ImageView button_back;
    private Button bookmark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        bookmark = findViewById(R.id.bookmark);
        imagePoster = findViewById(R.id.imagePoster);
        textJudul = findViewById(R.id.textJudul);
        textRating = findViewById(R.id.textRating);
        textDurasi = findViewById(R.id.textDurasi);
        textTahun = findViewById(R.id.textTahun);
        textGenre = findViewById(R.id.textGenre);
        textNegara = findViewById(R.id.textNegara);
        textDeskripsi = findViewById(R.id.textDeskripsi);

        String endpoint = "https://us-east-1.aws.data.mongodb-api.com/app/projekmovie-szhmv/endpoint/detailFilm";
        Uri.Builder builder = Uri.parse(endpoint).buildUpon();
        Intent intent = getIntent();
        String nomorKamar = intent.getStringExtra("id_film");
        builder.appendQueryParameter("id_film", nomorKamar);
        String url = builder.build().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                            try {
                                String judul = response.getString("judul");
                                int rating = response.getInt("rating");
                                String durasi = response.getString("durasi");
                                int tahun = response.getInt("tahun");
                                String genre = response.getString("genre");
                                String negara = response.getString("negara");
                                String deskripsi = response.getString("deskripsi");
                                String gambar = response.getString("gambar");

                                textJudul.setText(judul);
                                textRating.setText("Rating: " + rating);
                                textDurasi.setText("Durasi: " + durasi);
                                textTahun.setText("Tahun: " + tahun);
                                textGenre.setText("Genre: " + genre);
                                textNegara.setText("Negara: " + negara);
                                textDeskripsi.setText(deskripsi);

                                Picasso.get().load(gambar).into(imagePoster);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailFilmActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);


        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noKamar = intent.getStringExtra("id_film");
                wishlist(noKamar);
            }
        });


    }
    private void executeRequest(String nomorKamar) {

        String url = "https://us-east-1.aws.data.mongodb-api.com/app/projekmovie-szhmv/endpoint/tambahSuka";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                Toast.makeText(DetailFilmActivity.this, message, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(DetailFilmActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String errorMessage = new String(networkResponse.data);
                            Log.e("error", errorMessage);
                            Toast.makeText(DetailFilmActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("error", "onErrorResponse: " + error.getMessage(), error);
                            Toast.makeText(DetailFilmActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                JSONObject jsonObject = new JSONObject();
                try {
                    sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

                    String id_user = sharedPreferences.getString("id_user", null);
                    jsonObject.put("id_film", nomorKamar);
                    jsonObject.put("id_user", id_user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DetailFilmActivity.this);
        requestQueue.add(stringRequest);
    }

    private void wishlist(String nomorKamar) {

        executeRequest(nomorKamar);
    }
}