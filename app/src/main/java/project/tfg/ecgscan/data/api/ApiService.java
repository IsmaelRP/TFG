package project.tfg.ecgscan.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("getDiagnoseMsg")
    //@POST("sumar")
    Call<ApiResponse> getMatlabDiagnose(@Body RequestBody requestBody);

}
