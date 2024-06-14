package com.proyecto.mallnav.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkManager;
import androidx.work.Constraints;


import android.os.Bundle;

import com.proyecto.mallnav.R;
import com.proyecto.mallnav.service.NavigationWorker;
import com.proyecto.mallnav.ui.custom.navigation.SavedBottomNavigationView;
import com.proyecto.mallnav.ui.fragments.LocationsFragment;
import com.proyecto.mallnav.ui.fragments.NavigationFragment;
import com.proyecto.mallnav.ui.fragments.ProfileFragment;
import com.proyecto.mallnav.viewmodel.SharedViewModel;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SharedViewModel viewModel = null;
    private SavedBottomNavigationView mBottomNavigation = null;
    private List<Integer> navGraphIds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModel();
        initNavigationView();
        startNavigationService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
    }

    private void initNavigationView() {
        mBottomNavigation = findViewById(R.id.main__bottom_navigation);
        navGraphIds = Arrays.asList(
                R.navigation.navigation_locations,
                R.navigation.navigation_navigation,
                R.navigation.navigation_profile);

        mBottomNavigation.setupWithNavController(
                navGraphIds,
                getSupportFragmentManager(),
                R.id.nav_host_fragment_activity_main,
                getIntent());
    }

    private void startNavigationService() {
        Constraints constraints = new Constraints.Builder().build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NavigationWorker.class)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(request);
    }
}

