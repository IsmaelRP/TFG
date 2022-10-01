package project.tfg.ecgscan.ui.mainActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import project.tfg.ecgscan.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_ECGScan);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setupNavigationGraph();

    }


    private void setupViews() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> setTitle(destination.getLabel()));
    }

    private void setupNavigationGraph() {
        int startDestinationResId;
        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(
                        R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHost).getNavController();
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.navigation_graph);

        startDestinationResId = R.id.fragmentStart;

        navGraph.setStartDestination(startDestinationResId);
        navController.setGraph(navGraph);
    }

}
