package project.tfg.ecgscan.base;

import androidx.room.Delete;
import androidx.room.Insert;

public interface BaseDao<M>{

    @Insert
    long insert(M model);

    /*
    @Update
    int update(M... model);
     */

    @Delete
    int delete(M... model);

}
