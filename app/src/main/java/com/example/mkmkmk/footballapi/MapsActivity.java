package com.example.mkmkmk.footballapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mkmkmk.footballapi.GetDataStadium.DataParser;
import com.example.mkmkmk.footballapi.GetDataStadium.DownloadUrl;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String teamName;
    String urlMaps;

    private Marker marker;
    double latitude = 0.0;
    double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        teamName = getIntent().getStringExtra("team");
        Toast.makeText(this, teamName, Toast.LENGTH_SHORT).show();

        String team = teamName;
        String nameFormat;
        nameFormat = team.replaceAll("\\s", "+");

        urlMaps = getUrl(nameFormat);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        myUbication();

        Object dataTransfer[] = new Object[3];

        dataTransfer[0] = urlMaps;
        //finalement, on ajoute le Mot-clé
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;

        new GetStadeTeam().execute(dataTransfer);
    }

    private void addMarker(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);

        //CameraUpdate myUbication = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);

        if (marker != null) {
            marker.remove();
        }

        marker = mMap.addMarker(new MarkerOptions()
                .position(coordenadas).title("Vous êtes ici")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.player)));
        //mMap.animateCamera(myUbication);
    }

    private void updateUbication(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            addMarker(latitude, longitude);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateUbication(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void myUbication() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateUbication(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }


    private String getUrl(String teamName)
    {
        //on commence avec une variable qui va ajouter les direfents parties qui vont compposer l'url de demande a Google place
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?query=Stadium");
        googlePlaceUrl.append("+of+"+teamName);
        //Google API KEY PLACE, different Google Maps
        googlePlaceUrl.append("&key=AIzaSyCw3naphbRCLrt7c10B-riAq9hIOzyg0kQ");

        Log.i("DEBUG CreateURL", googlePlaceUrl.toString());
        //URL final pour realiser la requete a Google Place API
        return googlePlaceUrl.toString();
    }


    public class GetStadeTeam extends AsyncTask<Object, String, String> {


        String dataStade;
        String url;
        double myLatitude;
        double myLongitude;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... objects) {

            url = (String) objects[0];
            myLatitude = (double) objects[1];
            myLongitude = (double) objects[2];
            DownloadUrl downloadUrl = new DownloadUrl();

            dataStade = downloadUrl.readUrl(url);

            return dataStade;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            List<HashMap<String, String>> stadeList = null;

            DataParser dataParser = new DataParser();
            stadeList = dataParser.parserData(result);

            if (stadeList.size()==0 || stadeList ==null)
            {
                Toast.makeText(MapsActivity.this, "Error Google Maps Places", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                showStade(stadeList);
            }

        }

        private void showStade(List<HashMap<String, String>> stadeList) {

            mMap.clear();

            for (int i = 0; i < stadeList.size(); i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();

                HashMap<String, String> stadePlace = stadeList.get(i);

                String address = stadePlace.get("address");
                String nameStade = stadePlace.get("name");
                double latitude = Double.parseDouble(stadePlace.get("latitude"));
                double longitude = Double.parseDouble(stadePlace.get("longitude"));

                Log.i("DEBUG STADE MARKER", "address "+address);
                Log.i("DEBUG STADE MARKER", "name Stade "+nameStade);
                Log.i("DEBUG STADE MARKER", "latitude "+latitude);
                Log.i("DEBUG STADE MARKER", "longitude "+longitude);

                LatLng latLng = new LatLng(latitude, longitude);

                markerOptions.position(latLng);

                markerOptions.title("Stade de "+teamName+", "+nameStade);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.stadiumicon));


                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(4.50f));
                myUbication();

                //variables de type Location avec les coordonnes correspondant de stade et la notre, pour pouvoir avoir la distance
                Location loc = new Location("Location 1");
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);

                Location loc2 = new Location("Location 2");
                loc2.setLatitude(myLatitude);
                loc2.setLongitude(myLongitude);

                //on peut obtenir la distance
                double distanceM = loc.distanceTo(loc2);
                double distanceK = distanceM/1000;

                //finalement, on va desiner la ligne dans la carte
                Polyline line = mMap.addPolyline( new PolylineOptions()
                        .add(new LatLng(latitude, longitude), new LatLng(myLatitude,myLongitude))
                        .width(5)
                        .color(Color.GREEN));

                /* On peut ajouter une action dans le marker
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                        return true;
                    }
                });
                */
            }

        }
    }
}
