package com.practica.ismael.foodcal.ui.calendarTable;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marcohc.robotocalendar.RobotoCalendarView;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Contains;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.CalendarTableFragmentBinding;
import com.practica.ismael.foodcal.ui.main_activity.ToolbarConf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CalendarTableFragment extends Fragment implements RobotoCalendarView.RobotoCalendarListener {

    private ToolbarConf toolbarConf;
    private CalendarTableFragmentBinding b;
    private CalendarTableFragmentViewModel vm;
    private final Map<String, Integer> mapLunch = new HashMap<>();
    private final Map<String, Integer> mapDinner = new HashMap<>();

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private BottomSheetBehavior bsb;

    private String idUsuario;
    private String nombreCalendario;
    private int idCalendario;

    private Date clearDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.calendar_table_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getArguments());

        idUsuario = CalendarTableFragmentArgs.fromBundle(getArguments()).getIdUsuario();
        nombreCalendario = CalendarTableFragmentArgs.fromBundle(getArguments()).getNombreCalendario();
        idCalendario = CalendarTableFragmentArgs.fromBundle(getArguments()).getIdCalendario();
        setupViews();
    }

    private void setupViews() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.calendarTableToolbar);
        toolbarConf.onConfigure(b.calendarTableToolbar);
        b.calendarTableToolbar.setTitle(nombreCalendario);

        vm = ViewModelProviders.of(this,
                new CalendarFragmentTableViewModelFactory(ApiService.getInstance().getApi())).get(CalendarTableFragmentViewModel.class);

        bsb = BottomSheetBehavior.from(b.bottomSheet.bottomSheet);
        b.bottomSheet.imgClose.setOnClickListener(v -> bsb.setState(BottomSheetBehavior.STATE_COLLAPSED));
        b.bottomSheet.imgClear.setOnClickListener(v -> clearDay());

        b.robotoCalendarPicker.setRobotoCalendarListener(this);

        vm.getFoodLiveData().observe(getViewLifecycleOwner(), this::showBottomSheet);
        vm.getListContains().observe(getViewLifecycleOwner(), v -> updateCalendar());
        vm.getContains(idUsuario, idCalendario);
    }

    private void clearDay() {
        vm.deleteContains(idUsuario, idCalendario, sdf.format(clearDate));
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        b.robotoCalendarPicker.clearSelectedDay();

        mapDinner.remove(sdf.format(clearDate));
        mapLunch.remove(sdf.format(clearDate));
    }

    private void showBottomSheet(Event<List<Food>> foodEvent) {
        String foods = "";

        if (!foodEvent.hasBeenHandled()) {
            if (foodEvent.peekContent().get(0) != null) {
                foods = "Lunch: " + foodEvent.peekContent().get(0).getNombrecomida();
            }
            if (foodEvent.peekContent().get(1) != null) {
                if (foods.isEmpty()) {
                    foods = "Dinner: " + foodEvent.getContentIfNotHandled().get(1).getNombrecomida();
                } else {
                    foods += "\nDinner: " + foodEvent.getContentIfNotHandled().get(1).getNombrecomida();
                }
            }
            b.bottomSheet.lblSheetText.setText(foods);
            bsb.setState(BottomSheetBehavior.STATE_EXPANDED);

        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toolbarConf = (ToolbarConf) context;
    }

    @Override
    public void onDayClick(Date date) {
        clearDate = date;
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        showFood(date);
    }

    @Override
    public void onDayLongClick(Date date) {
        CalendarTableFragmentDirections.DesCalendarTableToFoodSelectionTable action = CalendarTableFragmentDirections.desCalendarTableToFoodSelectionTable(sdf.format(date), idCalendario);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onRightButtonClick() {
        updateCalendar();
    }

    @Override
    public void onLeftButtonClick() {
        updateCalendar();
    }

    private void updateCalendar() {
        List<Contains> list = vm.getListContains().getValue();
        if (list != null && !list.isEmpty()) {
            Calendar cal = Calendar.getInstance();

            for (Contains c : list) {
                switch (c.getTipocomida()) {
                    case "Almuerzo":
                        mapLunch.put(c.getFecha(), c.getIdcomidaprincipal());
                        break;
                    case "Cena":
                        mapDinner.put(c.getFecha(), c.getIdcomidaprincipal());
                        break;
                }
                try {
                    cal.setTime(sdf.parse(c.getFecha()));
                    if (b.robotoCalendarPicker.getDate().getMonth() == cal.getTime().getMonth()) {
                        b.robotoCalendarPicker.markCircleImage2(cal.getTime());
                    }

                } catch (ParseException e) {
                    Log.e("Error", "Dates error in CalendarTableFragment");
                }
            }
        }
    }

    private void showFood(Date date) {
        int food1 = -1;
        int food2 = -1;

        b.bottomSheet.lblSheetTitle.setText(sdf.format(date));

        if (mapLunch.get(sdf.format(date)) != null) {
            food1 = mapLunch.get(sdf.format(date));
        }

        if (mapDinner.get(sdf.format(date)) != null) {
            food2 = mapDinner.get(sdf.format(date));
        }
        if (food1 != -1 || food2 != -1) {
            vm.getFood(food1, food2);
        }
    }
}
