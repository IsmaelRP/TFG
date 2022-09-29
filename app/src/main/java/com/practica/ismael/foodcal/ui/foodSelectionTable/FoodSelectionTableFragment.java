package com.practica.ismael.foodcal.ui.foodSelectionTable;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Contains;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.FoodSelectionTableFragmentBinding;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;
import com.practica.ismael.foodcal.ui.main_activity.ToolbarConf;
import com.practica.ismael.foodcal.utils.KeyboardUtils;

import java.util.Collections;
import java.util.Objects;

public class FoodSelectionTableFragment extends Fragment {

    private MainActivityViewModel mainVM;
    private FoodSelectionTableFragmentViewModel vm;
    private FoodSelectionTableFragmentBinding b;
    private ToolbarConf toolbarConf;
    private FoodSelectionTableFragmentAdapter listAdapter;

    private String date;
    private int idCalendario;
    private String tipoComida = "Almuerzo";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.food_selection_table_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getArguments());

        date = FoodSelectionTableFragmentArgs.fromBundle(getArguments()).getDate();
        idCalendario = FoodSelectionTableFragmentArgs.fromBundle(getArguments()).getIdCalendario();

        setupViews();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setupViews() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.foodSelectionTableToolbar);
        toolbarConf.onConfigure(b.foodSelectionTableToolbar);

        vm = ViewModelProviders.of(this,
                new FoodSelectionTableFragmentViewModelFactory(ApiService.getInstance().getApi())).get(FoodSelectionTableFragmentViewModel.class);

        mainVM = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        vm.getFoodListLiveData().observe(getViewLifecycleOwner(), v -> updateList());
        vm.getFoods(mainVM.getIdUserToken().getValue());

        listAdapter = new FoodSelectionTableFragmentAdapter(mainVM);

        b.lstSelectionFoods.setHasFixedSize(true);
        b.lstSelectionFoods.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        b.lstSelectionFoods.setItemAnimator(new DefaultItemAnimator());
        b.lstSelectionFoods.setAdapter(listAdapter);

        mainVM.getFoodTableSelected().observe(getViewLifecycleOwner(), this::saveAndBack);

        b.rdgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == b.rdbLunch.getId()){
                tipoComida = "Almuerzo";
            }else{
                tipoComida = "Cena";
            }
        });
    }

    private void saveAndBack(Event<Food> foodEvent) {
        if (!foodEvent.hasBeenHandled()){
            vm.insertData(new Contains(idCalendario, mainVM.getIdUserToken().getValue(), date, foodEvent.getContentIfNotHandled().getIdcomida(), tipoComida));
            requireActivity().onBackPressed();
        }
    }

    private void updateList() {
        vm.getFoodListLiveData().observe(getViewLifecycleOwner(), foods -> {
            Collections.sort(foods);
            listAdapter.submitList(foods);
            b.lblEmptySelectionFoods.setVisibility(foods.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        });
    }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        toolbarConf = (ToolbarConf) context;
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
                (AppBarLayout.LayoutParams) b.foodSelectionTableToolbar.getLayoutParams();
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
