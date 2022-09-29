package com.practica.ismael.foodcal.ui.login_activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.practica.ismael.foodcal.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements OnDestroy {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
        setupNavigationGraph();
    }

    private void setupViews() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> setTitle(destination.getLabel()));

    }

    private void setupNavigationGraph() {
        int startDestinationResId;
        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(
                        R.id.navHostFragment);
        NavController navController = Objects.requireNonNull(navHost).getNavController();
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.login_navigation);

        startDestinationResId = R.id.loginFragment;

        navGraph.setStartDestination(startDestinationResId);
        navController.setGraph(navGraph);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void clearBackStack() {
        finish();
    }
}
