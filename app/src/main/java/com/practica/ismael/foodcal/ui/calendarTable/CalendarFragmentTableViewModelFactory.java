package com.practica.ismael.foodcal.ui.calendarTable;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.practica.ismael.foodcal.data.remote.Api;

public class CalendarFragmentTableViewModelFactory implements ViewModelProvider.Factory {

    private final Api api;

    public CalendarFragmentTableViewModelFactory(@NonNull Api api) {
        this.api = api;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CalendarTableFragmentViewModel.class)) {
            return (T) new CalendarTableFragmentViewModel(api);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
