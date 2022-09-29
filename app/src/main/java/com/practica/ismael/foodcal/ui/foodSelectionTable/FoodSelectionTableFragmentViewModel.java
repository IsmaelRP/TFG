package com.practica.ismael.foodcal.ui.foodSelectionTable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.model.Contains;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.remote.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodSelectionTableFragmentViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<List<Food>> foodListLiveData = new MutableLiveData<>();

    FoodSelectionTableFragmentViewModel(Api api) {
        this.api = api;
    }

    void getFoods(String idUsuario) {
        Call<List<Food>> response = api.getFoods(idUsuario);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setFoodListLiveData(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {

            }
        });
    }

    public void insertData(Contains contains) {
        Call<Contains> response = api.addContains(contains.getIdcalendario(),
                contains.getIdusuariocalendario(), contains.getFecha(), contains.getIdcomidaprincipal(),
                contains.getTipocomida());

        //noinspection NullableProblems
        response.enqueue(new Callback<Contains>() {
            @Override
            public void onResponse(Call<Contains> call, Response<Contains> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Contains> call, Throwable t) {
            }
        });
    }

    MutableLiveData<List<Food>> getFoodListLiveData() {
        return foodListLiveData;
    }

    private void setFoodListLiveData(List<Food> foodListLiveData) {
        this.foodListLiveData.setValue(foodListLiveData);
    }

}
