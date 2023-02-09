package project.tfg.ecgscan.ui.secondActivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import project.tfg.ecgscan.data.ElectroImage;
import project.tfg.ecgscan.data.Event;

public class SecondActivityViewModel extends ViewModel {

    private final MutableLiveData<Event<Boolean>> navigateToList = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> preferencesCloud = new MutableLiveData<>();

    private final MutableLiveData<Event<FirebaseStorage>> firebaseStorage = new MutableLiveData<>();
    private final MutableLiveData<Event<StorageReference>> storageReference = new MutableLiveData<>();

    private final MutableLiveData<Event<List<ElectroImage>>> listElectros = new MutableLiveData<>();


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


    public MutableLiveData<Event<FirebaseStorage>> getFirebaseStorage() {
        return firebaseStorage;
    }

    public void setFirebaseStorage(FirebaseStorage f) {
        this.firebaseStorage.setValue(new Event<>(f));
    }

    public MutableLiveData<Event<StorageReference>> getStorageReference() {
        return storageReference;
    }

    public void setStorageReference(StorageReference s) {
        this.storageReference.setValue(new Event<>(s));
    }



    public MutableLiveData<Event<List<ElectroImage>>> getListElectros() {
        return listElectros;
    }

    public void setListElectros(List<ElectroImage> l) {
        this.listElectros.setValue(new Event<>(l));
    }

    public void addImage(ElectroImage e){

        List<ElectroImage> aux = new ArrayList<>(listElectros.getValue().peekContent());
        aux.add(e);
        this.listElectros.postValue(new Event<>(aux));
    }


}
