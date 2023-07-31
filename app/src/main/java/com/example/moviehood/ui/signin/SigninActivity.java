package com.example.moviehood.ui.signin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviehood.MainActivity;
import com.example.moviehood.R;
import com.example.moviehood.data.model.User;
import com.example.moviehood.ui.signup.SignupActivity;
import com.example.moviehood.utils.ProgressDialogHelper;

public class SigninActivity extends AppCompatActivity {
    private EditText input_email;
    private EditText input_password;
    private SigninViewModel loginViewModel;

    private ProgressDialogHelper progressDialogHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(SigninViewModel.class);

        TextView kesignup = findViewById(R.id.kesignup);
        Button kemain = findViewById(com.example.moviehood.R.id.kemain);
        progressDialogHelper = new ProgressDialogHelper();
        kesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.password);

        kemain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SigninActivity.this, "Harap isi semua input terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialogHelper.showProgressDialog(SigninActivity.this);
                    loginViewModel.login(email, password);
                }
            }
        });

        loginViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                progressDialogHelper.hideProgressDialog();
                if (user != null) {
                   saveUserDataToSharedPreferences(user);
                    Toast.makeText(SigninActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SigninActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserDataToSharedPreferences(User user) {
         SharedPreferences.Editor setData = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
        setData.putString("id_user", user.getIdUser());
         setData.putString("nama", user.getNama());
         setData.putString("email", user.getEmail());
         setData.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialogHelper.hideProgressDialog();

    }


}
