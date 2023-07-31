package com.example.moviehood.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.moviehood.R;

public class ProgressDialogHelper {
    private ProgressDialog progressDialog;

    public void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
