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
import com.google.maps.android.SphericalUtil;

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

        puntos  = new ArrayList<LatLng>();
        contador=0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setOnMapClickListener(this);

        CameraUpdate camUpd1 =
                CameraUpdateFactory
                        .newLatLngZoom(new LatLng(-1.012509212579253,
                                -79.46950741279677), 17);
        mapa.moveCamera(camUpd1);

        PolylineOptions lineas = new
                PolylineOptions()
                .add(new LatLng(-1.0123668827998387, -79.46721848497673))
                .add(new LatLng(-1.0134825073655847, -79.46740087517409))
                .add(new LatLng(-1.0131821469433566, -79.4718318840867))
                .add(new LatLng(-1.0119163420009807, -79.47187479942727))
                .add(new LatLng(-1.0123668827998387, -79.46721848497673));
        lineas.width(8);
        lineas.color(Color.RED);
        mapa.addPolyline(lineas);
    }

    public void ConfigMap (View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    public void MoveMap(View view){

        LatLng madrid = new LatLng(27.17516231214711, 78.04214977189152);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(madrid)
                .zoom(20)
                .bearing(85)
                .tilt(70)
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);
    }


    @Override
    public void onMapClick(LatLng latLng) {
         /*Toast.makeText(getApplicationContext(),
                "Lat: " + latLng.latitude + "\n" + "Lng: " +
latLng.longitude + "\n",
                Toast.LENGTH_SHORT).show();*/

        mapa.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Punto"));

        puntos.add(latLng);
        contador++;
        if(contador==4){
            PolylineOptions lineas = new
                    PolylineOptions()
                    .add(puntos.get(0))
                    .add(puntos.get(1))
                    .add(puntos.get(2))
                    .add(puntos.get(3))
                    .add(puntos.get(0));
            lineas.width(8);
            lineas.color(Color.RED);
            mapa.addPolyline(lineas);

            double area = SphericalUtil.computeArea(puntos);
            Toast.makeText(getApplicationContext(), "Área del poligono: " + area, Toast.LENGTH_SHORT).show();


            contador=0;
            puntos.clear();
        }

    }

}