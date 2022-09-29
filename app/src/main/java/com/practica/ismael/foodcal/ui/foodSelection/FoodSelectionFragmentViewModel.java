package com.practica.ismael.foodcal.ui.foodSelection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.remote.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodSelectionFragmentViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<List<Food>> foodListLiveData = new MutableLiveData<>();

    FoodSelectionFragmentViewModel(Api api) {
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

    MutableLiveData<List<Food>> getFoodListLiveData() {
        return foodListLiveData;
    }

    private void setFoodListLiveData(List<Food> foodListLiveData) {
        this.foodListLiveData.setValue(foodListLiveData);
    }
}
