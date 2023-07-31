package com.example.moviehood.ui.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviehood.data.repository.SignUpRepository;

public class SignupViewModel extends ViewModel {
    private final SignUpRepository signUpRepository;

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRegisterSuccessful = new MutableLiveData<>();

    public SignupViewModel() {
        signUpRepository = new SignUpRepository();
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsRegisterSuccessful() {
        return isRegisterSuccessful;
    }

    public void register(String nama, String email, String password) {
        if (validateInput(nama, email, password)) {
            signUpRepository.register(nama, email, password, new SignUpRepository.RegisterCallback() {
                @Override
                public void onSuccess() {
                    isRegisterSuccessful.setValue(true);
                }

                @Override
                public void onError(String error) {
                    errorMessage.setValue(error);
                }
            });
        }
    }

    private boolean validateInput(String nama, String email, String password) {
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Harap isi semua input terlebih dahulu");
            return false;
        }
        return true;
    }
}
