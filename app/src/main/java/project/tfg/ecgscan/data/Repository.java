package project.tfg.ecgscan.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import project.tfg.ecgscan.base.Resource;
import project.tfg.ecgscan.data.local.model.Electro;

public interface Repository {


    //  Electro objects
    LiveData<List<Electro>> queryElectros();
    LiveData<Resource<Long>> insertElectro(Electro electro);
    LiveData<Resource<Integer>> deleteElectro(Electro electro);


}
