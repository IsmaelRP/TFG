package project.tfg.ecgscan.data;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import project.tfg.ecgscan.base.Resource;
import project.tfg.ecgscan.data.local.ElectroDao;
import project.tfg.ecgscan.data.local.model.Electro;

public class RepositoryImpl implements Repository{

    private ElectroDao electroDao;

    public RepositoryImpl(ElectroDao electroDao) {
        this.electroDao = electroDao;
    }

    @Override
    public LiveData<List<Electro>> queryElectros() {
        return electroDao.queryCompanies();
    }

    @Override
    public LiveData<Resource<Long>> insertElectro(Electro electro) {
        MutableLiveData<Resource<Long>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                long id = electroDao.insert(electro);
                result.postValue(Resource.success(id));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<Resource<Integer>> deleteElectro(Electro electro) {
        MutableLiveData<Resource<Integer>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                int deleted = electroDao.delete(electro);
                result.postValue(Resource.success(deleted));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

}
