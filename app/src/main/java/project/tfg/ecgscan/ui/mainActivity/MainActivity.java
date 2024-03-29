package project.tfg.ecgscan.ui.mainActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.facebook.CallbackManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.ui.secondActivity.SecondActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;

    private MainActivityViewModel mainActivityViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_ECGScan);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Initialize Firebase Auth
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){    //  Logged
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            finish();
        }else if(!mainActivityViewModel.getPhoneLogin().getValue().peekContent()){      //  Not logged
            setupNavigationGraph(R.navigation.navigation_graph, R.id.fragmentStart);
        }
    }


    private void setupViews() {
        mainActivityViewModel = ViewModelProviders.of(this, new MainActivityViewModelFactory()).get(MainActivityViewModel.class);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> setTitle(destination.getLabel()));

        callbackManager = CallbackManager.Factory.create();

    }

    private void setupNavigationGraph(int graphId, int initialFragmentId) {
        int startDestinationResId;
        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(
                        R.id.nav_host_fragment);

        NavController navController = Objects.requireNonNull(navHost).getNavController();
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(graphId);

        startDestinationResId = initialFragmentId;

        navGraph.setStartDestination(startDestinationResId);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        NavigationUI.setupActionBarWithNavController(this, navController);

        navController.setGraph(navGraph);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
