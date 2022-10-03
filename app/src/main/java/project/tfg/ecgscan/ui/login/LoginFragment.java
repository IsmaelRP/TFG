package project.tfg.ecgscan.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.databinding.FragmentLoginBinding;
import project.tfg.ecgscan.databinding.FragmentStartBinding;
import project.tfg.ecgscan.ui.start.StartFragmentDirections;

public class LoginFragment extends Fragment {

    private NavController navController;
    private FragmentLoginBinding b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentLoginBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view) {
        navController = Objects.requireNonNull(Navigation.findNavController(view));

        b.btnLogin.setOnClickListener(v -> {
            getActivity().finish();
            navController.navigate(LoginFragmentDirections.desLoginToSecond());
        });
    }



}
