package project.tfg.ecgscan.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.databinding.FragmentLoginBinding;
import project.tfg.ecgscan.utils.KeyboardUtils;
import project.tfg.ecgscan.utils.SnackbarUtils;

public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN_GOOGLE = 123;

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

        b.btnAnnonymous.setOnClickListener(v -> loginAnonymous());
        b.btnGoogle.setOnClickListener(v -> loginGoogle());
    }

    private void loginGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RC_SIGN_IN_GOOGLE) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);

                String pepe = account.getIdToken();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity(), taskLambda -> {
                    if (taskLambda.isSuccessful()){
                        loginSuccessfull();
                    }else{
                        SnackbarUtils.snackbar(getView(), "Error at login", 600);
                    }
                });

            }

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void loginAnonymous() {

        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginSuccessfull();
                        } else {
                            // If sign in fails, display a message to the user.
                            SnackbarUtils.snackbar(getView(), "Error at login", 600);
                        }
                    }
                });

    }

    private void login() {
        mAuth.signInWithEmailAndPassword(b.txtEmail.getText().toString(), b.txtPassword.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginSuccessfull();
            } else {
                SnackbarUtils.snackbar(getView(), "Error at login", 600);
            }
        });
    }

    private void loginSuccessfull() {
        requireActivity().finish();
        navController.navigate(LoginFragmentDirections.desLoginToSecond());
    }

}
