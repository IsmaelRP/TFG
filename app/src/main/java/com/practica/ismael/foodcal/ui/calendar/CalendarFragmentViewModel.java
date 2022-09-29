package com.practica.ismael.foodcal.ui.calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Calendar;
import com.practica.ismael.foodcal.data.model.Contains;
import com.practica.ismael.foodcal.data.remote.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class CalendarFragmentViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<List<Calendar>> calendarsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Event<Calendar>> calendarTrigger = new MutableLiveData<>();
    private final MutableLiveData<Event<Calendar>> calendarDeletedTrigger = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> calendarDeletedAuxTrigger = new MutableLiveData<>();

    CalendarFragmentViewModel(Api api){
        this.api = api;
    }

    void getCalendars(String idusuario) {

        Call<List<Calendar>> response = api.getCalendars(idusuario);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Calendar>>() {
            @Override
            public void onResponse(Call<List<Calendar>> call, Response<List<Calendar>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setCalendarsLiveData(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<Calendar>> call, Throwable t) {
            }
        });
    }

    void deleteCalendar(Calendar calendar) {
        Call<Calendar> response = api.deleteCalendar(calendar.getIdUsuario(), calendar.getIdCalendario());

        List<Calendar> list = new ArrayList<>(getCalendarsLiveData().getValue());
        list.remove(calendar);
        setCalendarsLiveData(list);

        //noinspection NullableProblems
        response.enqueue(new Callback<Calendar>() {
            @Override
            public void onResponse(Call<Calendar> call, Response<Calendar> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Calendar> call, Throwable t) {
            }
        });
    }

    void deleteContains(String idusuario, int idcalendario) {
        Call<List<Contains>> response = api.deleteContains(idusuario, idcalendario);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Contains>>() {
            @Override
            public void onResponse(Call<List<Contains>> call, Response<List<Contains>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setCalendarDeletedAuxTrigger(true);
                }
            }

            @Override
            public void onFailure(Call<List<Contains>> call, Throwable t) {
            }
        });
    }

    MutableLiveData<List<Calendar>> getCalendarsLiveData() {
        return calendarsLiveData;
    }

    private void setCalendarsLiveData(List<Calendar> calendarsLiveData) {
        this.calendarsLiveData.setValue(calendarsLiveData);
    }

    MutableLiveData<Event<Calendar>> getCalendarTrigger() {
        return calendarTrigger;
    }

    void setCalendarTrigger(Calendar calendarTrigger) {
        this.calendarTrigger.setValue(new Event<>(calendarTrigger));
    }

    MutableLiveData<Event<Calendar>> getCalendarDeletedTrigger() {
        return calendarDeletedTrigger;
    }

    void setCalendarDeletedTrigger(Calendar calendarDeletedTrigger) {
        this.calendarDeletedTrigger.setValue(new Event<>(calendarDeletedTrigger));
    }

    MutableLiveData<Event<Boolean>> getCalendarDeletedAuxTrigger() {
        return calendarDeletedAuxTrigger;
    }

    void setCalendarDeletedAuxTrigger(boolean aux) {
        this.calendarDeletedAuxTrigger.setValue(new Event<>(aux));
    }
}
