package com.example.moviehood.ui.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviehood.MainActivity;
import com.example.moviehood.R;
import com.example.moviehood.data.model.User;
import com.example.moviehood.ui.signup.SignupActivity;
import com.example.moviehood.utils.ProgressDialogHelper;

public class SigninActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private SigninViewModel loginViewModel;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(SigninViewModel.class);

        TextView kesignup = findViewById(R.id.kesignup);
        Button kemain = findViewById(R.id.kemain);
        progressDialogHelper = new ProgressDialogHelper();

        kesignup.setOnClickListener(view -> navigateToSignup());

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.password);

        kemain.setOnClickListener(view -> loginUser());

        loginViewModel.getUserLiveData().observe(this, user -> {
            progressDialogHelper.hideProgressDialog();
            if (user != null) {
                saveUserDataToSharedPreferences(user);
                showToast("Login successful");
                navigateToMainActivity();
                finish();
            } else {
                showToast("Login failed");
            }
        });
    }

    private void navigateToSignup() {
        startActivity(new Intent(SigninActivity.this, SignupActivity.class));
    }

    private void loginUser() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showToast("Harap isi semua input terlebih dahulu");
        } else {
            progressDialogHelper.showProgressDialog(this);
            loginViewModel.login(email, password);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveUserDataToSharedPreferences(User user) {
        SharedPreferences.Editor setData = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
        setData.putString("id_user", user.getIdUser());
        setData.putString("nama", user.getNama());
        setData.putString("email", user.getEmail());
        setData.apply();
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(SigninActivity.this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialogHelper.hideProgressDialog();
    }
}
