package project.tfg.ecgscan.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import project.tfg.ecgscan.data.Repository;

public class HomeFragmentViewModelFactory implements ViewModelProvider.Factory {


    private final Repository repository;

    public HomeFragmentViewModelFactory(@NonNull Application application, @NonNull Repository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeFragmentViewmodel.class)) {
            return (T) new HomeFragmentViewmodel(repository);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
