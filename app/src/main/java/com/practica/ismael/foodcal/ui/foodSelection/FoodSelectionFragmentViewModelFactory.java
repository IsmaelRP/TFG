package com.practica.ismael.foodcal.ui.foodSelection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.practica.ismael.foodcal.data.remote.Api;

public class FoodSelectionFragmentViewModelFactory implements ViewModelProvider.Factory{

    private final Api api;

    public FoodSelectionFragmentViewModelFactory(@NonNull Api api) {
        this.api = api;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FoodSelectionFragmentViewModel.class)) {
            return (T) new FoodSelectionFragmentViewModel(api);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
