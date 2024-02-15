package com.example.googlemaps;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Marker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {
    GoogleMap mapa;
    private int contador = 0;
    private ArrayList<LatLng> puntos = new ArrayList<>();

    private RequestQueue requestQueue;
    private static final String API_KEY = "AIzaSyCw1KDa8VGucjI8Fun-yHfBFJD4qBBnlnI";

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

        mapFragment.getMapAsync(this);
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setOnMapClickListener(this);
        mapa.getUiSettings().setZoomControlsEnabled(true);


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

        LatLng madrid = new LatLng(-1.012529191970843, -79.46946021380661);

        CameraPosition campos = new CameraPosition.Builder()
                .target(madrid)
                .zoom(19)
                .bearing(45)
                .tilt(70)
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(campos);

        InfoAdapter infoWindowAdapter = new InfoAdapter(this);
        mapa.setInfoWindowAdapter(infoWindowAdapter);
        mapa.animateCamera(camUpd3);
        mapa.setOnMapClickListener(this);
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

        LatLng punto = new LatLng(latLng.latitude, latLng.longitude);
        mapa.addMarker(new MarkerOptions().position(punto));

       /* mapa.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Punto")); */

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
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?fields=name&location="
                + latLng.latitude + "," + latLng.longitude + "&radius=1500&type=bar&key=" + API_KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject place = results.getJSONObject(i);
                        String name = place.getString("name");
                        String placeId = place.getString("place_id");
                        String Ubicacion = place.getString("vicinity");
                        JSONObject geometry = place.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double placeLat = location.getDouble("lat");
                        double placeLng = location.getDouble("lng");
                        JSONArray photos = place.optJSONArray("photos");
                        if (photos != null && photos.length() > 0) {
                            JSONObject firstPhoto = photos.getJSONObject(0);
                            String photoReference = firstPhoto.getString("photo_reference");
                            String detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?"
                                    + "fields=name%2Crating%2Cformatted_phone_number" + "&place_id=" + placeId
                                    + "&key=" + API_KEY;
                            JsonObjectRequest detailsRequest = new JsonObjectRequest(Request.Method.GET, detailsUrl, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                String rating = response.getJSONObject("result").getString("rating");
                                                String phoneNumber = response.getJSONObject("result")
                                                        .getString("formatted_phone_number");
                                                String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?"
                                                        + "maxwidth=200" + "&photo_reference=" + photoReference
                                                        + "&key=" + API_KEY;
                                                ImageRequest imageRequest = new ImageRequest(photoUrl,
                                                        new Response.Listener<android.graphics.Bitmap>() {
                                                            @Override
                                                            public void onResponse(android.graphics.Bitmap response) {
                                                                LatLng placeLatLng = new LatLng(placeLat, placeLng);
                                                                MarkerOptions markerOptions = new MarkerOptions()
                                                                        .position(placeLatLng).title(name)
                                                                        .snippet("Calificacion: " + rating + "\nTelefono: "
                                                                                + phoneNumber + "\nUbicacion: " + Ubicacion);
                                                                Marker marker = mapa.addMarker(markerOptions);
                                                                marker.setTag(response);
                                                            }
                                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                error.printStackTrace();
                                                            }
                                                        });
                                                requestQueue.add(imageRequest);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                            requestQueue.add(detailsRequest);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }
}