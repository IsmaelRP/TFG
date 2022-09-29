package com.practica.ismael.foodcal.data.remote;

import com.practica.ismael.foodcal.data.model.Calendar;
import com.practica.ismael.foodcal.data.model.Contains;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.model.Include;
import com.practica.ismael.foodcal.data.model.Stretch;
import com.practica.ismael.foodcal.data.model.User;
import com.practica.ismael.foodcal.data.model.Week;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("user/add")
    Call<User> register(@Body User user);

    @POST("user/login")
    Call<User> login(@Body User user);

    @POST("user/name")
    Call<User> getName(@Query("idusuario") String idusuario);

    @POST("food/add")
    Call<Food> addFood(@Body Food food);

    @POST("food/all")
    Call<List<Food>> getFoods(@Query("idusuario") String idusuario);

    @POST("food/edit")
    Call<Food> editFood(@Body Food food);

    @POST("food/delete")
    Call<Food> deleteFood(@Body Food food);

    @POST("food/get")
    Call<List<Food>> getFood(@Query("idcomida1") int idcomida1, @Query("idcomida2") int idcomida2);

    @POST("week/add")
    Call<Week> addWeek(@Query("idusuario") String idusuario, @Query("nombresemana") String nombresemana);

    @POST("week/all")
    Call<List<Week>> getWeeks(@Query("idusuario") String idusuario);

    @POST("week/delete")
    Call<Week> deleteWeek(@Query("idusuario") String idusuario, @Query("idsemana") int idsemana);

    @POST("week/edit")
    Call<Week> editWeek(@Body Week week);

    @POST("stretch/add")
    Call<Stretch> addStretch(@Query("idtramo") String idtramo, @Query("idsemana") int idsemana,
                             @Query("idusuario") String idusuario, @Query("tipotramo") String tipotramo,
                             @Query("dia") String dia);

    @POST("stretch/delete")
    Call<Stretch> deleteStretch(@Query("idtramo") String idtramo, @Query("idsemana") int idsemana,
                             @Query("idusuario") String idusuario);

    @POST("include/add")
    Call<Include> addInclude(@Query("idtramo") String idtramo, @Query("idsemana") int idsemana,
                             @Query("idusuario") String idusuario,
                             @Query("idcomidaprincipal") int idcomidaprincipal);

    @POST("include/get")
    Call<List<Include>> getInclude(@Query("idusuario") String idusuario, @Query("idsemana") int idsemana);

    @POST("include/delete")
    Call<List<Include>> deleteInclude(@Query("idusuario") String idusuario, @Query("idsemana") int idsemana);

    @POST("calendar/add")
    Call<Calendar> addCalendar(@Query("idusuario") String idusuario, @Query("nombrecalendario") String nombrecalendario);

    @POST("calendar/all")
    Call<List<Calendar>> getCalendars(@Query("idusuario") String idusuario);

    @POST("calendar/delete")
    Call<Calendar> deleteCalendar(@Query("idusuario") String idusuario, @Query("idcalendario") int idcalendario);

    @POST("contains/add")
    Call<Contains> addContains(@Query("idcalendario") int idcalendario,
                               @Query("idusuariocalendario") String idusuariocalendario,
                               @Query("fecha") String fecha, @Query("idcomidaprincipal") int idcomida,
                               @Query("tipocomida") String tipocomida);

    @POST("contains/all")
    Call<List<Contains>> getContains(@Query("idusuario") String idusuario, @Query("idcalendario") int idcalendario);

    @POST("contains/delete")
    Call<List<Contains>> deleteContains(@Query("idusuario") String idusuario, @Query("idcalendario") int idcalendario);

    @POST("contains/deleteDay")
    Call<List<Contains>> deleteContainsDay(@Query("idusuario") String idusuario,
                                           @Query("idcalendario") int idcalendario,
                                           @Query("fecha") String fecha);

}
