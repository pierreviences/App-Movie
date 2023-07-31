package com.example.moviehood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {
    private EditText input_nama;
    private EditText input_email;
    private EditText input_password;
    private EditText confirm_password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button login_btn = findViewById(R.id.login_btn);
        Button submit_btn = findViewById(R.id.submit_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        input_nama = findViewById(R.id.input_nama);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.password);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String nama = input_nama.getText().toString();
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();

                // Memeriksa apakah inputan yang diperlukan sudah diisi atau tidak

                if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SigninActivity.this, "Harap isi semua input terlebih dahulu", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                } else {
                    register(nama,  email, password);
                }
            }
        });
    }

    // Fungsi untuk menampilkan ProgressDialog
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(SigninActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

    // Fungsi untuk menyembunyikan ProgressDialog
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void executeRequest(String nama, String email, String password ) {
        showProgressDialog(); // Menampilkan ProgressDialog sebelum request Volley dimulai
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/projekmovie-szhmv/endpoint/user/register";
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
                                Toast.makeText(SigninActivity.this, message, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish(); // Tutup RegisterActivity agar tidak bisa kembali dengan tombol back
                                hideProgressDialog(); // Pindahkan pemanggilan ke sini
                            }else {
                                hideProgressDialog();
                                Toast.makeText(SigninActivity.this, message, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(SigninActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        } else {
                            Log.e("error", "onErrorResponse: " + error.getMessage(), error);
                            Toast.makeText(SigninActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nama", nama);
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);
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

        RequestQueue requestQueue = Volley.newRequestQueue(SigninActivity.this);
        requestQueue.add(stringRequest);
    }


    private void register(String nama, String email,  String password ) {

        executeRequest(nama, email, password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}