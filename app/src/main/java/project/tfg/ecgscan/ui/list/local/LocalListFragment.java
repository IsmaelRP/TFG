package project.tfg.ecgscan.ui.list.local;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.data.ElectroImage;
import project.tfg.ecgscan.data.Event;
import project.tfg.ecgscan.data.RepositoryImpl;
import project.tfg.ecgscan.data.local.AppDatabase;
import project.tfg.ecgscan.databinding.FragmentListBinding;
import project.tfg.ecgscan.ui.secondActivity.SecondActivityViewModel;

public class LocalListFragment extends Fragment {

    private FragmentListBinding b;
    private SecondActivityViewModel secondVM;
    private LocalListFragmentViewModel vm;
    private Toolbar toolbar;
    private LocalListFragmentAdapter listAdapter;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentListBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    public LocalListFragment(NavController navController) {
        this.navController = navController;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view) {
        secondVM = ViewModelProviders.of(requireActivity()).get(SecondActivityViewModel.class);

        vm = ViewModelProviders.of(this,
                        new LocalListFragmentViewModelFactory(requireActivity().getApplication(), new RepositoryImpl(
                                AppDatabase.getInstance(requireContext().getApplicationContext()).electroDao())))
                .get(LocalListFragmentViewModel.class);

        listAdapter = new LocalListFragmentAdapter(secondVM, vm);

        b.lstList.setHasFixedSize(true);
        b.lstList.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.lstList.setItemAnimator(new DefaultItemAnimator());
        b.lstList.setAdapter(listAdapter);


        toolbar = Objects.requireNonNull(requireActivity().findViewById(R.id.main_toolbar));

        SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return true;
            }
        });

        b.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                observeCompanies();
            }
        });

        observeCompanies();

        secondVM.getElectroObservable().observe(getViewLifecycleOwner(), this::goToHome);
    }

    private void goToHome(Event<ElectroImage> electroImageEvent) {
        if (!electroImageEvent.hasBeenHandled()){
            electroImageEvent.getContentIfNotHandled();

            if(!navController.getCurrentDestination().getDisplayName().contains("home")){
                requireActivity().onBackPressed();
            }
        }
    }


    private void observeCompanies() {
        vm.getElectros().observe(getViewLifecycleOwner(), electros -> {
            listAdapter.submitList(electros);
            b.lblEmptyList.setVisibility(electros.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            b.swipeRefreshLayout.setRefreshing(false);
        });
    }

    public static LocalListFragment newInstance(NavController navController) {
        return new LocalListFragment(navController);
    }



}
