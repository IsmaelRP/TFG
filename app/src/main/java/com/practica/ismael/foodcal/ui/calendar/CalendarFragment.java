package com.practica.ismael.foodcal.ui.calendar;

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
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Calendar;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.CalendarFragmentBinding;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;
import com.practica.ismael.foodcal.ui.main_activity.ToolbarConf;
import com.practica.ismael.foodcal.utils.KeyboardUtils;
import com.practica.ismael.foodcal.utils.SnackbarUtils;

import java.util.Collections;

public class CalendarFragment extends Fragment {

    private CalendarFragmentBinding b;
    private ToolbarConf toolbarConf;
    private CalendarFragmentAdapter listAdapter;

    private CalendarFragmentViewModel vm;
    private MainActivityViewModel mainVM;

    private Calendar cal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.calendar_fragment, container, false);
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
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.calendarToolbar);
        toolbarConf.onConfigure(b.calendarToolbar);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

        b.calendarFab.setOnClickListener(v -> navigateToWeekConfSelection());
        b.lblEmptyCalendar.setOnClickListener(v -> navigateToWeekConfSelection());

        vm = ViewModelProviders.of(this,
                new CalendarFragmentViewModelFactory(ApiService.getInstance().getApi())).get(CalendarFragmentViewModel.class);
        mainVM = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        listAdapter = new CalendarFragmentAdapter(vm);

        b.lstCalendar.setHasFixedSize(true);
        b.lstCalendar.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        b.lstCalendar.setItemAnimator(new DefaultItemAnimator());
        b.lstCalendar.setAdapter(listAdapter);

        vm.getCalendarsLiveData().observe(getViewLifecycleOwner(), v -> updateList());
        vm.getCalendarTrigger().observe(getViewLifecycleOwner(), this::navigateToCalendarTable);
        vm.getCalendarDeletedTrigger().observe(getViewLifecycleOwner(), this::deleteCalendar);
        vm.getCalendarDeletedAuxTrigger().observe(getViewLifecycleOwner(), this::deleteCalendarAux);
        vm.getCalendars(mainVM.getIdUserToken().getValue());

        setupSwipeToRefresh();
    }

    private void setupSwipeToRefresh() {
        b.swRefreshCalendar.setOnRefreshListener(() -> vm.getCalendars(mainVM.getIdUserToken().getValue()));
        b.swRefreshCalendar.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void deleteCalendar(Event<Calendar> calendarEvent) {
        if (!calendarEvent.hasBeenHandled()) {
            vm.deleteContains(calendarEvent.peekContent().getIdUsuario(), calendarEvent.peekContent().getIdCalendario());
            cal = calendarEvent.getContentIfNotHandled();

            SnackbarUtils.snackbar(requireView(), calendarEvent.peekContent().getNombreCalendario().concat(" deleted"), Snackbar.LENGTH_SHORT);
        }
    }

    private void deleteCalendarAux(Event<Boolean> booleanEvent) {
        if (!booleanEvent.hasBeenHandled()) {
            booleanEvent.getContentIfNotHandled();
            vm.deleteCalendar(cal);
        }
    }

    private void navigateToCalendarTable(Event<Calendar> calendarEvent) {
        if (!calendarEvent.hasBeenHandled()) {
            CalendarFragmentDirections.DesCalendarToCalendarTable action = CalendarFragmentDirections.desCalendarToCalendarTable(calendarEvent.peekContent().getIdCalendario(), calendarEvent.peekContent().getIdUsuario(), calendarEvent.getContentIfNotHandled().getNombreCalendario());
            Navigation.findNavController(requireView()).navigate(action);
        }
    }

    private void updateList() {
        vm.getCalendarsLiveData().observe(getViewLifecycleOwner(), calendars -> {
            Collections.sort(calendars);
            listAdapter.submitList(calendars);
            b.lblEmptyCalendar.setVisibility(calendars.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            b.swRefreshCalendar.setRefreshing(false);
        });
    }

    private void navigateToWeekConfSelection() {
        Navigation.findNavController(requireView()).navigate(CalendarFragmentDirections.desCalendarToWeekConf());
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
                (AppBarLayout.LayoutParams) b.calendarToolbar.getLayoutParams();
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
