package project.tfg.ecgscan.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.Objects;
import project.tfg.ecgscan.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

    private NavController navController;
    private FragmentListBinding b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentListBinding.inflate(inflater, container, false);
        return b.getRoot();
    }


    private void setupViews(View view) {
        navController = Objects.requireNonNull(Navigation.findNavController(view));
    }


}
