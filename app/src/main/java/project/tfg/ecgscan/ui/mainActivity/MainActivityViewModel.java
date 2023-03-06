package project.tfg.ecgscan.ui.mainActivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.tfg.ecgscan.data.Event;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Event<Boolean>> phoneLogin = new MutableLiveData<>(new Event<>(false));

    public MutableLiveData<Event<Boolean>> getPhoneLogin() {
        return phoneLogin;
    }

    public void setPhoneLogin(boolean b) {
        phoneLogin.postValue(new Event<>(b));
    }
}
