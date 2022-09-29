package com.practica.ismael.foodcal.ui.main_activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.remote.ApiService;
import com.practica.ismael.foodcal.ui.login_activity.OnDestroy;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnDestroy, ToolbarConf {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private AppBarConfiguration appBarConfiguration;
    private MainActivityViewModel mainVM;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();

        mainVM.setIdUserToken(MainActivityArgs.fromBundle(getIntent().getExtras()).getIdusuario());

        setupNavigationGraph();
        mainVM.getIdName().observe(this, this::setupDrawerInfo);
        mainVM.getName(mainVM.getIdUserToken().getValue());
    }

    private void setupDrawerInfo(String s) {
        TextView lblUserName = navigationView.getHeaderView(0).findViewById(R.id.lblUserName);
        lblUserName.setText(s);
    }

    private void setupViews() {
        mainVM = ViewModelProviders.of(this, new MainActivityViewModelFactory(ApiService.getInstance().getApi())).get(MainActivityViewModel.class);

        navController = Navigation.findNavController(this, R.id.navHostFragment);

        navigationView = ActivityCompat.requireViewById(this, R.id.mainNavigationView);
        bottomNavigationView = ActivityCompat.requireViewById(this,
                R.id.bottom_navigation);

        DrawerLayout drawerLayout = findViewById(R.id.mainDrawer);
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> setTitle(destination.getLabel()));
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.calendarFragment, R.id.configurationFragment, R.id.foodFragment).setDrawerLayout(drawerLayout).build();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mnuExit) {
                logout();
            } else if (item.getItemId() == R.id.mnuAbout) {
                navigateToAboutPage();
            }
            return true;
        });
    }

    private void navigateToAboutPage() {
        navController.navigate(R.id.aboutActivity);
    }

    private void setupNavigationGraph() {
        int startDestinationResId;
        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(
                        R.id.navHostFragment);
        NavController navController = Objects.requireNonNull(navHost).getNavController();
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.main_navigation);

        startDestinationResId = R.id.calendarFragment;

        navGraph.setStartDestination(startDestinationResId);
        navController.setGraph(navGraph);
    }

    @Override
    public boolean onSupportNavigateUp() {
        DrawerLayout mainDrawer = findViewById(R.id.mainDrawer);
        if (mainDrawer != null && mainDrawer.isDrawerOpen(GravityCompat.START)) {
            mainDrawer.openDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void logout() {
        String KEYPATH = "token.txt";
        File file = new File(getFilesDir(), KEYPATH);
        if (file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
        navController.navigate(R.id.loginActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout mainDrawer = findViewById(R.id.mainDrawer);
        if (mainDrawer != null && mainDrawer.isDrawerOpen(GravityCompat.START)) {
            mainDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void clearBackStack() {
        finish();
    }

    @Override
    public void onConfigure(Toolbar toolbar) {
        setSupportActionBar(toolbar);

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
