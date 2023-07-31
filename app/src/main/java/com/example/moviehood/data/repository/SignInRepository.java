package com.example.moviehood.data.repository;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviehood.data.model.User;
import com.example.moviehood.utils.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInRepository {
    private static final String BASE_URL = "https://us-east-1.aws.data.mongodb-api.com/app/projekmovie-szhmv/endpoint/user/";
    private static final String LOGIN_ENDPOINT = "login";

    public interface UserLoginCallback {
        void onSuccess(User user);

        void onError(String errorMessage);
    }

    public void login(String email, String password, UserLoginCallback callback) {
        String url = BASE_URL + LOGIN_ENDPOINT;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int status = jsonResponse.getInt("status");
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            JSONObject userJsonObject = jsonResponse.getJSONObject("data");

                            User user = new User();
                            user.setIdUser(jsonResponse.getString("id_user"));
                            user.setNama(userJsonObject.getString("nama"));
                            user.setEmail(userJsonObject.getString("email"));

                            callback.onSuccess(user);
                        } else {
                            callback.onError(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("An error occurred");
                    }
                },
                error -> {
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        String errorMessage = new String(networkResponse.data);
                        callback.onError(errorMessage);
                    } else {
                        callback.onError("An error occurred");
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                JSONObject jsonObject = new JSONObject();
                try {
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
}
