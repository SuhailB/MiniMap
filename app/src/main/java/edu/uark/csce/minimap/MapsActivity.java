package edu.uark.csce.minimap;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String MY_PREFS_NAME = "RGB";
    int RED;
    int GREEN;
    int BLUE;
    private GoogleMap mMap;
    Building[] buildings;
    int position;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            RED = intent.getIntExtra("RED", 0);
            GREEN = intent.getIntExtra("GREEN", 0);
            BLUE = intent.getIntExtra("BLUE", 255);
            updateBuildingColor(mMap);
            Log.d("receiver", "Got message: " + RED + " " + GREEN + " " + BLUE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ColorService.ACTION));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        createBuildings();
        mapFragment.getMapAsync(this);
        position = getIntent().getExtras().getInt("POSITION");

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    protected void onResume() {
        super.onResume();
        Intent colorService = new Intent(this, ColorService.class);
        startService(colorService);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);
        // Add a marker to the selected building location and move the camera
        LatLng buildingLocation = new LatLng(buildings[position].getLatitude(), buildings[position].getLongitude());
        mMap.addMarker(new MarkerOptions().position(buildingLocation).title(buildings[position].getBuildingName()));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(buildingLocation, 13));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(buildingLocation)      // Sets the center of the map to location user
                .zoom(18)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        updateBuildingColor(mMap);

    }


    public void updateBuildingColor(GoogleMap mMap)
    {
        mMap.clear();
        LatLng buildingLocation = new LatLng(buildings[position].getLatitude(), buildings[position].getLongitude());
        mMap.addMarker(new MarkerOptions().position(buildingLocation).title(buildings[position].getBuildingName()));
        if (buildings[position].isHeatmapAvailable()) {

            //Not finished here, need to get the color from the database.
            int heatShade = Color.argb(150, RED, GREEN, BLUE);
            int test = Color.rgb(2, 4, 155);
            PolygonOptions rectOptions = new PolygonOptions()
                    .add(
                            buildings[position].polygon[0],
                            buildings[position].polygon[1],
                            buildings[position].polygon[2],
                            buildings[position].polygon[3]
                    )
                    .fillColor(heatShade);
            mMap.addPolygon(rectOptions);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent myService = new Intent(MapsActivity.this, ColorService.class);
        stopService(myService);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent myService = new Intent(MapsActivity.this, ColorService.class);
        stopService(myService);
    }

    public void createBuildings()
    {
//        buildings = new Building[]{
//        new Building("Mullins Library",
//                36.0686,
//                -94.1736,
//                true,
//                36.069150,
//                -94.174346,
//                36.069178,
//                -94.173322,
//                36.068206,
//                -94.173303,
//                36.068206,
//                -94.174300),
//                new Building("Brough Dining Hall", 36.0662, -94.1752, false),
//                new Building("JB-Hunt", 36.066052, -94.173755, false),
//                new Building("The Union", 36.082157, -94.171852, false),
//                new Building("Pat Walker: Health Center", 36.070790, -94.176020, false),
//                new Building("Campus Bookstore on Dickson", 36.066760, -94.167390, false)
//    };
    }
}
