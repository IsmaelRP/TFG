package com.practica.ismael.foodcal.ui.configuration;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Week;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.ConfigurationFragmentBinding;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;
import com.practica.ismael.foodcal.ui.main_activity.ToolbarConf;
import com.practica.ismael.foodcal.utils.CustomLayoutDialogFragment;
import com.practica.ismael.foodcal.utils.KeyboardUtils;
import com.practica.ismael.foodcal.utils.SnackbarUtils;

import java.util.Collections;
import java.util.Objects;

public class ConfigurationFragment extends Fragment implements CustomLayoutDialogFragment.Listener {

    private ConfigurationFragmentBinding b;
    private ToolbarConf toolbarConf;
    private ConfigurationFragmentViewModel vm;
    private MainActivityViewModel mainVM;
    private ConfigurationFragmentAdapter listAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.configuration_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
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

    private void setupViews() {

        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.confToolbar);
        toolbarConf.onConfigure(b.confToolbar);

        vm = ViewModelProviders.of(this,
                new ConfigurationFragmentViewModelFactory(ApiService.getInstance().getApi())).get(ConfigurationFragmentViewModel.class);

        mainVM = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        vm.getWeekListLiveData().observe(getViewLifecycleOwner(), v -> updateList());
        vm.getWeekLiveData().observe(getViewLifecycleOwner(), this::showMessage);
        vm.getErrorTrigger().observe(getViewLifecycleOwner(), this::showError);
        vm.getWeekDeletedLiveData().observe(getViewLifecycleOwner(), this::showDeleted);
        vm.getWeekEditTrigger().observe(getViewLifecycleOwner(), v -> showFoodEditDialog());
        vm.getWeekTrigger().observe(getViewLifecycleOwner(), this::navigateToWeek);

        b.confFab.setOnClickListener(v -> showFoodDialog());
        b.lblEmptyConf.setOnClickListener(v -> showFoodDialog());

        listAdapter = new ConfigurationFragmentAdapter(vm);

        b.lstConf.setHasFixedSize(true);
        b.lstConf.setLayoutManager(new GridLayoutManager(requireContext(), 1)); //TODO: considerar si poner 2 columnas en landscape
        b.lstConf.setItemAnimator(new DefaultItemAnimator());
        b.lstConf.setAdapter(listAdapter);

        vm.getWeeks(mainVM.getIdUserToken().getValue());
        setupSwipeToRefresh();
    }

    private void setupSwipeToRefresh() {
        b.swRefreshConfiguration.setOnRefreshListener(() -> vm.getWeeks(mainVM.getIdUserToken().getValue()));
        b.swRefreshConfiguration.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void navigateToWeek(Event<Week> weekEvent) {
        if (!weekEvent.hasBeenHandled()) {
            ConfigurationFragmentDirections.DesConfigurationToWeekConf action = ConfigurationFragmentDirections.desConfigurationToWeekConf(weekEvent.peekContent().getIdUsuario(), weekEvent.getContentIfNotHandled().getIdSemana(), weekEvent.peekContent().getNombreSemana());
            Navigation.findNavController(requireView()).navigate(action);
        }
    }

    private void updateList() {
        vm.getWeekListLiveData().observe(getViewLifecycleOwner(), weeks -> {
            Collections.sort(weeks);
            listAdapter.submitList(weeks);
            b.lblEmptyConf.setVisibility(weeks.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            b.swRefreshConfiguration.setRefreshing(false);
        });
    }

    private void showFoodDialog() {
        CustomLayoutDialogFragment.setData(R.string.config_layout_dialog_title,
                R.string.config_layout_dialog_positiveButton, R.string.config_layout_dialog_negativeButton);
        CustomLayoutDialogFragment.setListener(this);
        (new CustomLayoutDialogFragment()).show(requireActivity().getSupportFragmentManager(),
                "CustomLayoutDialogFragment");
    }

    private void showFoodEditDialog() {
        CustomLayoutDialogFragment.setData(R.string.config_layout_dialog_title,
                R.string.config_edit_layout_dialog_positiveButton, R.string.layout_dialog_negativeButton);
        if (!Objects.requireNonNull(vm.getWeekEditTrigger().getValue()).hasBeenHandled()) {
            CustomLayoutDialogFragment.setListener(this);
            (new CustomLayoutDialogFragment()).show(requireActivity().getSupportFragmentManager(),
                    "CustomLayoutDialogFragment");
        }
    }

    private void showMessage(Event<Week> week) {
        if (!week.hasBeenHandled()) {
            SnackbarUtils.snackbar(requireView(), week.getContentIfNotHandled().getNombreSemana().concat(" created!"), Snackbar.LENGTH_SHORT);
        }
    }

    private void showError(Event<String> stringEvent) {
        if (!stringEvent.hasBeenHandled()) {
            SnackbarUtils.snackbar(requireView(), stringEvent.getContentIfNotHandled(), Snackbar.LENGTH_SHORT);
        }
    }

    private void showDeleted(Event<Week> weekEvent) {
        if (!weekEvent.hasBeenHandled()) {
            vm.deleteStretch(weekEvent.peekContent(), "LA");
            vm.deleteStretch(weekEvent.peekContent(), "MA");
            vm.deleteStretch(weekEvent.peekContent(), "XA");
            vm.deleteStretch(weekEvent.peekContent(), "JA");
            vm.deleteStretch(weekEvent.peekContent(), "VA");
            vm.deleteStretch(weekEvent.peekContent(), "SA");
            vm.deleteStretch(weekEvent.peekContent(), "DA");

            vm.deleteStretch(weekEvent.peekContent(), "LC");
            vm.deleteStretch(weekEvent.peekContent(), "MC");
            vm.deleteStretch(weekEvent.peekContent(), "XC");
            vm.deleteStretch(weekEvent.peekContent(), "JC");
            vm.deleteStretch(weekEvent.peekContent(), "VC");
            vm.deleteStretch(weekEvent.peekContent(), "SC");
            vm.deleteStretch(weekEvent.peekContent(), "DC");

            //  Para resolver el problema de las dependencias en la base de datos,
            //  al ser DELETE on CASCADE, el primer borrado borra las dependencias en los hijos y el
            //  segundo borrado, borra la entidad padre en sí
            vm.deleteWeek(weekEvent.peekContent());
            vm.deleteWeek(weekEvent.peekContent());
            SnackbarUtils.snackbar(requireView(), weekEvent.getContentIfNotHandled().getNombreSemana().concat(" deleted"), Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onAddClick(DialogFragment dialog) {
        EditText configurationName = Objects.requireNonNull(dialog.getDialog()).findViewById(R.id.txtInput);

        if (vm.getWeekEditTrigger().getValue() == null || vm.getWeekEditTrigger().getValue().hasBeenHandled()) {
            vm.addWeek(mainVM.getIdUserToken().getValue(), configurationName.getText().toString());
        } else {
            Week week = vm.getWeekEditTrigger().getValue().getContentIfNotHandled();
            week.setNombreSemana(configurationName.getText().toString());
            vm.editWeek(week);
            showEdited(week);
        }
    }

    private void showEdited(Week week) {
        SnackbarUtils.snackbar(requireView(), week.getNombreSemana().concat(" renamed"), Snackbar.LENGTH_SHORT);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelClick(DialogFragment dialog) {
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
                (AppBarLayout.LayoutParams) b.confToolbar.getLayoutParams();
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
