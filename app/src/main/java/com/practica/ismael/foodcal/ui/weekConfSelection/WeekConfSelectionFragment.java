package com.practica.ismael.foodcal.ui.weekConfSelection;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Calendar;
import com.practica.ismael.foodcal.data.model.Week;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.WeekConfSelectionFragmentBinding;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;
import com.practica.ismael.foodcal.ui.main_activity.ToolbarConf;
import com.practica.ismael.foodcal.utils.KeyboardUtils;
import com.practica.ismael.foodcal.utils.NumberPickerDialog;

import java.util.Collections;

public class WeekConfSelectionFragment extends Fragment implements NumberPicker.OnValueChangeListener {

    private WeekConfSelectionFragmentBinding b;
    private ToolbarConf toolbarConf;
    private WeekConfSelectionViewModel vm;
    private MainActivityViewModel mainVM;
    private WeekConfSelectionFragmentAdapter listAdapter;
    private Week week;
    private int picker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.week_conf_selection_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toolbarConf = (ToolbarConf) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        toolbarConf.onConfigure(b.weekConfSelectionToolbar);

        vm = ViewModelProviders.of(this,
                new WeekConfSelectionViewModelFactory(ApiService.getInstance().getApi())).get(WeekConfSelectionViewModel.class);

        mainVM = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        listAdapter = new WeekConfSelectionFragmentAdapter(mainVM, vm);

        b.lstSelectionWeekConf.setHasFixedSize(true);
        b.lstSelectionWeekConf.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        b.lstSelectionWeekConf.setItemAnimator(new DefaultItemAnimator());
        b.lstSelectionWeekConf.setAdapter(listAdapter);

        vm.getListCalendars().observe(getViewLifecycleOwner(), v -> updateList());
        vm.getCalendars(mainVM.getIdUserToken().getValue());

        vm.getWeekSelected().observe(getViewLifecycleOwner(), this::showPopUp);
        vm.getDateTrigger().observe(getViewLifecycleOwner(), v -> showNumberPicker());
        vm.getCalendarTrigger().observe(getViewLifecycleOwner(), this::insertData);

    }

    private void insertData(Event<Calendar> calendarEvent) {
        if (!calendarEvent.hasBeenHandled()) {
            vm.getIncludes(mainVM.getIdUserToken().getValue(), picker, week, calendarEvent.getContentIfNotHandled().getIdCalendario());
        }
        requireActivity().onBackPressed();
    }

    private void showPopUp(Event<Week> weekEvent) {
        if (!weekEvent.hasBeenHandled()) {
            week = weekEvent.getContentIfNotHandled();
            showDatePicker();
        }
    }

    private void showDatePicker() {
        final java.util.Calendar calendario = java.util.Calendar.getInstance();
        int yy = calendario.get(java.util.Calendar.YEAR);
        int mm = calendario.get(java.util.Calendar.MONTH);
        int dd = calendario.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> vm.setDateTrigger(dayOfMonth + "/" + monthOfYear + "/" + year), yy, mm, dd);

        datePicker.show();
    }

    private void updateList() {
        vm.getListCalendars().observe(getViewLifecycleOwner(), calendars -> {
            Collections.sort(calendars);
            listAdapter.submitList(calendars);
            b.lblEmptySelectionWeekConf.setVisibility(calendars.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        vm.addCalendar(mainVM.getIdUserToken().getValue(), week.getNombreSemana());
        this.picker = picker.getValue();
    }

    private void showNumberPicker() {
        if (!vm.getDateTrigger().getValue().hasBeenHandled()){
            vm.getDateTrigger().getValue().getContentIfNotHandled();

            NumberPickerDialog newFragment = new NumberPickerDialog();
            newFragment.setValueChangeListener(this);
            newFragment.show(requireActivity().getSupportFragmentManager(), "time picker");
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem mnuSearch = menu.findItem(R.id.mnuSearch);
        SearchView searchView = (SearchView) mnuSearch.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.mnuSearch_hint));
        //searchView.setIconifiedByDefault(false);
        // In order to save state.
        mnuSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Make toolbar not scrollable.
                changeToolbarScrollBehavior(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                changeToolbarScrollBehavior(true);
                return true;
            }
        });
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter adapter when text is changed
                if (listAdapter != null) {
                    listAdapter.getFilter().filter(query);
                }

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void changeToolbarScrollBehavior(boolean scrollable) {
        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) b.weekConfSelectionToolbar.getLayoutParams();
        if (scrollable) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            params.setScrollFlags(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardUtils.hideSoftKeyboard(requireActivity());
    }
}
