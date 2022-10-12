package project.tfg.ecgscan.ui.secondActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SecondActivityViewModelFactory implements ViewModelProvider.Factory  {

    public SecondActivityViewModelFactory() {
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SecondActivityViewModel.class)) {
            return (T) new SecondActivityViewModel();
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
