package edu.uark.csce.minimap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //this is a git comment te
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent myService = new Intent(MainActivity.this, ColorService.class);
        stopService(myService);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent myService = new Intent(MainActivity.this, ColorService.class);
        stopService(myService);
    }



    @Override
    protected void onResume() {
        super.onResume();
        Intent colorService = new Intent(this, ColorService.class);
        startService(colorService);
    }
}

