package edu.uark.csce.minimap.ui.home;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import edu.uark.csce.minimap.Building;
import edu.uark.csce.minimap.ColorService;
import edu.uark.csce.minimap.MainActivity;
import edu.uark.csce.minimap.R;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener  {

    private HomeViewModel homeViewModel;
    private GoogleMap map;
    private int position;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Building[] buildings;
    private static final int REQUEST_CODE = 101;

    int RED;
    int GREEN;
    int BLUE;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            RED = intent.getIntExtra("RED", 0);
            GREEN = intent.getIntExtra("GREEN", 0);
            BLUE = intent.getIntExtra("BLUE", 255);
            updateBuildingColor(map);
            Log.d("receiver", "Got message: " + RED + " " + GREEN + " " + BLUE);
        }
    };

    public boolean areLocationPermissionsGranted(){
        return  ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(ColorService.ACTION));
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
////        homeViewModel.getText().observe(this, new Observer<String>() {
////            @Override
////            public void onChanged(@Nullable String s) {
////                textView.setText(s);
////            }
////        });
//        return root;
        createBuildings();
        if(getArguments()!=null)
            position = getArguments().getInt("POS");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLocation();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get the fragment xml resources
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    private void fetchLocation() {
        if (!areLocationPermissionsGranted()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //assign the local map varaible a googleMap variable
        map = googleMap;
        map.setBuildingsEnabled(true);
        if (areLocationPermissionsGranted())
            map.setMyLocationEnabled(true);
//        PolygonOptions rectOptions = new PolygonOptions()
//                .add(new LatLng(36.066417, -94.174103),
//                        new LatLng(36.066524, -94.173799),
//                        new LatLng(36.065930, -94.173419),
//                        new LatLng(36.065628, -94.173467),
//                        new LatLng(36.065704, -94.173987),
//                        new LatLng(36.066270, -94.174011));
//        rectOptions.fillColor(0);


        LatLng buildingLocation = new LatLng(buildings[position].getLatitude(), buildings[position].getLongitude());
        map.addMarker(new MarkerOptions().position(buildingLocation).title(buildings[position].getBuildingName()));


       map.moveCamera(CameraUpdateFactory.newLatLngZoom(buildingLocation, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(buildingLocation)      // Sets the center of the map to location user
                .zoom(18)                      // Sets the zoom
                .bearing(0)                    // Sets the orientation of the camera to east
                .tilt(0)                       // Sets the tilt of the camera to 30 degrees
                .build();                      // Creates a CameraPosition from the builder
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        updateBuildingColor(map);
// Get back the mutable Polygon
//        Polygon polygon = map.addPolygon(rectOptions);

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
            PolygonOptions polyOptions = new PolygonOptions()
                    .add(buildings[position].polygon)
                    .fillColor(heatShade);
            mMap.addPolygon(polyOptions);
        }

    }
    public void createBuildings()
    {

        LatLng[] MullinCoords = {   new LatLng(36.069150, -94.174346),
                                    new LatLng(36.069178, -94.173322),
                                    new LatLng(36.068206, -94.173303),
                                    new LatLng(36.068206, -94.174300)};

        LatLng[] JB_HuntCoords = {  new LatLng(36.066417, -94.174103),
                                    new LatLng(36.066524, -94.173799),
                                    new LatLng(36.065930, -94.173419),
                                    new LatLng(36.065628, -94.173467),
                                    new LatLng(36.065704, -94.173987),
                                    new LatLng(36.066270, -94.174011)};
        buildings = new Building[]{
                new Building("Mullins Library", 36.0686,-94.1736, true, MullinCoords),
                new Building("Brough Dining Hall", 36.0662, -94.1752, false),
                new Building("JB-Hunt", 36.066052, -94.173755, true, JB_HuntCoords),
                new Building("The Union", 36.082157, -94.171852, false),
                new Building("Pat Walker: Health Center", 36.070790, -94.176020, false),
                new Building("Campus Bookstore on Dickson", 36.066760, -94.167390, false)
        };
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();

                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {

        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        NavHostFragment.findNavController(this).navigate(R.id.navigation_notifications);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }
}