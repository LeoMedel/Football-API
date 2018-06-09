package com.example.mkmkmk.footballapi;

import android.Manifest;
import android.content.pm.PackageManager;
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

        new GetStadeTeam().execute(urlMaps);


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
    }


    public void addMarkerTeam(double latitude, double longitude, String stadeName)
    {
        Marker markerTeam;
        LatLng coordonneeStade = new LatLng(latitude, longitude);
        CameraUpdate stadeUbication = CameraUpdateFactory.newLatLngZoom(coordonneeStade, 5);

        markerTeam = mMap.addMarker(new MarkerOptions()
                .position(coordonneeStade)
                .title("Stade de "+stadeName)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(stadeUbication);

    }


    private void addMarker(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);

        CameraUpdate myUbication = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);

        if (marker != null) {
            marker.remove();
        }

        marker = mMap.addMarker(new MarkerOptions().position(coordenadas).title("My Ubication").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(myUbication);
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
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?address=Estadio");
        googlePlaceUrl.append("+"+teamName);
        //Google API PLACE, different Google Maps
        googlePlaceUrl.append("&key=AIzaSyCw3naphbRCLrt7c10B-riAq9hIOzyg0kQ");

        Log.i("DEBUG CreateURL", googlePlaceUrl.toString());
        //URL final pour realiser la requete a Google Place API
        return googlePlaceUrl.toString();
    }


    public class GetStadeTeam extends AsyncTask<Object, String, String> {


        String dataStade;
        GoogleMap map;
        String url;
        //ProgressBar progressStade;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MapsActivity.this, "url Maps"+urlMaps, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(Object... objects) {

            //map = (GoogleMap) objects[0];
            url = (String) objects[0];
            //progressStade = (ProgressBar) objects[2];

            //progressStade.setVisibility(View.VISIBLE);

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
                Toast.makeText(MapsActivity.this, "Error GoogleMaps", Toast.LENGTH_SHORT).show();
                //myUbication();
            }
            else
            {
                showStade(stadeList);
                //myUbication();
            }

        }

        private void showStade(List<HashMap<String, String>> stadeList) {

            mMap.clear();

            for (int i = 0; i < stadeList.size(); i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();

                HashMap<String, String> stadePlace = stadeList.get(i);

                String city = stadePlace.get("city");
                String county = stadePlace.get("country");
                String address = stadePlace.get("address");
                double latitude = Double.parseDouble(stadePlace.get("latitude"));
                double longitude = Double.parseDouble(stadePlace.get("longitude"));

                Log.i("DEBUG STADE MARKER", "city"+city);
                Log.i("DEBUG STADE MARKER", "country"+county);
                Log.i("DEBUG STADE MARKER", "address"+address);
                Log.i("DEBUG STADE MARKER", "latitude"+latitude);
                Log.i("DEBUG STADE MARKER", "longitude"+longitude);

                LatLng latLng = new LatLng(latitude, longitude);

                markerOptions.position(latLng);

                markerOptions.title("Stade de "+teamName+" "+ city+" "+county+" "+address);

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            }

        }
    }
}
