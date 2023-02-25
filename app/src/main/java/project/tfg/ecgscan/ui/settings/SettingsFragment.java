package project.tfg.ecgscan.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.data.Event;
import project.tfg.ecgscan.ui.secondActivity.SecondActivityViewModel;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SecondActivityViewModel vm;
    private final SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = this;
    private NavController navController;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.splashbg));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view) {
        navController = Objects.requireNonNull(Navigation.findNavController(view));
        vm = ViewModelProviders.of(requireActivity()).get(SecondActivityViewModel.class);
        vm.getNavigateToList().observe(getViewLifecycleOwner(), this::navigateToTabs);
    }

    private void navigateToTabs(Event<Boolean> booleanEvent) {
        if (booleanEvent.getContentIfNotHandled() != null){
            navController.navigate(SettingsFragmentDirections.desSettingsToTabs());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        super.onPause();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        vm.setPreferencesCloud(sharedPreferences.getBoolean(getString(R.string.preferencesCloud), false));
    }

}
