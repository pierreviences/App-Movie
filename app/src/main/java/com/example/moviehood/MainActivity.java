package com.example.moviehood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private List<ModelFilm> listFilm;
    private RecyclerView recyclerView;
    private AdapterFilm adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mendapatkan referensi ke SearchView
        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.searchView);

// Menambahkan listener untuk mengamati perubahan pada SearchView
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Menangani peristiwa ketika pengguna menekan tombol cari
                // Mengirim query ke CariActivity
                Intent intent = new Intent(MainActivity.this, CariFilmActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Menangani peristiwa perubahan teks dalam SearchView
                // Jika Anda ingin mengambil tindakan saat teks berubah, Anda dapat menambahkan logika di sini
                return false;
            }
        });


        ImageView kelist = findViewById(R.id.kelist);
        ImageView keprofil = findViewById(R.id.keprofil);

        kelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MylistActivity.class);
                startActivity(intent);
            }
        });

        keprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        listFilm = new ArrayList<ModelFilm>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AdapterFilm(listFilm);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getData();
    }

    private void getData() {
        String endpoint = "https://us-east-1.aws.data.mongodb-api.com/app/projekmovie-szhmv/endpoint/film";
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            if (success) {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    ModelFilm modelKamar = new ModelFilm();
                                    modelKamar.setId(obj.getString("_id"));
                                    modelKamar.setJudul(obj.getString("judul"));
                                    modelKamar.setRating(obj.getInt("rating"));
                                    modelKamar.setTahun(obj.getInt("tahun"));
                                    modelKamar.setGenre(obj.getString("genre"));
                                    modelKamar.setNegara(obj.getString("negara"));
                                    modelKamar.setDeskripsi(obj.getString("deskripsi"));
                                    modelKamar.setGambar(obj.getString("gambar"));
                                    listFilm.add(modelKamar);

                                }


                            }
                            progressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    public class AdapterFilm extends RecyclerView.Adapter<AdapterFilm.ViewHolder> {

        private List<ModelFilm> listFilm;

        public AdapterFilm(List<ModelFilm> listFilm) {
            this.listFilm = listFilm;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_favorit, parent, false);
            AdapterFilm.ViewHolder viewHolder = new AdapterFilm.ViewHolder(view);
            viewHolder.itemView.setOnClickListener(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterFilm.ViewHolder holder, int position) {
            ModelFilm modelKamar = listFilm.get(position);
            holder.judul.setText(modelKamar.getJudul());
            holder.tahun.setText(String.valueOf(modelKamar.getTahun()));
            Picasso.get().load(modelKamar.getGambar()).fit().centerCrop().into(holder.gambar);

        }


        @Override
        public int getItemCount() {
            return (listFilm != null) ? listFilm.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
            private TextView judul, tahun;
            private ImageView gambar;

            public ViewHolder(View itemView) {
                super(itemView);
                judul = itemView.findViewById(R.id.judul);
                tahun = itemView.findViewById(R.id.tahun);
                gambar = itemView.findViewById(R.id.gambar);
                itemView.setFocusable(true);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                ModelFilm modelFilm = listFilm.get(position);
                String id_film = String.valueOf(modelFilm.getId());
                Intent intent = new Intent(view.getContext(), DetailFilmActivity.class);
                intent.putExtra("id_film", id_film);
                view.getContext().startActivity(intent);
                }
            }



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}
