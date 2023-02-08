package project.tfg.ecgscan.ui.secondActivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.tfg.ecgscan.data.Event;

public class SecondActivityViewModel extends ViewModel {

    private final MutableLiveData<Event<Boolean>> navigateToList = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> preferencesCloud = new MutableLiveData<>();

    public MutableLiveData<Event<Boolean>> getNavigateToList() {
        return navigateToList;
    }

    void setNavigateToList(Boolean b) {
        this.navigateToList.setValue(new Event<>(b));
    }

    public MutableLiveData<Event<Boolean>> getPreferencesCloud() {
        return preferencesCloud;
    }

    public void setPreferencesCloud(Boolean b) {
        this.preferencesCloud.setValue(new Event<>(b));
    }
}
