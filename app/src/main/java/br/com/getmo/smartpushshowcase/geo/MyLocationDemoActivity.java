package br.com.getmo.smartpushshowcase.geo;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import br.com.getmo.smartpushshowcase.R;
import br.com.smartpush.Smartpush;
import br.com.smartpush.SmartpushService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MyLocationDemoActivity extends AppCompatActivity
        implements
//            OnMyLocationButtonClickListener,
//            OnMyLocationClickListener,
            OnMapReadyCallback {
//            ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    @BindView( R.id.toolbar )
    Toolbar toolbar;

    @BindView( R.id.btnSendGeolocation )
    Button sendGeoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_my_location_demo );

        ButterKnife.bind(this );

        setSupportActionBar( toolbar );
        getSupportActionBar().setHomeButtonEnabled(true);

        sendGeoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = mMap.getCameraPosition().target;
                Toast.makeText( MyLocationDemoActivity.this, "Current location:\n" + latLng.toString(), Toast.LENGTH_LONG).show();
                Smartpush.nearestZone( MyLocationDemoActivity.this, latLng.latitude, latLng.longitude );
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map );

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager
                .getInstance( this )
                .registerReceiver( receiver,
                        new IntentFilter(
                                SmartpushService.ACTION_GEOZONES_UPDATED ) );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager
                .getInstance( this )
                .unregisterReceiver( receiver );
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        LatLng portoAlegre = new LatLng(-30.0277, -51.2287 );
        mMap.animateCamera( CameraUpdateFactory.newLatLng( portoAlegre ) );

//        mMap.setOnMyLocationButtonClickListener( this );
//        mMap.setOnMyLocationClickListener( this );
//
//        enableMyLocation();

        addGeozonesToMap();

        Smartpush.nearestZone( this, portoAlegre.latitude, portoAlegre.longitude );
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data ) {
            if ( data.getAction().equals( SmartpushService.ACTION_GEOZONES_UPDATED ) ) {
                addGeozonesToMap();
            }
        }
    };

    private void addGeozonesToMap() {
        mMap.clear();
        ArrayList<Geozone> list =
                Geozone.toList( Smartpush.getGeozones( this ) );

        Iterator<Geozone> it = list.iterator();
        while( it.hasNext() ) {
            Geozone geozone = it.next();

            mMap.addMarker(
                    new MarkerOptions().position( geozone.getLatLng() ).title( geozone.alias ) );

            mMap.addCircle( new CircleOptions()
                    .center( geozone.getLatLng() )
                    .radius( geozone.radius )
                    .strokeWidth( 2 )
                    .strokeColor( Color.HSVToColor(
                            24, new float[]{ 217, 1, 1} ) )
                    .fillColor( Color.HSVToColor(
                            49, new float[]{ 230, 1, 1} ) )
                    .clickable( false ) );
        }

        LatLng initialPosition =
                ( list.size() > 0 ) ? list.get( 0 ).getLatLng() : new LatLng(-30.0277, -51.2287 );

        mMap.animateCamera( CameraUpdateFactory.newLatLng( initialPosition ) );
    }

//    /**
//     * Enables the My Location layer if the fine location permission has been granted.
//     */
//    private void enableMyLocation() {
//        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission to access the location is missing.
//            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true);
//        } else if ( mMap != null ) {
//            // Access to the location has been granted to the app.
//            mMap.setMyLocationEnabled( true );
//        }
//    }
//
//    @Override
//    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
//        // Return false so that we don't consume the event and the default behavior still occurs
//        // (the camera animates to the user's current position).
//        return false;
//    }
//
//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
//        Smartpush.nearestZone( this, location.getLatitude(), location.getLongitude() );
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//            @NonNull int[] grantResults) {
//        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
//            return;
//        }
//
//        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
//                Manifest.permission.ACCESS_FINE_LOCATION)) {
//            // Enable the my location layer if the permission has been granted.
//            enableMyLocation();
//        } else {
//            // Display the missing permission error dialog when the fragments resume.
//            mPermissionDenied = true;
//        }
//    }
//
//    @Override
//    protected void onResumeFragments() {
//        super.onResumeFragments();
//        if (mPermissionDenied) {
//            // Permission was not granted, display error dialog.
//            showMissingPermissionError();
//            mPermissionDenied = false;
//        }
//    }
//
//    /**
//     * Displays a dialog with error message explaining that the location permission is missing.
//     */
//    private void showMissingPermissionError() {
//        PermissionUtils.PermissionDeniedDialog
//                .newInstance(true).show(getSupportFragmentManager(), "dialog");
//    }
}