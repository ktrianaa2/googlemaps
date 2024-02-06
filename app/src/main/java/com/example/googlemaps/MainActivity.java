package com.example.googlemaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener
{
    GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setOnMapClickListener(this);

        PolylineOptions lineas = new
                PolylineOptions()
                .add(new LatLng(45.0, -12.0))
                .add(new LatLng(45.0, 5.0))
                .add(new LatLng(34.5, 5.0))
                .add(new LatLng(34.5, -12.0))
                .add(new LatLng(45.0, -12.0));
        lineas.width(8);
        lineas.color(Color.RED);
        mapa.addPolyline(lineas);
    }

    public void ConfigMap (View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    public void MoveMapa (View view) {
        // CameraUpdate camUpd1 =
           //     CameraUpdateFactory
             //           .newLatLngZoom(new LatLng( 40.689282829455024, -74.04451839977278), 17);
        // mapa.moveCamera(camUpd1);

        LatLng lugar = new LatLng(27.17525931078609, 78.04203490765134);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(lugar)
                .zoom(20)
                .bearing(85) //noreste arriba
                .tilt(70) //punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(),
                "Lat: " + latLng.latitude + "\n" + "Lng: " + latLng.longitude + "\n",
                Toast.LENGTH_SHORT).show();
        LatLng punto = new LatLng(latLng.latitude,
                latLng.longitude);
        mapa.addMarker(new
                MarkerOptions().position(punto)
                .title("Marker in Sydney"));
    }


}