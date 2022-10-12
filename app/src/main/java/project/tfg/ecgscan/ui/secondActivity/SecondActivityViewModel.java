package project.tfg.ecgscan.ui.secondActivity;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.tfg.ecgscan.data.Event;

public class SecondActivityViewModel extends ViewModel {

    private final MutableLiveData<Event<Boolean>> navigateToList = new MutableLiveData<>();

    public MutableLiveData<Event<Boolean>> getNavigateToList() {
        return navigateToList;
    }

    void setNavigateToList(Boolean b) {
        this.navigateToList.setValue(new Event<>(b));
    }

}
