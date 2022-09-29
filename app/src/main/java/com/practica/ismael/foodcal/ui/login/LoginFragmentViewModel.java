package com.practica.ismael.foodcal.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.User;
import com.practica.ismael.foodcal.data.remote.Api;

import java.io.EOFException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LoginFragmentViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<Event<User>> userResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> errorTrigger = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> eyeVisibility = new MutableLiveData<>(true);

    LoginFragmentViewModel(Api api){
        this.api = api;
    }

    void login(User user) {
        loading.setValue(true);
        Call<User> response = api.login(user);

        //noinspection NullableProblems
        response.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setUserResponseLiveData(response.body());

                } else {
                    errorTrigger.setValue(new Event<>("Wrong credentials"));    //Wrong access
                }
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loading.setValue(false);
                if (t instanceof EOFException){
                    errorTrigger.setValue(new Event<>("Wrong credentials"));
                }else if (t instanceof SocketTimeoutException){
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                }else{
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
    }

    MutableLiveData<Event<User>> getUserResponseLiveData() {
        return userResponseLiveData;
    }

    private void setUserResponseLiveData(User response) {
        this.userResponseLiveData.setValue(new Event<>(response));
    }

    MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    MutableLiveData<Event<String>> getErrorTrigger() {
        return errorTrigger;
    }

    public MutableLiveData<Boolean> getEyeVisibility() {
        return eyeVisibility;
    }

    public void setEyeVisibility(boolean bool){
        this.eyeVisibility.setValue(bool);
    }
}
