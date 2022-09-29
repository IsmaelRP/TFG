package com.practica.ismael.foodcal.ui.weekConf;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.model.Include;
import com.practica.ismael.foodcal.data.model.Stretch;
import com.practica.ismael.foodcal.data.remote.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeekConfFragmentViewModel extends ViewModel {

    private final Api api;
    private MutableLiveData<List<Include>> listMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Food>> listFoods = new MutableLiveData<>();

    WeekConfFragmentViewModel(Api api) {
        this.api = api;
    }

    void addStretch(Stretch stretch) {
        Call<Stretch> response = api.addStretch(stretch.getIdtramo(), stretch.getIdsemana(), stretch.getIdusuario(), stretch.getTipotramo(), stretch.getDia());

        //noinspection NullableProblems
        response.enqueue(new Callback<Stretch>() {
            @Override
            public void onResponse(Call<Stretch> call, Response<Stretch> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Stretch> call, Throwable t) {
            }
        });
    }

    synchronized void addInclude(String idtramo, int idsemana, String idusuario, int idcomidaprincipal) {
        Call<Include> response = api.addInclude(idtramo, idsemana, idusuario, idcomidaprincipal);

        //noinspection NullableProblems
        response.enqueue(new Callback<Include>() {
            @Override
            public void onResponse(Call<Include> call, Response<Include> response) {
                if (response.isSuccessful() && response.body() != null) {
                }

            }

            @Override
            public void onFailure(Call<Include> call, Throwable t) {
            }
        });
    }

    void getIncludes(String idusuario, int idsemana) {
        Call<List<Include>> response = api.getInclude(idusuario, idsemana);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Include>>() {
            @Override
            public void onResponse(Call<List<Include>> call, Response<List<Include>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setListMutableLiveData(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Include>> call, Throwable t) {
            }
        });
    }

    void getFoods(String idUsuario) {
        Call<List<Food>> response = api.getFoods(idUsuario);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setListFoods(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
            }
        });
    }

    void deleteInclude(String idUsuario, int idSemana) {
        Call<List<Include>> response = api.deleteInclude(idUsuario, idSemana);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Include>>() {
            @Override
            public void onResponse(Call<List<Include>> call, Response<List<Include>> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<List<Include>> call, Throwable t) {
            }
        });
    }

    MutableLiveData<List<Include>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    private void setListMutableLiveData(List<Include> list) {
        this.listMutableLiveData.setValue(list);
    }

    MutableLiveData<List<Food>> getListFoods() {
        return listFoods;
    }

    private void setListFoods(List<Food> listFoods) {
        this.listFoods.setValue(listFoods);
    }

    void addFoodToList(Include e) {
        this.listMutableLiveData.getValue().add(e);
        this.listMutableLiveData = new MutableLiveData<>(getListMutableLiveData().getValue());
    }

    void clearFoodList(){
        this.listMutableLiveData.getValue().clear();
        this.listMutableLiveData = new MutableLiveData<>(getListMutableLiveData().getValue());
    }
}
