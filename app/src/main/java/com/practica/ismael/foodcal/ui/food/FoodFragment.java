package com.practica.ismael.foodcal.ui.food;

import android.content.Context;
import android.graphics.Color;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.FoodFragmentBinding;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;
import com.practica.ismael.foodcal.ui.main_activity.ToolbarConf;
import com.practica.ismael.foodcal.utils.CustomLayoutDialogFragment;
import com.practica.ismael.foodcal.utils.KeyboardUtils;
import com.practica.ismael.foodcal.utils.SnackbarUtils;

import java.util.Collections;
import java.util.Objects;

public class FoodFragment extends Fragment implements CustomLayoutDialogFragment.Listener {

    private FoodFragmentBinding b;
    private FoodFragmentViewModel vm;
    private MainActivityViewModel mainVM;
    private FoodFragmentAdapter listAdapter;
    private ToolbarConf toolbarConf;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.food_fragment, container, false);
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

    private void setupViews() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.foodToolbar);
        toolbarConf.onConfigure(b.foodToolbar);

        vm = ViewModelProviders.of(this,
                new FoodFragmentViewModelFactory(ApiService.getInstance().getApi())).get(FoodFragmentViewModel.class);

        mainVM = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

        vm.getFoodLiveData().observe(getViewLifecycleOwner(), this::showMessage);
        vm.getErrorTrigger().observe(getViewLifecycleOwner(), this::showError);
        vm.getFoodListLiveData().observe(getViewLifecycleOwner(), v -> updateList());
        vm.getFoodDeletedLiveData().observe(getViewLifecycleOwner(), this::showDeleted);
        vm.getFoodEditTrigger().observe(getViewLifecycleOwner(), v -> showFoodEditDialog());
        vm.getFoodConfirmDeletion().observe(getViewLifecycleOwner(), this::showMsgDelete);

        b.foodFab.setOnClickListener(v -> showFoodDialog());
        b.lblEmptyFoods.setOnClickListener(v -> showFoodDialog());

        listAdapter = new FoodFragmentAdapter(vm);

        b.lstFoods.setHasFixedSize(true);
        b.lstFoods.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        b.lstFoods.setItemAnimator(new DefaultItemAnimator());
        b.lstFoods.setAdapter(listAdapter);

        vm.getFoods(mainVM.getIdUserToken().getValue());

        setupSwipeToRefresh();
    }

    private void setupSwipeToRefresh() {
        b.swRefreshFoods.setOnRefreshListener(() -> vm.getFoods(mainVM.getIdUserToken().getValue()));
        b.swRefreshFoods.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void showDeleted(Event<Food> foodEvent) {
        if (!foodEvent.hasBeenHandled()) {
            vm.deleteFood(foodEvent.peekContent());
        }
    }

    private void showMsgDelete(Event<Food> foodEvent){
        if (!foodEvent.hasBeenHandled()){
            if (foodEvent.getContentIfNotHandled() == null){
                SnackbarUtils.snackbar(requireView(), "You can't delete the food if it's being used!", Snackbar.LENGTH_SHORT);
            }else{
                Snackbar snackbar = Snackbar
                        .make(b.foodCoordinatorLayout, foodEvent.peekContent().getNombrecomida().concat(" deleted"), Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> listAdapter.restoreItem());
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
        }

    }

    private void updateList() {
        vm.getFoodListLiveData().observe(getViewLifecycleOwner(), foods -> {
            Collections.sort(foods);
            listAdapter.submitList(foods);
            b.lblEmptyFoods.setVisibility(foods.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            b.swRefreshFoods.setRefreshing(false);
        });
    }

    private void showError(Event<String> stringEvent) {
        if (!stringEvent.hasBeenHandled()) {
            SnackbarUtils.snackbar(requireView(), stringEvent.getContentIfNotHandled(), Snackbar.LENGTH_SHORT);
        }
    }

    private void showMessage(Event<Food> food) {
        if (!food.hasBeenHandled()) {
            SnackbarUtils.snackbar(requireView(), food.getContentIfNotHandled().getNombrecomida().concat(" added!"), Snackbar.LENGTH_SHORT);
        }
    }

    private void showEdited(Food food) {
        SnackbarUtils.snackbar(requireView(), food.getNombrecomida().concat(" renamed"), Snackbar.LENGTH_SHORT);
        listAdapter.notifyDataSetChanged();
    }

    private void showFoodEditDialog() {
        CustomLayoutDialogFragment.setData(R.string.food_layout_dialog_title,
                R.string.config_edit_layout_dialog_positiveButton, R.string.layout_dialog_negativeButton);
        if (!Objects.requireNonNull(vm.getFoodEditTrigger().getValue()).hasBeenHandled()) {
            CustomLayoutDialogFragment.setListener(this);
            (new CustomLayoutDialogFragment()).show(requireActivity().getSupportFragmentManager(),
                    "CustomLayoutDialogFragment");
        }
    }

    private void showFoodDialog() {
        CustomLayoutDialogFragment.setData(R.string.food_layout_dialog_title,
                R.string.food_layout_dialog_positiveButton, R.string.layout_dialog_negativeButton);
        CustomLayoutDialogFragment.setListener(this);
        (new CustomLayoutDialogFragment()).show(requireActivity().getSupportFragmentManager(),
                "CustomLayoutDialogFragment");
    }

    @Override
    public void onAddClick(DialogFragment dialog) {
        EditText foodName = Objects.requireNonNull(dialog.getDialog()).findViewById(R.id.txtInput);
        if (vm.getFoodEditTrigger().getValue() == null || vm.getFoodEditTrigger().getValue().hasBeenHandled()) {
            Food a = new Food(0, mainVM.getIdUserToken().getValue(), foodName.getText().toString());
            vm.addFood(a);
        } else {
            Food food = vm.getFoodEditTrigger().getValue().getContentIfNotHandled();
            food.setNombrecomida(foodName.getText().toString());
            vm.editFood(food);
            showEdited(food);
        }
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
                (AppBarLayout.LayoutParams) b.foodToolbar.getLayoutParams();
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
