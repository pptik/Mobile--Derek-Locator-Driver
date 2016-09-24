package pptik.org.mobildereklocator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pptik.org.mobildereklocator.Connection.IConnectionResponseHandler;
import pptik.org.mobildereklocator.Connection.RequestRest;
import pptik.org.mobildereklocator.Setup.ApplicationConstants;

public class MainActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //keperluan map
    private FloatingActionButton fabMyLoc,fabAddProgram;
    private boolean isFirstZoom = false;
    int permissionCheck =0;
    private static final int INITIAL_REQUEST=1337;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=INITIAL_REQUEST+1;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private String TAG = this.getClass().getSimpleName();
    private double currentLatitude;
    private double currentLongitude;
    private int id_user;
    MapView mapset;
    GeoPoint currentPoint;
    Marker curMarker;
    IMapController mapController;

    SharedPreferences prefs;
    Context context;
    Button _panicButton;
    String __username,__location,__latitude,__longitude,__nomortelepon;
    int panicbuttonstate=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        prefs = getSharedPreferences(ApplicationConstants.USER_PREFS_NAME,
                Context.MODE_PRIVATE);


        permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        setLocationBuilder();
        bindingXML();

    }

    private void bindingXML(){
        __username=prefs.getString(ApplicationConstants.USERNAME,"");
        __nomortelepon=prefs.getString(ApplicationConstants.NOMOR_HP,"");

        _panicButton=(Button)findViewById(R.id.panic_button);
        _panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (checkState()){
                  checkDataPanicButton();
                  Log.i("username",__username);
                  Log.i("location",__location);
                  Log.i("latitude",__latitude);
                  Log.i("longitude",__longitude);
                  Log.i("handphone",__nomortelepon);
                  sendPanicButtonReport();
              }

            }
        });

    }

    private boolean checkState(){
        if (panicbuttonstate==3){
            panicbuttonstate=2;
            _panicButton.setText("2");
            _panicButton.setTextSize(100);
            return false;

        }
        if (panicbuttonstate==2){
            panicbuttonstate=1;
            _panicButton.setText("1");
            return false;
        }
        if (panicbuttonstate==1){
            _panicButton.setText("0");


            _panicButton.setTextSize(50);
            _panicButton.setText("Help!");
            panicbuttonstate=3;
            return true;
        }
        return false;
    }
    private void checkDataPanicButton(){
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            __location=cityName+", " +stateName+", " +countryName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        __latitude=String.valueOf(currentLatitude);
        __longitude=String.valueOf(currentLongitude);
    }

    private void sendPanicButtonReport(){
        RequestRest req = new RequestRest(MainActivity.this, new IConnectionResponseHandler(){
            @Override
            public void OnSuccessArray(JSONArray result){

            }
            @Override
            public void onSuccessJSONObject(String result){
                try {
                    JSONObject obj = new JSONObject(result);
                    Toast.makeText(MainActivity.this, "Laporan Telah Dikirim, anda akan dihubungi secepatnya ", Toast.LENGTH_LONG).show();

                } catch (JSONException e){

                }
            }
            @Override
            public void onFailure(String e){
                Toast.makeText(MainActivity.this, "Maaf Laporan gagal dikirim, silahkan coba lagi atau hubungi 0982*** ", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onSuccessJSONArray(String result){
            }
        });


        req.panicButtonReport(__username,__location,__latitude,__longitude,__nomortelepon);
    }

    private void setLocationBuilder(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            Toast.makeText(this, "Location Detected "/*+mLocation.getLatitude()+" "+
                    mLocation.getLongitude()*/, Toast.LENGTH_SHORT).show();
            currentLatitude = mLocation.getLatitude();
            currentLongitude = mLocation.getLongitude();
            currentPoint = new GeoPoint(currentLatitude, currentLongitude);
            storeLastLocation(getApplicationContext(), String.valueOf(currentLatitude),String.valueOf(currentLongitude));

            isFirstZoom = true;
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the task you need to do.

                } else {

                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            mGoogleApiClient.connect();
        }catch (Exception e){
            Toast.makeText(this, "Location not Detected, Please Turn On GPS", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        currentPoint = new GeoPoint(currentLatitude, currentLongitude);
        storeLastLocation(getApplicationContext(), String.valueOf(currentLatitude),String.valueOf(currentLongitude));
        if(isFirstZoom == false){

            isFirstZoom = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Now lets connect to the API
        if(mGoogleApiClient.isConnected() == false)
            mGoogleApiClient.connect();

    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        if(mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        //
    }
    private void storeLastLocation(Context context, String latitude_,String longitude_) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstants.USER_LATITUDE, latitude_);
        editor.putString(ApplicationConstants.USER_LONGITUDE, longitude_);
        editor.commit();
    }


    @Override
    public void onBackPressed() {

        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = null;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.logout) {
            context.getSharedPreferences(ApplicationConstants.USER_PREFS_NAME,
                    Context.MODE_PRIVATE).edit().clear().commit();
            intent = new Intent(context, Login.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
