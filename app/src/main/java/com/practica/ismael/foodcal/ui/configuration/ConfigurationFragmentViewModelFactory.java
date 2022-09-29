package com.practica.ismael.foodcal.ui.configuration;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.practica.ismael.foodcal.data.remote.Api;

public class ConfigurationFragmentViewModelFactory implements ViewModelProvider.Factory{

    private final Api api;

    public ConfigurationFragmentViewModelFactory(@NonNull Api api) {
        this.api = api;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ConfigurationFragmentViewModel.class)) {
            return (T) new ConfigurationFragmentViewModel(api);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
