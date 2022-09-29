package com.practica.ismael.foodcal.ui.login_activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.User;

public class LoginActivityViewModel extends ViewModel {

    private final MutableLiveData<String> idUserToken = new MutableLiveData<>();

    final MutableLiveData<Event<User>> userRegisterLiveData = new MutableLiveData<>();

    public MutableLiveData<String> getIdUserToken() {
        return idUserToken;
    }

    public void setIdUserToken(String idUserToken) {
        this.idUserToken.setValue(idUserToken);
    }

    public MutableLiveData<Event<User>> getUserRegisterLiveData() {
        return userRegisterLiveData;
    }

    public void setUserRegisterLiveData(User user) {
        this.userRegisterLiveData.setValue(new Event<>(user));
    }
}
