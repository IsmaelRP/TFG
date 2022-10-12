package project.tfg.ecgscan.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import project.tfg.ecgscan.databinding.FragmentRegisterBinding;
import project.tfg.ecgscan.utils.KeyboardUtils;
import project.tfg.ecgscan.utils.SnackbarUtils;

public class RegisterFragment extends Fragment {

    private NavController navController;
    private FirebaseAuth mAuth;
    private FragmentRegisterBinding b;

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

    private void setupViews(View view) {
        navController = Objects.requireNonNull(Navigation.findNavController(view));
        b.txtPassword.setOnEditorActionListener((v, actionId, event) -> {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            registerWithFirebase(b.txtEmail.getText().toString(), b.txtPassword.getText().toString());
            return true;
        });
        b.btnRegister.setOnClickListener(v -> registerWithFirebase(b.txtEmail.getText().toString(), b.txtPassword.getText().toString()));
    }

    private void setupFirebase() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    private void registerWithFirebase(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                requireActivity().finish();
                navController.navigate(RegisterFragmentDirections.desRegisterToSecond());
            } else {
                SnackbarUtils.snackbar(getView(), "Error at register", 600);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentRegisterBinding.inflate(inflater, container, false);
        return b.getRoot();
    }


}
