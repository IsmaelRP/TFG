package com.practica.ismael.foodcal.ui.calendarTable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Contains;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.remote.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class CalendarTableFragmentViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<List<Contains>> listContains = new MutableLiveData<>();
    private final MutableLiveData<Event<List<Food>>> foodLiveData = new MutableLiveData<>();

    CalendarTableFragmentViewModel(Api api) {
        this.api = api;
    }

    void getContains(String idusuario, int idcalendario) {
        Call<List<Contains>> response = api.getContains(idusuario, idcalendario);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Contains>>() {
            @Override
            public void onResponse(Call<List<Contains>> call, Response<List<Contains>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setListContains(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Contains>> call, Throwable t) {
            }
        });
    }

    void getFood(int idcomida1, int idomida2) {
        Call<List<Food>> response = api.getFood(idcomida1, idomida2);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0) != null || response.body().get(1) != null){
                        setFoodLiveData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
            }
        });
    }

    void deleteContains(String idusuario, int idcalendario, String fecha) {
        Call<List<Contains>> response = api.deleteContainsDay(idusuario, idcalendario, fecha);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Contains>>() {
            @Override
            public void onResponse(Call<List<Contains>> call, Response<List<Contains>> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<List<Contains>> call, Throwable t) {
            }
        });
    }

    MutableLiveData<List<Contains>> getListContains() {
        return listContains;
    }

    private void setListContains(List<Contains> listContains) {
        this.listContains.setValue(listContains);
    }

    MutableLiveData<Event<List<Food>>> getFoodLiveData() {
        return foodLiveData;
    }

    private void setFoodLiveData(List<Food> foodLiveData) {
        this.foodLiveData.setValue(new Event<>(foodLiveData));
    }
}
