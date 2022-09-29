package com.practica.ismael.foodcal.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.User;
import com.practica.ismael.foodcal.data.remote.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class RegisterFragmentViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<Event<User>> loginLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> errorTrigger = new MutableLiveData<>();

    RegisterFragmentViewModel(Api api) {
        this.api = api;
    }

    void register(User user) {
        Call<User> response = api.register(user);

        //noinspection NullableProblems
        response.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setLoginLiveData(response.body());
                } else {
                    setErrorTrigger("This email is already in use");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                setErrorTrigger("This email is already in use");
            }
        });
    }

    MutableLiveData<Event<User>> getLoginLiveData() {
        return loginLiveData;
    }

    private void setLoginLiveData(User user) {
        this.loginLiveData.setValue(new Event<>(user));
    }

    MutableLiveData<Event<String>> getErrorTrigger() {
        return errorTrigger;
    }

    private void setErrorTrigger(String error) {
        this.errorTrigger.setValue(new Event<>(error));
    }
}
