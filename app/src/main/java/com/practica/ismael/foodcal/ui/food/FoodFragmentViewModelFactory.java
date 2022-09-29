package com.practica.ismael.foodcal.ui.food;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.practica.ismael.foodcal.data.remote.Api;

public class FoodFragmentViewModelFactory implements ViewModelProvider.Factory{

    private final Api api;

    public FoodFragmentViewModelFactory(@NonNull Api api) {
        this.api = api;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FoodFragmentViewModel.class)) {
            return (T) new FoodFragmentViewModel(api);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
