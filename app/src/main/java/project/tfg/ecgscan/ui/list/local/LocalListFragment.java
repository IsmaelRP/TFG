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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.data.RepositoryImpl;
import project.tfg.ecgscan.data.local.AppDatabase;
import project.tfg.ecgscan.databinding.FragmentListBinding;

public class LocalListFragment extends Fragment {

    private FragmentListBinding b;
    private LocalListFragmentViewModel vm;
    private Toolbar toolbar;
    private LocalListFragmentAdapter listAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentListBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        vm = ViewModelProviders.of(this,
                        new LocalListFragmentViewModelFactory(requireActivity().getApplication(), new RepositoryImpl(
                                AppDatabase.getInstance(requireContext().getApplicationContext()).electroDao())))
                .get(LocalListFragmentViewModel.class);

        listAdapter = new LocalListFragmentAdapter(vm);

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

        observeCompanies();
    }


    private void observeCompanies() {
        vm.getElectros().observe(getViewLifecycleOwner(), electros -> {
            listAdapter.submitList(electros);
            b.lblEmptyList.setVisibility(electros.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        });
    }

    public static LocalListFragment newInstance() {
        return new LocalListFragment();
    }



}
