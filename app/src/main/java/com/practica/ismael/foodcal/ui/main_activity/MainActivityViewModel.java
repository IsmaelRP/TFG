package com.practica.ismael.foodcal.ui.main_activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Calendar;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.model.User;
import com.practica.ismael.foodcal.data.remote.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<Event<Food>> foodSelected = new MutableLiveData<>();
    private final MutableLiveData<Event<Food>> foodTableSelected = new MutableLiveData<>();
    private final MutableLiveData<Event<Calendar>> calendarSelected = new MutableLiveData<>();

    MainActivityViewModel(Api api) {
        this.api = api;
    }

    private final MutableLiveData<String> idUserToken = new MutableLiveData<>();

    private final MutableLiveData<String> idName = new MutableLiveData<>();

    public MutableLiveData<String> getIdUserToken() {
        return idUserToken;
    }

    void setIdUserToken(String idUserToken) {
        this.idUserToken.setValue(idUserToken);
    }

    MutableLiveData<String> getIdName() {
        return idName;
    }

    private void setIdName(String idName) {
        this.idName.setValue(idName);
    }

    void getName(String userId) {
        Call<User> response = api.getName(userId);

        //noinspection NullableProblems
        response.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setIdName(response.body().getNombreUsuario());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof Exception){
                }
            }
        });
    }

    public MutableLiveData<Event<Food>> getFoodSelected() {
        return foodSelected;
    }

    public void setFoodSelected(Food foodSelected) {
        this.foodSelected.setValue(new Event<>(foodSelected));
    }

    public MutableLiveData<Event<Calendar>> getCalendarSelected() {
        return calendarSelected;
    }

    public void setCalendarSelected(Calendar calendarSelected) {
        this.calendarSelected.setValue(new Event<>(calendarSelected));
    }

    public MutableLiveData<Event<Food>> getFoodTableSelected() {
        return foodTableSelected;
    }

    public void setFoodTableSelected(Food foodTableSelected) {
        this.foodTableSelected.setValue(new Event<>(foodTableSelected));
    }
}
