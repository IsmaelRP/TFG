package project.tfg.ecgscan.ui.list.local;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import project.tfg.ecgscan.data.Repository;

public class LocalListFragmentViewModelFactory implements ViewModelProvider.Factory {


    private final Application application;
    private final Repository repository;

    public LocalListFragmentViewModelFactory(@NonNull Application application, @NonNull Repository repository) {
        this.application = application;
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LocalListFragmentViewModel.class)) {
            return (T) new LocalListFragmentViewModel(application, repository);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }

}
