package com.ecys.ingenieria.usac.guate_disco;

/**
 * Created by akroma on 19/10/15.
 */
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Double latitud,altitud;
    String place="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latitud = getIntent().getExtras().getDouble("latitud", 1);
        altitud = getIntent().getExtras().getDouble("altitud", 1);
        place = getIntent().getExtras().getString("place", "...");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(latitud,altitud );
        map.addMarker(new MarkerOptions().position(sydney).title(place));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}