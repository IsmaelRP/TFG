package com.practica.ismael.foodcal.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.User;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.LoginFragmentBinding;
import com.practica.ismael.foodcal.ui.login_activity.LoginActivityViewModel;
import com.practica.ismael.foodcal.ui.login_activity.OnDestroy;
import com.practica.ismael.foodcal.utils.KeyboardUtils;
import com.practica.ismael.foodcal.utils.SnackbarUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    private LoginFragmentBinding b;
    private NavController navController;
    private LoginFragmentViewModel vm;
    private LoginActivityViewModel mainVM;
    private final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        checkKey();
    }

    private void checkKey() {
        String KEY_PATH = "token.txt";
        File file = new File(requireActivity().getFilesDir(), KEY_PATH);
        if (file.exists()) {    //  Check if the user is logged
            Log.i("Token", "exist");

            FileInputStream fileInput;
            try {
                fileInput = requireActivity().openFileInput(KEY_PATH);
                InputStreamReader input = new InputStreamReader(fileInput);
                BufferedReader bufferedReader = new BufferedReader(input);
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                mainVM.setIdUserToken(stringBuilder.toString());
            } catch (FileNotFoundException e) {
                Log.e("Token", "file not found");
            } catch (IOException e) {
                Log.e("Token", "input/output error");
            }

            navigateToMain(mainVM.getIdUserToken().getValue());
        } else {
            Log.i("Bearer Token", "doesn't exist");
        }
    }

    private void setupViews() {
        vm = ViewModelProviders.of(this,
                new LoginFragmentViewModelFactory(ApiService.getInstance().getApi())).get(LoginFragmentViewModel.class);     //  Obtain LoginFragmentViewModel through LoginFragmentViewModelFactory

        mainVM = ViewModelProviders.of(requireActivity()).get(LoginActivityViewModel.class);

        mainVM.getUserRegisterLiveData().observe(getViewLifecycleOwner(), this::showRegistered);
        setupObservers();
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

        b.btnLogin.setOnClickListener(v -> login());
        b.btnRegister.setOnClickListener(v -> navController.navigate(LoginFragmentDirections.desLoginToRegister()));
        b.txtPassword.setOnEditorActionListener((v, actionId, event) -> {
            login();
            return true;
        });

        vm.getEyeVisibility().observe(getViewLifecycleOwner(), this::updateEye);
        b.imgPassword.setOnClickListener(v -> showHidePassword());
    }

    private void updateEye(Boolean visible) {
        if (visible) {
            b.imgPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_black_24dp));
            b.txtPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            b.imgPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_off_black_24dp));
            b.txtPassword.setTransformationMethod(null);
        }
    }

    private void showHidePassword() {
        vm.setEyeVisibility(!vm.getEyeVisibility().getValue());
    }

    private void showRegistered(Event<User> userEvent) {
        if (!userEvent.hasBeenHandled()) {
            userEvent.getContentIfNotHandled();
            SnackbarUtils.snackbar(requireView(), getString(R.string.successfullyRegistered), Snackbar.LENGTH_LONG);
        }
    }

    private void setupObservers() {
        vm.getUserResponseLiveData().observe(getViewLifecycleOwner(), this::checkResponse);
        vm.getLoading().observe(getViewLifecycleOwner(), this::showLoading);
        vm.getErrorTrigger().observe(getViewLifecycleOwner(), this::showError);
    }

    private void showError(Event<String> stringEvent) {
        if (!stringEvent.hasBeenHandled()) {
            String s;
            if (stringEvent.getContentIfNotHandled().equals("Wrong credentials")) {
                s = getString(R.string.errorCredentials);
            } else {
                s = getString(R.string.serverDown);
            }
            SnackbarUtils.snackbar(requireView(), s, Snackbar.LENGTH_SHORT);
        }
    }

    private void showLoading(Boolean loading) {
        if (loading) {
            b.pgbar.setVisibility(View.VISIBLE);
        } else {
            b.pgbar.setVisibility(View.INVISIBLE);
        }
    }

    private void checkResponse(Event<User> userEvent) {
        if (!userEvent.hasBeenHandled() && userEvent.peekContent() != null) {
            userEvent.getContentIfNotHandled();
            setupTokenFile();
            navigateToMain(userEvent.peekContent().getEmailUsuario());
        }
    }

    private void login() {
        KeyboardUtils.hideSoftKeyboard(requireActivity());
        if (fieldsAreCorrect()) {
            vm.login(new User(b.txtEmail.getText().toString(), "", b.txtPassword.getText().toString()));
        } else {
            b.txtEmail.addTextChangedListener(new GenericTextWatcher(b.txtEmail));
            b.txtPassword.addTextChangedListener(new GenericTextWatcher(b.txtPassword));
            SnackbarUtils.snackbar(requireView(), getString(R.string.errorFields), Snackbar.LENGTH_SHORT);
        }
    }

    private boolean fieldsAreCorrect() {
        boolean flag = true;
        if (!EMAIL_ADDRESS_PATTERN.matcher(b.txtEmail.getText()).matches()) {
            flag = false;
            b.tilEmail.setErrorEnabled(true);
            b.tilEmail.setError(getString(R.string.error_email));
        }
        if (b.txtPassword.getText().toString().isEmpty()) {
            flag = false;
            b.tilPassword.setErrorEnabled(true);
            b.tilPassword.setError(getString(R.string.error_pass));
        }
        return flag;
    }

    private void navigateToMain(String idUsuario) {
        KeyboardUtils.hideSoftKeyboard(requireActivity());
        navController.navigate(LoginFragmentDirections.desLoginToMainActivity(idUsuario));
        ((OnDestroy) requireActivity()).clearBackStack();
    }

    private void setupTokenFile() {
        FileOutputStream writer;    //  To store the Key, so the user doesn't have to log in every time he enter in the App
        try {
            String KEYPATH = "token.txt";
            File file = new File(requireContext().getFilesDir(), KEYPATH);
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            writer = requireActivity().openFileOutput(KEYPATH, Context.MODE_PRIVATE);
            writer.write(b.txtEmail.getText().toString().getBytes());
            writer.close();
        } catch (Exception e) {
            Log.e("Token", e.getMessage(), e);
        }
    }

    private class GenericTextWatcher implements TextWatcher {

        private final View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txtEmail:
                    setErrorEmail();
                    break;
                case R.id.txtPassword:
                    setErrorPass();
                    break;
            }
        }

        private void setErrorPass() {
            if (!b.txtPassword.getText().toString().isEmpty()) {
                b.tilPassword.setError("");
                b.tilPassword.setErrorEnabled(false);
            } else {
                b.tilPassword.setError(getString(R.string.error_pass));
                b.tilPassword.setErrorEnabled(true);
            }
        }

        private void setErrorEmail() {
            if (EMAIL_ADDRESS_PATTERN.matcher(b.txtEmail.getText().toString()).matches()) {
                b.tilEmail.setError("");
                b.tilEmail.setErrorEnabled(false);
            } else {
                b.tilEmail.setError(getString(R.string.error_email));
                b.tilEmail.setErrorEnabled(true);
            }
        }
    }
}