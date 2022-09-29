package com.practica.ismael.foodcal.ui.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.practica.ismael.foodcal.data.remote.Api;

public class RegisterFragmentViewModelFactory implements ViewModelProvider.Factory {

    private final Api api;

    public RegisterFragmentViewModelFactory(@NonNull Api api) {
        this.api = api;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterFragmentViewModel.class)) {
            return (T) new RegisterFragmentViewModel(api);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
