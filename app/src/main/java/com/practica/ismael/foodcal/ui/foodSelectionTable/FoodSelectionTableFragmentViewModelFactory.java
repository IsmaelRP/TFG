package com.practica.ismael.foodcal.ui.foodSelectionTable;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.practica.ismael.foodcal.data.remote.Api;

public class FoodSelectionTableFragmentViewModelFactory implements ViewModelProvider.Factory{

    private final Api api;

    public FoodSelectionTableFragmentViewModelFactory(@NonNull Api api) {
        this.api = api;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FoodSelectionTableFragmentViewModel.class)) {
            return (T) new FoodSelectionTableFragmentViewModel(api);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
