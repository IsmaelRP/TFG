package project.tfg.ecgscan.ui.list.local;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import project.tfg.ecgscan.data.Repository;
import project.tfg.ecgscan.data.local.model.Electro;

public class LocalListFragmentViewModel extends ViewModel {

    private final Application application;
    private final LiveData<List<Electro>> electros;
    private Repository repository;


    LocalListFragmentViewModel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
        electros = this.repository.queryElectros();

    }


    public LiveData<List<Electro>> getElectros() {
        return electros;
    }


    public void deleteImgFromLocal(Electro electro) {
        repository.deleteElectro(electro);
    }




}
