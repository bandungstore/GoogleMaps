package edu.upi.student.fikryalfarisimuslim.googlemaps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    private Marker currentPos;
    private Marker currentPlace;
    double lat = 0.0;
    double lng = 0.0;
    DBadapter myDB;
    LatLng place;
    DBadapter.Markers wp;
    int score = 0;
    int i = 1;
    Polyline line;
    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    int statsMarker = 1;
    int KM ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        statsMarker = 1;
        score = 0;
        i = 1;
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onShake() {
                // Do stuff!
                mMap.clear();
                placeMarker(lat, lng);
                statsMarker = 1;
                //double temp = CalculationByDistance(new LatLng(lat, lng),place);
                currentPlace = mMap.addMarker(new MarkerOptions().position(place).title(wp.getNama()));

                Toast.makeText(MapsActivity.this, "Point Anda Saat Ini : " + score + " Tujuan Berikutnya : " + wp.getNama(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume () {
    super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
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
        openDB();

        wp = myDB.getMarker(String.valueOf(i));
        place =new LatLng(wp.getLat(),wp.getLng());


        mLocation();
       // PolylineOptions options = new PolylineOptions()
        //        .add(currentPos.getPosition())
         //       .add(currentPlace.getPosition())
        //        .color(Color.BLUE);
        //line = mMap.addPolyline(options);
        //  LatLng posSekarang = new LatLng(-6.8603119,107.5900084);
        // currentPos = mMap.addMarker(new MarkerOptions().position(posSekarang).title("PosSekarang"));
//        batas lokasi UPI
        // LatLngBounds UPI = new LatLngBounds(new LatLng(-6.863273,107.587212),new LatLng(-6.858025, 107.597839));
        // LatLng UPI1 = new LatLng(-6.863273,107.587212);
        // LatLng GIK = new LatLng(-6.860418,107.589889);
        // mMap.addMarker(new MarkerOptions().position(UPI).title("Marker di GIK").snippet("aku disini").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)));
        // mMap.addMarker(new MarkerOptions().position(UPI1).title("Marker di UPI"));

//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(UPI,0));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(UPI1));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posSekarang,15));

    }


    private void openDB () {
        myDB = new DBadapter(this);
        myDB.open();
    }

    private void placeMarker(double lat, double lng) {
        LatLng coordinates = new LatLng(lat, lng);
        CameraUpdate setCamera = CameraUpdateFactory.newLatLngZoom(coordinates, 16);
        if (currentPos != null) currentPos.remove();
        currentPos = mMap.addMarker(new MarkerOptions().position(coordinates).title("Saya Disini!!!").icon(BitmapDescriptorFactory.fromResource(R.drawable.player)));
        mMap.animateCamera(setCamera);

        if(statsMarker == 1) {
            if (line != null) line.remove();
            line = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(lat, lng), new LatLng(wp.getLat(), wp.getLng()))
                    .width(5)
                    .color(Color.RED));
        }
    }

    private void actualPosition(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            placeMarker(lat, lng);
        }
    }

    android.location.LocationListener locListerner = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualPosition(location);
            //line.remove();
            //draw line



            Location target = new Location("target");
            for(LatLng point : new LatLng[]{place}) {
                target.setLatitude(point.latitude);
                target.setLongitude(point.longitude);
                if(location.distanceTo(target) < 100 ) {
                    statsMarker = 0;
                    mMap.clear();
                    i++;

                    score = score + 10;
                    wp = myDB.getMarker(String.valueOf(i));
                    place =new LatLng(wp.getLat(),wp.getLng());
                    //currentPlace = mMap.addMarker(new MarkerOptions().position(place).title(wp.getNama()));
                    actualPosition(location);

                    AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                    if(i>5){
                        alertDialog.setTitle("SEMUA TEMPAT TERCAPAI. BERHASIL!!!");
                        alertDialog.setMessage("Point Anda = "+ score);

                    }else {
                        alertDialog.setTitle("Selamat anda mendapatkan 10 point");
                        alertDialog.setMessage("Shake hpmu untuk mengetahui tempat berikutnya");
                    }
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if(i>5){
                                        Intent intent2 = getIntent();
                                        String scorer = Integer.toString(score);
                                        intent2.putExtra(Main2Activity.Extra_Score, scorer);
                                        setResult(RESULT_OK,intent2);
                                        finish();
                                    }
                                }
                            });
                    alertDialog.show();



                }

            }

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
    };



    private void mLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualPosition(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0, locListerner);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        KM = kmInDec;
        //Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                //+ " Meter   " + meterInDec);

        return Radius * c;
    }
}
