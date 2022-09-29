package com.practica.ismael.foodcal.ui.foodSelection;

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
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.FoodSelectionFragmentBinding;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;
import com.practica.ismael.foodcal.ui.main_activity.ToolbarConf;
import com.practica.ismael.foodcal.utils.KeyboardUtils;

import java.util.Collections;

public class FoodSelectionFragment extends Fragment {

    private FoodSelectionFragmentViewModel vm;
    private FoodSelectionFragmentBinding b;
    private ToolbarConf toolbarConf;
    private FoodSelectionFragmentAdapter listAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.food_selection_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setupViews() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.foodSelectionToolbar);
        toolbarConf.onConfigure(b.foodSelectionToolbar);

        vm = ViewModelProviders.of(this,
                new FoodSelectionFragmentViewModelFactory(ApiService.getInstance().getApi())).get(FoodSelectionFragmentViewModel.class);

        MainActivityViewModel mainVM = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        vm.getFoodListLiveData().observe(getViewLifecycleOwner(), v -> updateList());
        vm.getFoods(mainVM.getIdUserToken().getValue());

        listAdapter = new FoodSelectionFragmentAdapter(mainVM);

        b.lstSelectionFoods.setHasFixedSize(true);
        b.lstSelectionFoods.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        b.lstSelectionFoods.setItemAnimator(new DefaultItemAnimator());
        b.lstSelectionFoods.setAdapter(listAdapter);

        mainVM.getFoodSelected().observe(getViewLifecycleOwner(), this::saveAndBack);
    }

    private void saveAndBack(Event<Food> foodEvent) {
        if (!foodEvent.hasBeenHandled()){
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
                (AppBarLayout.LayoutParams) b.foodSelectionToolbar.getLayoutParams();
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
