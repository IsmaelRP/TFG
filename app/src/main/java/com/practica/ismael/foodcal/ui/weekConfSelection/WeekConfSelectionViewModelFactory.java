package com.practica.ismael.foodcal.ui.weekConfSelection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.practica.ismael.foodcal.data.remote.Api;

public class WeekConfSelectionViewModelFactory implements ViewModelProvider.Factory{

    private final Api api;

    public WeekConfSelectionViewModelFactory(@NonNull Api api) {
        this.api = api;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WeekConfSelectionViewModel.class)) {
            return (T) new WeekConfSelectionViewModel(api);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
