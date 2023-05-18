package project.tfg.ecgscan.ui.mainActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import project.tfg.ecgscan.ui.secondActivity.SecondActivityViewModel;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    public MainActivityViewModelFactory() {
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel();
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }

}