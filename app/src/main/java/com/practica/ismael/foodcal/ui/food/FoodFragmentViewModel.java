package com.practica.ismael.foodcal.ui.food;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.remote.Api;

import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class FoodFragmentViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<Event<Food>> foodLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> errorTrigger = new MutableLiveData<>();
    private final MutableLiveData<List<Food>> foodListLiveData = new MutableLiveData<>();

    private final MutableLiveData<Event<Food>> foodDeletedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<Food>> foodEditTrigger = new MutableLiveData<>();
    private final MutableLiveData<Event<Food>> foodConfirmDeletion = new MutableLiveData<>();

    FoodFragmentViewModel(Api api){
        this.api = api;
    }

    void addFood(Food food) {
        List<Food> list = new ArrayList<>(getFoodListLiveData().getValue());
        list.add(food);
        setFoodListLiveData(list);

        Call<Food> response = api.addFood(food);

        //noinspection NullableProblems
        response.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setFoodLiveData(response.body());

                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                if (t instanceof EOFException){
                    errorTrigger.setValue(new Event<>("Ops, something is wrong"));
                }else if (t instanceof SocketTimeoutException){
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                }else{
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
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
                if (t instanceof EOFException){
                    errorTrigger.setValue(new Event<>("Ops, something is wrong"));
                }else if (t instanceof SocketTimeoutException){
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                }else{
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
    }

    void editFood(Food food) {
        List<Food> list = new ArrayList<>(getFoodListLiveData().getValue());
        list.set(list.indexOf(food), food);
        setFoodListLiveData(list);

        Call<Food> response = api.editFood(food);

        //noinspection NullableProblems
        response.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                if (t instanceof EOFException){
                    errorTrigger.setValue(new Event<>("Ops, something is wrong"));
                }else if (t instanceof SocketTimeoutException){
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                }else{
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
    }

    void deleteFood(Food food) {
        Call<Food> response = api.deleteFood(food);

        //noinspection NullableProblems
        response.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Food> list = new ArrayList<>(getFoodListLiveData().getValue());
                    list.remove(food);
                    setFoodListLiveData(list);

                    setFoodConfirmDeletion(response.body());
                }else{
                    setFoodConfirmDeletion(null);
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                if (t instanceof EOFException){
                    errorTrigger.setValue(new Event<>("Ops, something is wrong"));
                }else if (t instanceof SocketTimeoutException){
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                }else{
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
    }

    MutableLiveData<Event<Food>> getFoodLiveData() {
        return foodLiveData;
    }

    private void setFoodLiveData(Food response) {
        this.foodLiveData.setValue(new Event<>(response));
    }

    MutableLiveData<Event<String>> getErrorTrigger() {
        return errorTrigger;
    }

    MutableLiveData<List<Food>> getFoodListLiveData() {
        return foodListLiveData;
    }

    private void setFoodListLiveData(List<Food> foodListLiveData) {
        this.foodListLiveData.setValue(foodListLiveData);
    }

    public MutableLiveData<Event<Food>> getFoodDeletedLiveData() {
        return foodDeletedLiveData;
    }

    public void setFoodDeletedLiveData(Food food) {
        this.foodDeletedLiveData.setValue(new Event<>(food));
    }

    public MutableLiveData<Event<Food>> getFoodEditTrigger() {
        return foodEditTrigger;
    }

    public void setFoodEditTrigger(Food food) {
        this.foodEditTrigger.setValue(new Event<>(food));
    }

    public MutableLiveData<Event<Food>> getFoodConfirmDeletion() {
        return foodConfirmDeletion;
    }

    public void setFoodConfirmDeletion(Food food){
        foodConfirmDeletion.setValue(new Event<>(food));
    }
}
