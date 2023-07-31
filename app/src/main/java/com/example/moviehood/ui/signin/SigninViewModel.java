package com.example.moviehood.ui.signin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviehood.data.model.User;
import com.example.moviehood.data.repository.SignInRepository;

public class SigninViewModel extends ViewModel {
    private MutableLiveData<User> userLiveData;
    private SignInRepository userRepository;

    public SigninViewModel() {
        userRepository = new SignInRepository();
    }

    public LiveData<User> getUserLiveData() {
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<>();
        }
        return userLiveData;
    }

    public void login(String email, String password) {
        userRepository.login(email, password, new SignInRepository.UserLoginCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String errorMessage) {
                userLiveData.setValue(null);
            }
        });
    }
}
