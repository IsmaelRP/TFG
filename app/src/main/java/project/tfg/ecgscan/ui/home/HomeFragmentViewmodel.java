package project.tfg.ecgscan.ui.home;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import project.tfg.ecgscan.data.Event;
import project.tfg.ecgscan.data.Repository;
import project.tfg.ecgscan.data.local.model.Electro;


public class HomeFragmentViewmodel extends ViewModel {


    private final Application application;
    private Repository repository;

    private final MutableLiveData<Event<String>>diagnoseResponse = new MutableLiveData<>();

    HomeFragmentViewmodel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
    }

    public void insertElectro(Electro electro) {
        repository.insertElectro(electro);
    }

    public MutableLiveData<Event<String>> getDiagnoseResponse() {
        return diagnoseResponse;
    }

    public void  setDiagnoseResponse(String diagnose) {
        diagnoseResponse.postValue(new Event<>(diagnose));
    }

    public void diagnoseImage(Bitmap image) {

        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            // TODO: implementar llamada a función Matlab con 'image'

            try {
                Thread.sleep(3000); // espera 5 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            setDiagnoseResponse("TODO: implementar diagnóstico en Matlab");
        });


    }
}
