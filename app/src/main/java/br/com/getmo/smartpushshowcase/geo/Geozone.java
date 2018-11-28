package br.com.getmo.smartpushshowcase.geo;

import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static br.com.smartpush.Utils.TAG;

/**
 * Created by fabio.licks on 09/02/2016.
 */
class Geozone {

    public static final String ALIAS  = "ALIAS";
    public static final String LAT    = "LAT";
    public static final String LNG    = "LNG";
    public static final String RADIUS = "RADIUS";

    public String alias;
    public double lat;
    public double lng;
    public int radius;

    public Geozone( JSONObject o ) {
        try {
            if ( o != null ) {
                alias = o.getString( ALIAS.toLowerCase() );
                lat = o.getDouble( LAT.toLowerCase() );
                lng = o.getDouble( LNG.toLowerCase() );
                radius = o.getInt( RADIUS.toLowerCase() );
            }
        } catch (JSONException e) {
            Log.e( TAG, e.getMessage(), e );
        }
    }

    @Override
    public String toString() {
        return "{ \"alias\":\"" + alias + "\"" + ", \"lat\":" + lat + ", \"lng\":" + lng +
                ", \"radius\":" + radius + '}';
    }

    public static ArrayList<Geozone> toList( String json ) {
        ArrayList<Geozone> list = new ArrayList<>();

        try {
            JSONArray array = new JSONArray( json );

            for ( int i = 0; i < array.length(); i++ ) {
                JSONObject obj = array.getJSONObject( i );
                list.add( new Geozone( obj ) );
            }

        } catch ( JSONException e ) {
            Log.e( "DEBUG", e.getMessage(), e );
        }

        return list;
    }

    public LatLng getLatLng() {
        return new LatLng( lat, lng );
    }
}