package project.tfg.ecgscan.ui.secondActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.ui.mainActivity.MainActivity;

public class SecondActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private SecondActivityViewModel secondActivityViewModel;
    private SharedPreferences preferences;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setupViews();
        setupNavigationGraph();
        setupPreferences();
        setupFirebaseStorage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setupFirebaseStorage() {
        storage = configureFirebase("ecgscan-240b9", "1:784049752308:android:ab77ff02b03aaf5d9ab6a1", "AIzaSyDgQU3c-aj-U-dlMeUa9HZa2NMCY7A5Rb4", "imgs/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        storageRef = storage.getReference("imgs/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/");

        secondActivityViewModel.setFirebaseStorage(storage);
        secondActivityViewModel.setStorageReference(storageRef);
    }


    private FirebaseStorage configureFirebase(String projectID, String applicationID, String APIkey, String databaseURL) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId(projectID)
                .setApplicationId(applicationID)
                .setApiKey(APIkey)
                .setDatabaseUrl(databaseURL)
                .setStorageBucket("ecgscan-240b9.appspot.com")
                .build();

        try {
            FirebaseApp.initializeApp(this, options);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }

        return FirebaseStorage.getInstance();

    }

    private void setupPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences != null){
            secondActivityViewModel.setPreferencesCloud(preferences.getBoolean(getString(R.string.preferencesCloud), false));
        }else{
            secondActivityViewModel.setPreferencesCloud(false);
        }
    }


    private void setupViews() {
        secondActivityViewModel = ViewModelProviders.of(this, new SecondActivityViewModelFactory()).get(SecondActivityViewModel.class);

        toolbar = Objects.requireNonNull(findViewById(R.id.my_toolbar));

        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.nav_second_host_fragment);

        navigationView = ActivityCompat.requireViewById(this, R.id.mainNavigationView);
        drawerLayout = Objects.requireNonNull(findViewById(R.id.mainDrawer));

        //navController.addOnDestinationChangedListener(
          //      (controller, destination, arguments) -> setTitle(destination.getLabel()));

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            String test = destination.getDisplayName();
            switch (destination.getId()) {
                case R.id.tabsListFragment:
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_list);
                    break;
                default:
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.main_menu);
                    break;
            }
        });


        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.listFragment, R.id.settingsFragment, R.id.tabsListFragment).setDrawerLayout(drawerLayout).build();

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
                    drawerLayout.closeDrawer(GravityCompat.START);
                    secondActivityViewModel.setNavigateToList(true);
                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

            } else if (item.getItemId() == R.id.item_home) {
                if (!navController.getCurrentDestination().getDisplayName().contains("home")){      //  Hardcoded option
                    onBackPressed();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

            }
            return true;
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                logout();
                break;
            case R.id.menu_settings:
                openSettings();
                break;
            case R.id.menu_search:
                break;
            default:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
        }
        return true;
    }

    private void openSettings() {
        navController.navigate(R.id.settingsFragment);
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
