package com.practica.ismael.foodcal.ui.weekConfSelection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Calendar;
import com.practica.ismael.foodcal.data.model.Contains;
import com.practica.ismael.foodcal.data.model.Include;
import com.practica.ismael.foodcal.data.model.Week;
import com.practica.ismael.foodcal.data.remote.Api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeekConfSelectionViewModel extends ViewModel {

    private final Api api;
    private final MutableLiveData<List<Week>> listCalendars = new MutableLiveData<>();
    private final MutableLiveData<Event<Week>> weekSelected = new MutableLiveData<>();
    private final MutableLiveData<List<Include>> listData = new MutableLiveData<>();
    private final MutableLiveData<Event<Calendar>> calendarTrigger = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> dateTrigger = new MutableLiveData<>();

    WeekConfSelectionViewModel(Api api) {
        this.api = api;
    }

    void getCalendars(String idUsuario) {
        Call<List<Week>> response = api.getWeeks(idUsuario);

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Week>>() {
            @Override
            public void onResponse(Call<List<Week>> call, Response<List<Week>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setListCalendars(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Week>> call, Throwable t) {

            }
        });
    }

    void addCalendar(String idUsuario, String nombreCalendario) {
        Call<Calendar> response = api.addCalendar(idUsuario, nombreCalendario);

        //noinspection NullableProblems
        response.enqueue(new Callback<Calendar>() {
            @Override
            public void onResponse(Call<Calendar> call, Response<Calendar> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setCalendarTrigger(response.body());
                }
            }

            @Override
            public void onFailure(Call<Calendar> call, Throwable t) {

            }
        });
    }

    void getIncludes(String idUsuario, int numberOfWeeks, Week week, int idcalendario) {
        Call<List<Include>> response = api.getInclude(idUsuario, week.getIdSemana());

        //noinspection NullableProblems
        response.enqueue(new Callback<List<Include>>() {
            @Override
            public void onResponse(Call<List<Include>> call, Response<List<Include>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Contains c;
                    Map<String, ArrayList<Include>> map = new LinkedHashMap<>();
                    Map<String, Iterator<Include>> iterables = new LinkedHashMap<>();

                    ArrayList<String> values = new ArrayList<>();

                    values.add("LA");
                    values.add("MA");
                    values.add("XA");
                    values.add("JA");
                    values.add("VA");
                    values.add("SA");
                    values.add("DA");
                    values.add("LC");
                    values.add("MC");
                    values.add("XC");
                    values.add("JC");
                    values.add("VC");
                    values.add("SC");
                    values.add("DC");

                    for (String s : values) {
                        map.put(s, new ArrayList<>());
                        iterables.put(s, map.get(s).iterator());
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    try {
                        cal.setTime(dateFormat.parse(getDateTrigger().getValue().peekContent()));
                        cal.add(java.util.Calendar.MONTH, 1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cal.setFirstDayOfWeek(java.util.Calendar.MONDAY);
                    String tipoComida;
                    String tipo;

                    for (Include i : response.body()) {
                        map.get(i.getIdtramo()).add(i);
                    }

                    for (int i = 0; i < numberOfWeeks; i++) {

                        switch (cal.get(java.util.Calendar.DAY_OF_WEEK)) {
                            case java.util.Calendar.MONDAY:
                                tipoComida = "LA";
                                tipo = "Almuerzo";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }

                                tipoComida = "LC";
                                tipo = "Cena";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }
                                break;
                            case java.util.Calendar.TUESDAY:
                                tipoComida = "MA";
                                tipo = "Almuerzo";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }

                                tipoComida = "MC";
                                tipo = "Cena";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }
                                break;
                            case java.util.Calendar.WEDNESDAY:
                                tipoComida = "XA";
                                tipo = "Almuerzo";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }

                                tipoComida = "XC";
                                tipo = "Cena";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }
                                break;
                            case java.util.Calendar.THURSDAY:
                                tipoComida = "JA";
                                tipo = "Almuerzo";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }

                                tipoComida = "JC";
                                tipo = "Cena";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }
                                break;
                            case java.util.Calendar.FRIDAY:
                                tipoComida = "VA";
                                tipo = "Almuerzo";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }

                                tipoComida = "VC";
                                tipo = "Cena";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }
                                break;
                            case java.util.Calendar.SATURDAY:
                                tipoComida = "SA";
                                tipo = "Almuerzo";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }

                                tipoComida = "SC";
                                tipo = "Cena";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }
                                break;
                            case java.util.Calendar.SUNDAY:
                                tipoComida = "DA";
                                tipo = "Almuerzo";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }

                                tipoComida = "DC";
                                tipo = "Cena";
                                if (!map.get(tipoComida).isEmpty()) {
                                    if (iterables.get(tipoComida).hasNext()) {
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    } else {
                                        iterables.put(tipoComida, map.get(tipoComida).iterator());
                                        c = new Contains(idcalendario, idUsuario, dateFormat.format(cal.getTime()), iterables.get(tipoComida).next().getIdcomidaprincipal(), tipo);
                                        insertData(c);
                                    }
                                }
                                break;
                        }


                        cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                    }
                }
            }


            @Override
            public void onFailure(Call<List<Include>> call, Throwable t) {

            }
        });
    }


    public void insertData(Contains contains) {
        Call<Contains> response = api.addContains(contains.getIdcalendario(),
                contains.getIdusuariocalendario(), contains.getFecha(), contains.getIdcomidaprincipal(),
                contains.getTipocomida());

        //noinspection NullableProblems
        response.enqueue(new Callback<Contains>() {
            @Override
            public void onResponse(Call<Contains> call, Response<Contains> response) {
                if (response.isSuccessful() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Contains> call, Throwable t) {
            }
        });
    }

    MutableLiveData<List<Week>> getListCalendars() {
        return listCalendars;
    }

    private void setListCalendars(List<Week> listCalendars) {
        this.listCalendars.setValue(listCalendars);
    }

    MutableLiveData<Event<Week>> getWeekSelected() {
        return weekSelected;
    }

    void setWeekSelected(Week weekSelected) {
        this.weekSelected.setValue(new Event<>(weekSelected));
    }

    public MutableLiveData<List<Include>> getListData() {
        return listData;
    }

    public void setListData(List<Include> listData) {
        this.listData.setValue(listData);
    }

    MutableLiveData<Event<Calendar>> getCalendarTrigger() {
        return calendarTrigger;
    }

    private void setCalendarTrigger(Calendar calendarTrigger) {
        this.calendarTrigger.setValue(new Event<>(calendarTrigger));
    }

    public MutableLiveData<Event<String>> getDateTrigger() {
        return dateTrigger;
    }

    public void setDateTrigger(String dateTrigger) {
        this.dateTrigger.setValue(new Event<>(dateTrigger));
    }
}
