package com.example.moviehood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import com.example.moviehood.ui.signin.SigninActivity;

public class ProfilActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageView kelist = findViewById(R.id.kelist);
        ImageView kehome = findViewById(R.id.kehome);

        kelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilActivity.this, MylistActivity.class);
                startActivity(intent);
            }
        });

        kehome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);


            String nama = sharedPreferences.getString("nama", null);
            String email = sharedPreferences.getString("email", null);



            TextView nama_tv = findViewById(R.id.input_nama);
            TextView email_tv = findViewById(R.id.input_email);
            nama_tv.setText(String.valueOf(nama));
            email_tv.setText(String.valueOf(email));
            Button logoutButton = findViewById(R.id.keluar);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
                builder.setTitle("Konfirmasi Logout")
                        .setMessage("Apakah Anda yakin ingin keluar?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Menghapus token dari SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("nama");
                                editor.remove("email");
                                editor.apply();

                                // Kembali ke halaman login
                                Intent intent = new Intent(ProfilActivity.this, SigninActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Batal", null)
                        .show();
            }
        });



    }
}