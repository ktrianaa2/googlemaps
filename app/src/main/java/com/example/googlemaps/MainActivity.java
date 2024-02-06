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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener
{
    GoogleMap mapa;
    private int contador = 0;
    private ArrayList<LatLng> puntos = new ArrayList<>();

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

        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(-1.012569747818524, -79.46954114585787), 17);
        mapa.moveCamera(camUpd1);

        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(-1.0119858275267548, -79.47158283128604))
                .add(new LatLng(-1.0129263982999144, -79.47163214309582))
                .add(new LatLng(-1.0131509364880045, -79.46732184243038))
                .add(new LatLng(-1.0123568247655028, -79.46729313511372))
                .add(new LatLng(-1.0119858275267548, -79.47158283128604));
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

        LatLng lugar = new LatLng(-1.0124663039271578, -79.46953959888509);
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
                .title("Marca"));

        contador++;
        puntos.add(latLng);

        mapa.addMarker(new MarkerOptions().position(latLng).title("Marker"));

        if (contador == 4) {
            dibujarCuadrado();

            // contador = 0;
        }

    }

    private void dibujarCuadrado() {
        PolygonOptions cuadradoOptions = new PolygonOptions();
        for (LatLng punto : puntos) {
            cuadradoOptions.add(punto);
        }

        cuadradoOptions.strokeWidth(5);
        cuadradoOptions.strokeColor(Color.BLUE);

        mapa.addPolygon(cuadradoOptions);
    }

}