package com.practica.ismael.foodcal.ui.weekConf;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Day;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.model.Include;
import com.practica.ismael.foodcal.data.model.Stretch;
import com.practica.ismael.foodcal.data.model.StretchType;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.WeekconfFragmentBinding;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;
import com.practica.ismael.foodcal.ui.main_activity.ToolbarConf;
import com.practica.ismael.foodcal.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WeekConfFragment extends Fragment {

    private WeekconfFragmentBinding b;
    private ToolbarConf toolbarConf;
    private String idUsuario;
    private int idSemana;
    private String nombreSemana;
    private String type;
    private NavController navController;
    private BottomSheetBehavior bsb;

    private WeekConfFragmentViewModel vm;
    private final Map<Integer, Food> mapFoods = new HashMap<>();

    private ArrayList<Food> mondayLunch = new ArrayList<>();
    private ArrayList<Food> tuesdayLunch = new ArrayList<>();
    private ArrayList<Food> wednesdayLunch = new ArrayList<>();
    private ArrayList<Food> thursdayLunch = new ArrayList<>();
    private ArrayList<Food> fridayLunch = new ArrayList<>();
    private ArrayList<Food> saturdayLunch = new ArrayList<>();
    private ArrayList<Food> sundayLunch = new ArrayList<>();

    private ArrayList<Food> mondayDinner = new ArrayList<>();
    private ArrayList<Food> tuesdayDinner = new ArrayList<>();
    private ArrayList<Food> wednesdayDinner = new ArrayList<>();
    private ArrayList<Food> thursdayDinner = new ArrayList<>();
    private ArrayList<Food> fridayDinner = new ArrayList<>();
    private ArrayList<Food> saturdayDinner = new ArrayList<>();
    private ArrayList<Food> sundayDinner = new ArrayList<>();

    private ArrayList<Food> clearList;
    private String clearTitle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.weekconf_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getArguments());

        idUsuario = WeekConfFragmentArgs.fromBundle(getArguments()).getIdusuario();
        idSemana = WeekConfFragmentArgs.fromBundle(getArguments()).getIdsemana();
        nombreSemana = WeekConfFragmentArgs.fromBundle(getArguments()).getNombreSemana();

        setupViews();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toolbarConf = (ToolbarConf) context;
    }

    private void setupViews() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.weekconfToolbar);
        toolbarConf.onConfigure(b.weekconfToolbar);
        b.weekconfToolbar.setTitle(nombreSemana);

        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        vm = ViewModelProviders.of(this,
                new WeekConfFragmentViewModelFactory(ApiService.getInstance().getApi())).get(WeekConfFragmentViewModel.class);

        MainActivityViewModel mainVM = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        bsb = BottomSheetBehavior.from(b.bottomSheet.bottomSheet);
        b.bottomSheet.imgClose.setOnClickListener(v -> bsb.setState(BottomSheetBehavior.STATE_COLLAPSED));

        b.bottomSheet.imgClear.setOnClickListener(v -> clear());

        b.btnMondayLunch.setOnClickListener(v -> navigateSelectionFoods("LA"));
        b.btnTuesdayLunch.setOnClickListener(v -> navigateSelectionFoods("MA"));
        b.btnWednesdayLunch.setOnClickListener(v -> navigateSelectionFoods("XA"));
        b.btnThursdayLunch.setOnClickListener(v -> navigateSelectionFoods("JA"));
        b.btnFridayLunch.setOnClickListener(v -> navigateSelectionFoods("VA"));
        b.btnSaturdayLunch.setOnClickListener(v -> navigateSelectionFoods("SA"));
        b.btnSundayLunch.setOnClickListener(v -> navigateSelectionFoods("DA"));

        b.btnMondayDinner.setOnClickListener(v -> navigateSelectionFoods("LC"));
        b.btnTuesdayDinner.setOnClickListener(v -> navigateSelectionFoods("MC"));
        b.btnWednesdayDinner.setOnClickListener(v -> navigateSelectionFoods("XC"));
        b.btnThursdayDinner.setOnClickListener(v -> navigateSelectionFoods("JC"));
        b.btnFridayDinner.setOnClickListener(v -> navigateSelectionFoods("VC"));
        b.btnSaturdayDinner.setOnClickListener(v -> navigateSelectionFoods("SC"));
        b.btnSundayDinner.setOnClickListener(v -> navigateSelectionFoods("DC"));

        b.mondayLunch.setOnClickListener(v -> setBottomSheet(mondayLunch, "Monday - Lunch"));
        b.tuesdayLunch.setOnClickListener(v -> setBottomSheet(tuesdayLunch, "Tuesday - Lunch"));
        b.wednesdayLunch.setOnClickListener(v -> setBottomSheet(wednesdayLunch, "Wednesday - Lunch"));
        b.thursdayLunch.setOnClickListener(v -> setBottomSheet(thursdayLunch, "Thursday - Lunch"));
        b.fridayLunch.setOnClickListener(v -> setBottomSheet(fridayLunch, "Friday - Lunch"));
        b.saturdayLunch.setOnClickListener(v -> setBottomSheet(saturdayLunch, "Saturday - Lunch"));
        b.sundayLunch.setOnClickListener(v -> setBottomSheet(sundayLunch, "Sunday - Lunch"));

        b.mondayDinner.setOnClickListener(v -> setBottomSheet(mondayDinner, "Monday - Dinner"));
        b.tuesdayDinner.setOnClickListener(v -> setBottomSheet(tuesdayDinner, "Tuesday - Dinner"));
        b.wednesdayDinner.setOnClickListener(v -> setBottomSheet(wednesdayDinner, "Wednesday - Dinner"));
        b.thursdayDinner.setOnClickListener(v -> setBottomSheet(thursdayDinner, "Thursday - Dinner"));
        b.fridayDinner.setOnClickListener(v -> setBottomSheet(fridayDinner, "Friday - Dinner"));
        b.saturdayDinner.setOnClickListener(v -> setBottomSheet(saturdayDinner, "Saturday - Dinner"));
        b.sundayDinner.setOnClickListener(v -> setBottomSheet(sundayDinner, "Sunday - Dinner"));

        mainVM.getFoodSelected().observe(getViewLifecycleOwner(), this::addToList);
        vm.getListMutableLiveData().observe(getViewLifecycleOwner(), this::addList);

        if (vm.getListMutableLiveData().getValue() == null) {
            vm.getIncludes(idUsuario, idSemana);
        }

        vm.getListFoods().observe(getViewLifecycleOwner(), this::addToMap);
        vm.getFoods(idUsuario);
    }

    private void clear() {
        clearList.clear();
        setFoodCounter();
        setBottomSheet(clearList, clearTitle);
        vm.clearFoodList();
    }

    private void addToMap(List<Food> foods) {
        for (Food f : foods) {
            mapFoods.put(f.getIdcomida(), f);
        }
    }

    private void resetLists() {
        mondayLunch = new ArrayList<>();
        tuesdayLunch = new ArrayList<>();
        wednesdayLunch = new ArrayList<>();
        thursdayLunch = new ArrayList<>();
        fridayLunch = new ArrayList<>();
        saturdayLunch = new ArrayList<>();
        sundayLunch = new ArrayList<>();

        mondayDinner = new ArrayList<>();
        tuesdayDinner = new ArrayList<>();
        wednesdayDinner = new ArrayList<>();
        thursdayDinner = new ArrayList<>();
        fridayDinner = new ArrayList<>();
        saturdayDinner = new ArrayList<>();
        sundayDinner = new ArrayList<>();
    }

    private void addList(List<Include> includes) {
        resetLists();

        for (Include i : includes) {
            switch (i.getIdtramo()) {
                case "LA":
                    mondayLunch.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "MA":
                    tuesdayLunch.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "XA":
                    wednesdayLunch.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "JA":
                    thursdayLunch.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "VA":
                    fridayLunch.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "SA":
                    saturdayLunch.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "DA":
                    sundayLunch.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "LC":
                    mondayDinner.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "MC":
                    tuesdayDinner.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "XC":
                    wednesdayDinner.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "JC":
                    thursdayDinner.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "VC":
                    fridayDinner.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "SC":
                    saturdayDinner.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
                case "DC":
                    sundayDinner.add(mapFoods.get(i.getIdcomidaprincipal()));
                    break;
            }
        }
        setFoodCounter();
    }

    private void setBottomSheet(ArrayList<Food> list, String title) {
        clearList = list;
        clearTitle = title;

        StringBuilder foods = new StringBuilder();
        b.bottomSheet.lblSheetTitle.setText(title);
        for (Food f : list) {
            if (f != null) {
                foods.append(f.getNombrecomida()).append("\n");
            }
        }
        if (foods.length() == 0) {
            b.bottomSheet.lblSheetText.setText(getString(R.string.lblSheet_empty));
        } else {
            b.bottomSheet.lblSheetText.setText(foods);
        }
        bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void addToList(Event<Food> foodEvent) {
        if (!foodEvent.hasBeenHandled()) {

            switch (type) {
                case "LA":
                    mondayLunch.add(foodEvent.getContentIfNotHandled());
                    break;
                case "MA":
                    tuesdayLunch.add(foodEvent.getContentIfNotHandled());
                    break;
                case "XA":
                    wednesdayLunch.add(foodEvent.getContentIfNotHandled());
                    break;
                case "JA":
                    thursdayLunch.add(foodEvent.getContentIfNotHandled());
                    break;
                case "VA":
                    fridayLunch.add(foodEvent.getContentIfNotHandled());
                    break;
                case "SA":
                    saturdayLunch.add(foodEvent.getContentIfNotHandled());
                    break;
                case "DA":
                    sundayLunch.add(foodEvent.getContentIfNotHandled());
                    break;
                case "LC":
                    mondayDinner.add(foodEvent.getContentIfNotHandled());
                    break;
                case "MC":
                    tuesdayDinner.add(foodEvent.getContentIfNotHandled());
                    break;
                case "XC":
                    wednesdayDinner.add(foodEvent.getContentIfNotHandled());
                    break;
                case "JC":
                    thursdayDinner.add(foodEvent.getContentIfNotHandled());
                    break;
                case "VC":
                    fridayDinner.add(foodEvent.getContentIfNotHandled());
                    break;
                case "SC":
                    saturdayDinner.add(foodEvent.getContentIfNotHandled());
                    break;
                case "DC":
                    sundayDinner.add(foodEvent.getContentIfNotHandled());
                    break;
            }
            vm.addFoodToList(new Include(type, idUsuario, idSemana, 0, foodEvent.peekContent().getIdcomida()));
            setFoodCounter();
        }
    }

    private void navigateSelectionFoods(String type) {
        this.type = type;
        navController.navigate(WeekConfFragmentDirections.desWeekConfToFoodSelection());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuSave) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        //Lunch
        vm.addStretch(new Stretch("LA", idUsuario, idSemana, StretchType.ALMUERZO.toString(), Day.LUNES.toString()));
        vm.addStretch(new Stretch("MA", idUsuario, idSemana, StretchType.ALMUERZO.toString(), Day.MARTES.toString()));
        vm.addStretch(new Stretch("XA", idUsuario, idSemana, StretchType.ALMUERZO.toString(), Day.MIÉRCOLES.toString()));
        vm.addStretch(new Stretch("JA", idUsuario, idSemana, StretchType.ALMUERZO.toString(), Day.JUEVES.toString()));
        vm.addStretch(new Stretch("VA", idUsuario, idSemana, StretchType.ALMUERZO.toString(), Day.VIERNES.toString()));
        vm.addStretch(new Stretch("SA", idUsuario, idSemana, StretchType.ALMUERZO.toString(), Day.SÁBADO.toString()));
        vm.addStretch(new Stretch("DA", idUsuario, idSemana, StretchType.ALMUERZO.toString(), Day.DOMINGO.toString()));

        //Dinner
        vm.addStretch(new Stretch("LC", idUsuario, idSemana, StretchType.CENA.toString(), Day.LUNES.toString()));
        vm.addStretch(new Stretch("MC", idUsuario, idSemana, StretchType.CENA.toString(), Day.MARTES.toString()));
        vm.addStretch(new Stretch("XC", idUsuario, idSemana, StretchType.CENA.toString(), Day.MIÉRCOLES.toString()));
        vm.addStretch(new Stretch("JC", idUsuario, idSemana, StretchType.CENA.toString(), Day.JUEVES.toString()));
        vm.addStretch(new Stretch("VC", idUsuario, idSemana, StretchType.CENA.toString(), Day.VIERNES.toString()));
        vm.addStretch(new Stretch("SC", idUsuario, idSemana, StretchType.CENA.toString(), Day.SÁBADO.toString()));
        vm.addStretch(new Stretch("DC", idUsuario, idSemana, StretchType.CENA.toString(), Day.DOMINGO.toString()));

        vm.deleteInclude(idUsuario, idSemana);

        //Lunch
        for (Food f : mondayLunch) {
            vm.addInclude("LA", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : tuesdayLunch) {
            vm.addInclude("MA", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : wednesdayLunch) {
            vm.addInclude("XA", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : thursdayLunch) {
            vm.addInclude("JA", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : fridayLunch) {
            vm.addInclude("VA", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : saturdayLunch) {
            vm.addInclude("SA", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : sundayLunch) {
            vm.addInclude("DA", idSemana, idUsuario, f.getIdcomida());
        }

        //Dinner
        for (Food f : mondayDinner) {
            vm.addInclude("LC", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : tuesdayDinner) {
            vm.addInclude("MC", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : wednesdayDinner) {
            vm.addInclude("XC", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : thursdayDinner) {
            vm.addInclude("JC", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : fridayDinner) {
            vm.addInclude("VC", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : saturdayDinner) {
            vm.addInclude("SC", idSemana, idUsuario, f.getIdcomida());
        }
        for (Food f : sundayDinner) {
            vm.addInclude("DC", idSemana, idUsuario, f.getIdcomida());
        }

        requireActivity().onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardUtils.hideSoftKeyboard(requireActivity());
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        setFoodCounter();
    }

    private void setFoodCounter() {
        //Lunch
        b.mondayLunch.setText(getResources().getQuantityString(R.plurals.foodCounter_text, mondayLunch.size(), mondayLunch.size()));
        b.tuesdayLunch.setText(getResources().getQuantityString(R.plurals.foodCounter_text, tuesdayLunch.size(), tuesdayLunch.size()));
        b.wednesdayLunch.setText(getResources().getQuantityString(R.plurals.foodCounter_text, wednesdayLunch.size(), wednesdayLunch.size()));
        b.thursdayLunch.setText(getResources().getQuantityString(R.plurals.foodCounter_text, thursdayLunch.size(), thursdayLunch.size()));
        b.fridayLunch.setText(getResources().getQuantityString(R.plurals.foodCounter_text, fridayLunch.size(), fridayLunch.size()));
        b.saturdayLunch.setText(getResources().getQuantityString(R.plurals.foodCounter_text, saturdayLunch.size(), saturdayLunch.size()));
        b.sundayLunch.setText(getResources().getQuantityString(R.plurals.foodCounter_text, sundayLunch.size(), sundayLunch.size()));

        //Dinner
        b.mondayDinner.setText(getResources().getQuantityString(R.plurals.foodCounter_text, mondayDinner.size(), mondayDinner.size()));
        b.tuesdayDinner.setText(getResources().getQuantityString(R.plurals.foodCounter_text, tuesdayDinner.size(), tuesdayDinner.size()));
        b.wednesdayDinner.setText(getResources().getQuantityString(R.plurals.foodCounter_text, wednesdayDinner.size(), wednesdayDinner.size()));
        b.thursdayDinner.setText(getResources().getQuantityString(R.plurals.foodCounter_text, thursdayDinner.size(), thursdayDinner.size()));
        b.fridayDinner.setText(getResources().getQuantityString(R.plurals.foodCounter_text, fridayDinner.size(), fridayDinner.size()));
        b.saturdayDinner.setText(getResources().getQuantityString(R.plurals.foodCounter_text, saturdayDinner.size(), saturdayDinner.size()));
        b.sundayDinner.setText(getResources().getQuantityString(R.plurals.foodCounter_text, sundayDinner.size(), sundayDinner.size()));
    }
}
