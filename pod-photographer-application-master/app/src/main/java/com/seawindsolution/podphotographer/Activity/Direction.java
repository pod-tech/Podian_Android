package com.seawindsolution.podphotographer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seawindsolution.podphotographer.R;

import java.util.Locale;

public class Direction extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    ProgressDialog progressDialog;
    Double lat, lon;
    GoogleMap mMap;
    String address, Lat, Lng;
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        progressDialog = ProgressDialog.show(Direction.this, null, null, false, true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_loading_dialog);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            address = null;
            Lat = null;
            Lng = null;
        } else {
            address = extras.getString("address");
            Lat = extras.getString("Lat");
            Lng = extras.getString("Lng");
        }

        Button button = findViewById(R.id.redirect);
        TextView tv_address = findViewById(R.id.tv_address);
        tv_address.setText(address);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Lat + "," + Lng));
                startActivity(intent);
            }
        });

        menu = findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lat = Double.valueOf(String.valueOf(Lat));
        lon = Double.valueOf(String.valueOf(Lng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
        LatLng sydney = new LatLng(lat, lon);

        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Shoot Location"));
    }
}
