package com.example.moviehood.ui.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignupViewModel.class)) {
            return (T) new SignupViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
