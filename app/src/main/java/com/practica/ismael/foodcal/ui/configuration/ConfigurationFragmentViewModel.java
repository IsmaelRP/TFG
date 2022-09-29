package com.practica.ismael.foodcal.ui.configuration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Stretch;
import com.practica.ismael.foodcal.data.model.Week;
import com.practica.ismael.foodcal.data.remote.Api;

import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ConfigurationFragmentViewModel extends ViewModel {

    private final Api api;

    private final MutableLiveData<Event<Week>> weekLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> errorTrigger = new MutableLiveData<>();
    private final MutableLiveData<List<Week>> weekListLiveData = new MutableLiveData<>();

    private final MutableLiveData<Event<Week>> weekDeletedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<Week>> weekEditTrigger = new MutableLiveData<>();
    private final MutableLiveData<Event<Week>> weekTrigger = new MutableLiveData<>();

    ConfigurationFragmentViewModel(Api api) {
        this.api = api;
    }

    void addWeek(String userId, String weekName) {
        Call<Week> response = api.addWeek(userId, weekName);

        //noinspection NullableProblems
        response.enqueue(new Callback<Week>() {
            @Override
            public void onResponse(Call<Week> call, Response<Week> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setWeekLiveData(response.body());

                    List<Week> list = new ArrayList<>(Objects.requireNonNull(getWeekListLiveData().getValue()));
                    list.add(response.body());
                    setWeekListLiveData(list);
                }
            }

            @Override
            public void onFailure(Call<Week> call, Throwable t) {
                if (t instanceof EOFException) {
                    errorTrigger.setValue(new Event<>("Ops, something is wrong"));
                } else if (t instanceof SocketTimeoutException) {
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                } else {
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
    }

    void getWeeks(String userId) {
        Call<List<Week>> response = api.getWeeks(userId);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Week>>() {
            @Override
            public void onResponse(Call<List<Week>> call, Response<List<Week>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setWeekListLiveData(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<Week>> call, Throwable t) {
                if (t instanceof EOFException) {
                    errorTrigger.setValue(new Event<>("Ops, something is wrong"));
                } else if (t instanceof SocketTimeoutException) {
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                } else {
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
    }

    void deleteWeek(Week week) {
        List<Week> list = new ArrayList<>(Objects.requireNonNull(getWeekListLiveData().getValue()));
        list.remove(week);
        setWeekListLiveData(list);

        Call<Week> response = api.deleteWeek(week.getIdUsuario(), week.getIdSemana());

        //noinspection NullableProblems
        response.enqueue(new Callback<Week>() {
            @Override
            public void onResponse(Call<Week> call, Response<Week> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Week> call, Throwable t) {
                if (t instanceof EOFException){
                    errorTrigger.setValue(new Event<>("Ops, something is wrong"));
                }else if (t instanceof SocketTimeoutException){
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                }else{
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
    }

    void editWeek(Week week) {
        List<Week> list = new ArrayList<>(Objects.requireNonNull(getWeekListLiveData().getValue()));
        list.set(list.indexOf(week), week);
        setWeekListLiveData(list);

        Call<Week> response = api.editWeek(week);

        //noinspection NullableProblems
        response.enqueue(new Callback<Week>() {
            @Override
            public void onResponse(Call<Week> call, Response<Week> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Week> call, Throwable t) {
                if (t instanceof EOFException){
                    errorTrigger.setValue(new Event<>("Ops, something is wrong"));
                }else if (t instanceof SocketTimeoutException){
                    errorTrigger.setValue(new Event<>("Server is not responding"));
                }else{
                    errorTrigger.setValue(new Event<>(t.toString()));
                }
            }
        });
    }

    void deleteStretch(Week week, String idtramo) {
        Call<Stretch> response = api.deleteStretch(idtramo, week.getIdSemana(), week.getIdUsuario());

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


    MutableLiveData<Event<Week>> getWeekLiveData() {
        return weekLiveData;
    }

    private void setWeekLiveData(Week week) {
        this.weekLiveData.setValue(new Event<>(week));
    }

    MutableLiveData<List<Week>> getWeekListLiveData() {
        return weekListLiveData;
    }

    private void setWeekListLiveData(List<Week> weekListLiveData) {
        this.weekListLiveData.setValue(weekListLiveData);
    }


    MutableLiveData<Event<String>> getErrorTrigger() {
        return errorTrigger;
    }

    MutableLiveData<Event<Week>> getWeekDeletedLiveData() {
        return weekDeletedLiveData;
    }

    public void setFoodDeletedLiveData(Week week) {
        this.weekDeletedLiveData.setValue(new Event<>(week));
    }

    MutableLiveData<Event<Week>> getWeekEditTrigger() {
        return weekEditTrigger;
    }

    void setWeekEditTrigger(Week week) {
        this.weekEditTrigger.setValue(new Event<>(week));
    }

    void setWeekDeletedLiveData(Week week) {
        this.weekDeletedLiveData.setValue(new Event<>(week));
    }

    MutableLiveData<Event<Week>> getWeekTrigger() {
        return weekTrigger;
    }

    void setWeekTrigger(Week week) {
        this.weekTrigger.setValue(new Event<>(week));
    }

}
