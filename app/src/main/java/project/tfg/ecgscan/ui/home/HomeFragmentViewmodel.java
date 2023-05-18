package project.tfg.ecgscan.ui.home;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import project.tfg.ecgscan.data.Event;
import project.tfg.ecgscan.data.Repository;
import project.tfg.ecgscan.data.api.ApiResponse;
import project.tfg.ecgscan.data.api.ApiService;
import project.tfg.ecgscan.data.api.RequestBody;
import project.tfg.ecgscan.data.local.model.Electro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragmentViewmodel extends ViewModel {


    private final Repository repository;

    private final MutableLiveData<Event<String>>diagnoseResponse = new MutableLiveData<>();

    private final MutableLiveData<ApiService>apiService = new MutableLiveData<>();

    HomeFragmentViewmodel(Repository repository) {
        this.repository = repository;
    }

    public void insertElectro(Electro electro) {
        repository.insertElectro(electro);
    }

    public MutableLiveData<Event<String>> getDiagnoseResponse() {
        return diagnoseResponse;
    }

    public void setDiagnoseResponse(String diagnose) {
        diagnoseResponse.postValue(new Event<>(diagnose));
    }

    public void diagnoseImage(Bitmap image) {

        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {

            if (getApiService() == null){
                setDiagnoseResponse("API NO INICIALIZADA");
            }else{
                int[] imageTranslated = bitmapToSimpleArray(image);

                List<Integer> mwdata = new ArrayList<>(imageTranslated.length);

                for (int i : imageTranslated) {
                    mwdata.add(i);
                }

                int[] mwsize = {image.getHeight(), image.getWidth(), 3};
                String mwtype = "uint8";

                RequestBody.Rhs rhs = new RequestBody.Rhs(mwdata, mwsize, mwtype);
                RequestBody requestBody = new RequestBody(1, rhs);

                requestMatlabDiagnose(requestBody);
            }


        });


    }

    public ApiService getApiService() {
        return apiService.getValue();
    }

    public void setApiService(Retrofit retrofit) {
        apiService.setValue(retrofit.create(ApiService.class));
    }


    public void requestMatlabDiagnose(RequestBody requestBody){

        Call<ApiResponse> call = apiService.getValue().getMatlabDiagnose(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    setDiagnoseResponse(response.body().getLhs().get(0).getMwdata()[0]);

                } else {
                    setDiagnoseResponse(response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                setDiagnoseResponse("Api caida");
            }
        });

    }


    public static int[] bitmapToSimpleArray(Bitmap img) {

        int[][][] pixels;
        int[] flatPixels;
        int auxColor;
        int idx;

        int height = img.getHeight();
        int width = img.getWidth();

        pixels = new int[height][width][3];

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                auxColor = img.getPixel(j, i);

                pixels[i][j][0] = Color.red(auxColor);
                pixels[i][j][1] = Color.green(auxColor);
                pixels[i][j][2] = Color.blue(auxColor);
            }
        }

        flatPixels = new int[height * width * 3];

        idx = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    flatPixels[idx++] = pixels[k][j][i];
                }
            }
        }

        return flatPixels;
    }


}
