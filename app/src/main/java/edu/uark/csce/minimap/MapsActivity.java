package edu.uark.csce.minimap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Building[] buildings = {
            new Building("Mullins Library", 36.0686, -94.1736, false),
            new Building("Brough Dining Hall", 36.0662, -94.1752, false),
            new Building("JB-Hunt", 36.066052, -94.173755,false),
            new Building("The Union", 36.082157, -94.171852, false),
            new Building("Pat Walker: Health Center", 36.070790, -94.176020, false),

    };

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker to the selected building location and move the camera
        LatLng buildingLocation = new LatLng(buildings[position].getLatitude(), buildings[position].getLongitude());
        mMap.addMarker(new MarkerOptions().position(buildingLocation).title(buildings[position].getBuildingName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(buildingLocation));
    }
}
