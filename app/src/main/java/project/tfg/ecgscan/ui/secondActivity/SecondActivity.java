package project.tfg.ecgscan.ui.secondActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.ui.mainActivity.MainActivity;

public class SecondActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private SecondActivityViewModel secondActivityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setupViews();
        setupNavigationGraph();

    }


    private void setupViews() {
        secondActivityViewModel = ViewModelProviders.of(this, new SecondActivityViewModelFactory()).get(SecondActivityViewModel.class);

        Toolbar myToolbar = (Toolbar) Objects.requireNonNull(findViewById(R.id.my_toolbar));

        setSupportActionBar(myToolbar);
        navController = Navigation.findNavController(this, R.id.nav_second_host_fragment);

        navigationView = ActivityCompat.requireViewById(this, R.id.mainNavigationView);
        drawerLayout = Objects.requireNonNull(findViewById(R.id.mainDrawer));

        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> setTitle(destination.getLabel()));

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.listFragment).setDrawerLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }



    private void setupNavigationGraph() {
        int startDestinationResId;

        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(
                        R.id.nav_second_host_fragment);
        NavController navController = Objects.requireNonNull(navHost).getNavController();
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.main_graph);
        startDestinationResId = R.id.homeFragment;
        navGraph.setStartDestination(startDestinationResId);
        navController.setGraph(navGraph);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item_logout) {
                logout();
            } else if (item.getItemId() == R.id.item_list) {
                if (!navController.getCurrentDestination().getDisplayName().contains("list")){      //  Hardcoded option
                    String a = navController.getCurrentDestination().getRoute();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    secondActivityViewModel.setNavigateToList(true);
                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

            }
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                logout();
                break;
            default:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
        }
        return true;
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
        return true;
    }





}
