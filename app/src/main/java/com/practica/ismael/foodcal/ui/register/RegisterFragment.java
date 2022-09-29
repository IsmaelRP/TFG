package com.practica.ismael.foodcal.ui.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.base.Event;
import com.practica.ismael.foodcal.data.model.User;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.databinding.RegisterFragmentBinding;
import com.practica.ismael.foodcal.ui.login_activity.LoginActivityViewModel;
import com.practica.ismael.foodcal.utils.KeyboardUtils;
import com.practica.ismael.foodcal.utils.SnackbarUtils;

import java.util.Objects;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class RegisterFragment extends Fragment {

    private RegisterFragmentBinding b;
    private RegisterFragmentViewModel vm;
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
        b = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        mainVM = ViewModelProviders.of(requireActivity()).get(LoginActivityViewModel.class);
        vm = ViewModelProviders.of(this,
                new RegisterFragmentViewModelFactory(ApiService.getInstance().getApi())).get(RegisterFragmentViewModel.class);

        vm.getLoginLiveData().observe(getViewLifecycleOwner(), this::registerSuccessfully);
        vm.getErrorTrigger().observe(getViewLifecycleOwner(), this::showError);
        //TODO: put loading screen while it tries to login

        setupToolbar();
        b.btnCancel.setOnClickListener(v -> Objects.requireNonNull(getFragmentManager()).popBackStack());
        b.btnRegister.setOnClickListener(v -> register());
        b.txtConfirmPassword.setOnEditorActionListener((v, actionId, event) -> {
            register();
            return true;
        });
    }

    private void showError(Event<String> stringEvent) {
        if (!stringEvent.hasBeenHandled()) {
            stringEvent.getContentIfNotHandled();
            SnackbarUtils.snackbar(requireView(), "Email already in use", Snackbar.LENGTH_SHORT);
        }
    }

    private void registerSuccessfully(Event<User> userEvent) {
        if (!userEvent.hasBeenHandled()) {
            mainVM.setUserRegisterLiveData(userEvent.getContentIfNotHandled());
        }
        requireActivity().onBackPressed();
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.registerToolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    private void register() {
        KeyboardUtils.hideSoftKeyboard(requireActivity());
        setGenericTextWatchers();
        if (Objects.requireNonNull(b.txtName.getText()).toString().isEmpty() || Objects.requireNonNull(b.txtEmail.getText()).toString().isEmpty() || Objects.requireNonNull(b.txtPassword.getText()).toString().isEmpty() || Objects.requireNonNull(b.txtConfirmPassword.getText()).toString().isEmpty()) {
            SnackbarUtils.snackbar(requireView(), "You have to fill all the fields!", Snackbar.LENGTH_SHORT);
            setErrorEmail();
            setErrorName();
            setErrorPass();
            setErrorConfirmPass();
        } else if (!Objects.requireNonNull(b.txtPassword.getText()).toString().equals(Objects.requireNonNull(b.txtConfirmPassword.getText()).toString())) {
            SnackbarUtils.snackbar(requireView(), "Passwords mismatch!", Snackbar.LENGTH_SHORT);
        } else if (!EMAIL_ADDRESS_PATTERN.matcher(b.txtEmail.getText().toString()).matches()) {
            SnackbarUtils.snackbar(requireView(), getString(R.string.error_email), Snackbar.LENGTH_SHORT);
        } else {
            vm.register(new User(b.txtEmail.getText().toString(), b.txtName.getText().toString(), b.txtPassword.getText().toString()));
        }
    }

    private void setGenericTextWatchers() {
        b.txtEmail.addTextChangedListener(new GenericTextWatcher(b.txtEmail));
        b.txtName.addTextChangedListener(new GenericTextWatcher(b.txtName));
        b.txtPassword.addTextChangedListener(new GenericTextWatcher(b.txtPassword));
        b.txtConfirmPassword.addTextChangedListener(new GenericTextWatcher(b.txtConfirmPassword));

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
                case R.id.txtName:
                    setErrorName();
                    break;
                case R.id.txtEmail:
                    setErrorEmail();
                    break;
                case R.id.txtPassword:
                    setErrorPass();
                    break;
                case R.id.txtConfirmPassword:
                    setErrorConfirmPass();
                    break;
            }
        }

    }

    private void setErrorConfirmPass() {
        if (b.txtConfirmPassword.getText().toString().equals(b.txtPassword.getText().toString())){
            b.tilConfirmPassword.setError("");
            b.tilConfirmPassword.setErrorEnabled(false);
        }else{
            b.tilConfirmPassword.setError(getString(R.string.error_confirmPassword));
            b.tilConfirmPassword.setErrorEnabled(true);
        }
    }

    private void setErrorName() {
        if (!b.txtName.getText().toString().isEmpty()){
            b.tilName.setError("");
            b.tilName.setErrorEnabled(false);
        }else{
            b.tilName.setError(getString(R.string.error_name));
            b.tilName.setErrorEnabled(true);
        }
    }

    private void setErrorPass() {
        setErrorConfirmPass();
        if (!b.txtPassword.getText().toString().isEmpty()){
            b.tilPassword.setError("");
            b.tilPassword.setErrorEnabled(false);
        }else{
            b.tilPassword.setError(getString(R.string.error_pass));
            b.tilPassword.setErrorEnabled(true);
        }
    }

    private void setErrorEmail() {
        if (EMAIL_ADDRESS_PATTERN.matcher(b.txtEmail.getText().toString()).matches()){
            b.tilEmail.setError("");
            b.tilEmail.setErrorEnabled(false);
        }else{
            b.tilEmail.setError(getString(R.string.error_email));
            b.tilEmail.setErrorEnabled(true);
        }
    }
}
