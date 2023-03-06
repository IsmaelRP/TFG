package project.tfg.ecgscan.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.base.PhoneDialog;
import project.tfg.ecgscan.base.PhoneNumberDialog;
import project.tfg.ecgscan.databinding.FragmentLoginBinding;
import project.tfg.ecgscan.ui.home.OnButtonClickListener;
import project.tfg.ecgscan.ui.mainActivity.MainActivityViewModel;
import project.tfg.ecgscan.ui.secondActivity.SecondActivity;
import project.tfg.ecgscan.utils.KeyboardUtils;
import project.tfg.ecgscan.utils.SnackbarUtils;

public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN_GOOGLE = 123;

    private NavController navController;
    private FirebaseAuth mAuth;
    private FragmentLoginBinding b;

    private CallbackManager callbackManager;

    private String phoneNumber;
    private String phoneCode;
    private MainActivityViewModel mainVM;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFacebook();
        setupFirebase();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    private void functionMatlab() {


    }

    private void setupFacebook() {

        FacebookSdk.sdkInitialize(requireContext());
        AppEventsLogger.activateApp(requireActivity());
        callbackManager = CallbackManager.Factory.create();
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
        //mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
    }

    private void setupViews(View view) {
        mainVM = ViewModelProviders.of(requireActivity()).get(MainActivityViewModel.class);

        navController = Objects.requireNonNull(Navigation.findNavController(view));
        b.txtPassword.setOnEditorActionListener((v, actionId, event) -> {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            login();
            return true;
        });
        b.btnLogin.setOnClickListener(v -> login());

        b.btnAnnonymous.setOnClickListener(v -> loginAnonymous());
        b.btnGoogle.setOnClickListener(v -> loginGoogle());
        b.btnPhone.setOnClickListener(v -> loginPhoneNumber());
        //b.btnFacebook.setOnClickListener(v -> loginFacebook(view));
        setupFacebookButton();
    }

    private void setupFacebookButton() {
        b.btnFacebook.setReadPermissions("email", "public_profile");
        b.btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Inicio de sesión exitoso, autenticar con Firebase
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Inicio de sesión con Firebase exitoso, navegar a la pantalla principal
                                startActivity(new Intent(getActivity(), SecondActivity.class));
                                getActivity().finish();
                            } else {
                                // Fallo en el inicio de sesión con Firebase, mostrar mensaje de error
                                Toast.makeText(getActivity(), "Error al iniciar sesión con Firebase", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancel() {
                // Cancelación del inicio de sesión con Facebook, mostrar mensaje de cancelación
                Toast.makeText(getActivity(), "Inicio de sesión con Facebook cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                // Error en el inicio de sesión con Facebook, mostrar mensaje de error
                Toast.makeText(getActivity(), "Error al iniciar sesión con Facebook: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loginPhoneNumber() {
        PhoneNumberDialog phoneNumberDialog = PhoneNumberDialog.newInstance();
        phoneNumberDialog.setOnClickListener(new OnButtonClickListener() {
            @Override
            public void onButtonClick(String title) {
                phoneNumber = "+34" + title;
                sendVerificationCode(phoneNumber);
                mainVM.setPhoneLogin(true);

                PhoneDialog phoneDialoglog = PhoneDialog.newInstance();
                phoneDialoglog.setOnClickListener(new OnButtonClickListener() {
                    @Override
                    public void onButtonClick(String title) {
                        verifyCode(title);
                    }
                });
                phoneDialoglog.show(getChildFragmentManager(), "PhoneDialog");
            }
        });
        phoneNumberDialog.show(getChildFragmentManager(), "PhoneNumberDialog");




    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private PhoneAuthCredential phoneCredential;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            phoneCode = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            phoneCredential = phoneAuthCredential;

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            int a = 1;
            //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        if(phoneCode == null){
            // error el codigo otp lo ha puesto mal hijo puta
            //Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
        }else{


        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneCode, code);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginSuccessfull();

                        } else {
                            //Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

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

     */

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
