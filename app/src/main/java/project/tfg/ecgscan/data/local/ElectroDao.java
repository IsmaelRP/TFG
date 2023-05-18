package project.tfg.ecgscan.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import project.tfg.ecgscan.base.BaseDao;
import project.tfg.ecgscan.data.local.model.Electro;

@Dao
public interface ElectroDao extends BaseDao<Electro> {

    @Query("SELECT * FROM Electro ORDER BY date")
    LiveData<List<Electro>> queryCompanies();

}
