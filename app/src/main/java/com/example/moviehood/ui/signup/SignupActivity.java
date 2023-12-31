package com.example.moviehood.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviehood.R;
import com.example.moviehood.ui.signin.SigninActivity;
import com.example.moviehood.utils.ProgressDialogHelper;

public class SignupActivity extends AppCompatActivity {

    private EditText inputNama;
    private EditText inputEmail;
    private EditText inputPassword;
    private SignupViewModel signupViewModel;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        Button loginBtn = findViewById(R.id.login_btn);
        Button submitBtn = findViewById(R.id.submit_btn);

        progressDialogHelper = new ProgressDialogHelper();

        loginBtn.setOnClickListener(view -> navigateToSignin());

        inputNama = findViewById(R.id.input_nama);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.password);

        submitBtn.setOnClickListener(v -> registerUser());

        observeViewModel();
    }

    private void navigateToSignin() {
        startActivity(new Intent(SignupActivity.this, SigninActivity.class));
    }

    private void registerUser() {
        String nama = inputNama.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showToast("Harap isi semua input terlebih dahulu");
        } else {
            showProgressDialog();
            signupViewModel.register(nama, email, password);
        }
    }

    private void observeViewModel() {
        signupViewModel.getErrorMessage().observe(this, errorMessage -> {
            hideProgressDialog();
            showToast(errorMessage);
        });

        signupViewModel.getIsRegisterSuccessful().observe(this, isRegisterSuccessful -> {
            hideProgressDialog();
            if (isRegisterSuccessful) {
                onRegisterSuccess();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog() {
        progressDialogHelper.showProgressDialog(this);
    }

    private void hideProgressDialog() {
        progressDialogHelper.hideProgressDialog();
    }

    private void onRegisterSuccess() {
        showToast("Registrasi berhasil!");
        navigateToSignin();
        finish();
    }
}
