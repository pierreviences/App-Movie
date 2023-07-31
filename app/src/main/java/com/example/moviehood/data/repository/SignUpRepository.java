package com.example.moviehood.data.repository;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviehood.utils.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpRepository {
    private static final String BASE_URL = "https://us-east-1.aws.data.mongodb-api.com/app/projekmovie-szhmv/endpoint/user/";

    public void register(String nama, String email, String password, RegisterCallback callback) {
        String url = BASE_URL + "register";
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
                                callback.onSuccess();
                            } else {
                                callback.onError(message);
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
                            callback.onError(errorMessage);
                        } else {
                            callback.onError(error.getMessage());
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

        RequestQueue requestQueue = Volley.newRequestQueue(MyApp.getAppContext());
        requestQueue.add(stringRequest);
    }

    public interface RegisterCallback {
        void onSuccess();
        void onError(String error);
    }
}
