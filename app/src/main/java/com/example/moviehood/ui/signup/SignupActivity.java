package com.example.moviehood.ui.signup;

import android.app.ProgressDialog;
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
    private EditText input_nama;
    private EditText input_email;
    private EditText input_password;

    private SignupViewModel signinViewModel;

    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signinViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        Button login_btn = findViewById(R.id.login_btn);
        Button submit_btn = findViewById(R.id.submit_btn);
        progressDialogHelper = new ProgressDialogHelper();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        input_nama = findViewById(R.id.input_nama);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.password);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = input_nama.getText().toString();
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();
                if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Harap isi semua input terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialogHelper.showProgressDialog(SignupActivity.this);
                    signinViewModel.register(nama, email, password);
                }
            }
        });

        observeViewModel();
    }

    private void observeViewModel() {
        signinViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                progressDialogHelper.hideProgressDialog();
                Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        signinViewModel.getIsRegisterSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRegisterSuccessful) {
                progressDialogHelper.hideProgressDialog();
                if (isRegisterSuccessful) {
                    Toast.makeText(SignupActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }




}
