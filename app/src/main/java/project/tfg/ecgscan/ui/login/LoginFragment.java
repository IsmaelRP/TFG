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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.databinding.FragmentLoginBinding;
import project.tfg.ecgscan.databinding.FragmentStartBinding;
import project.tfg.ecgscan.ui.register.RegisterFragmentDirections;
import project.tfg.ecgscan.ui.start.StartFragmentDirections;
import project.tfg.ecgscan.utils.KeyboardUtils;
import project.tfg.ecgscan.utils.SnackbarUtils;

public class LoginFragment extends Fragment {

    private NavController navController;
    private FirebaseAuth mAuth;
    private FragmentLoginBinding b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFirebase();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentLoginBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    private void setupFirebase() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupViews(View view) {
        navController = Objects.requireNonNull(Navigation.findNavController(view));
        b.txtPassword.setOnEditorActionListener((v, actionId, event) -> {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            login();
            return true;
        });
        b.btnLogin.setOnClickListener(v -> login());
    }

    private void login(){
        mAuth.signInWithEmailAndPassword(b.txtEmail.getText().toString(), b.txtPassword.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                requireActivity().finish();
                navController.navigate(LoginFragmentDirections.desLoginToSecond());
            } else {
                SnackbarUtils.snackbar(getView(), "Error at login", 600);
            }
        });
    }

}
