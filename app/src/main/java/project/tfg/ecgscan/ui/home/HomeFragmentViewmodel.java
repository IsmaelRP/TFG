package project.tfg.ecgscan.ui.home;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import project.tfg.ecgscan.base.DiagnosisListener;
import project.tfg.ecgscan.base.Resource;
import project.tfg.ecgscan.data.DiagnosisTask;
import project.tfg.ecgscan.data.Repository;
import project.tfg.ecgscan.data.local.model.Electro;


public class HomeFragmentViewmodel extends ViewModel {


    private final Application application;
    private Repository repository;

    private LiveData<Resource<Long>> insertResult;
    private final MutableLiveData<Electro> insertTrigger = new MutableLiveData<>();

    private final MutableLiveData<String> diagnoseResponse = new MutableLiveData<>();

    HomeFragmentViewmodel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
        insertResult = Transformations.switchMap(insertTrigger, repository::insertElectro);
    }

    public LiveData<Resource<Long>> getInsertResult() {
        return insertResult;
    }

    public void insertElectro(Electro electro) {
        insertTrigger.setValue(electro);
    }


    public MutableLiveData<String> getDiagnoseResponse() {
        return diagnoseResponse;
    }

    public void  setDiagnoseResponse(String diagnose) {
        diagnoseResponse.postValue(diagnose);
    }

    public void diagnoseImage(Bitmap image) {

        DiagnosisTask task = new DiagnosisTask(new DiagnosisListener() {

            @Override
            public void onDiagnoseComplete(String result) {
                setDiagnoseResponse(result);
            }
        }, image);

        task.execute();


    }
}
