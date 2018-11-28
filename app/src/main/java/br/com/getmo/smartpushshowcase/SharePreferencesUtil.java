package br.com.getmo.smartpushshowcase;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {
    private static final String SHARED_PREFERENCE = "smartpush.PREFERENCES";

    public static final String KEY_ALIAS = "smartpush.KEY_ALIAS";
    public static final String KEY_HWID  = "smartpush.KEY_HWID";
    public static final String KEY_PUSH_STATUS = "smartpush.KEY_PUSH_STATUS";

    private static SharedPreferences get( Context context ) {
        return context.getSharedPreferences( SHARED_PREFERENCE, Context.MODE_PRIVATE );
    }

    public static Boolean getBoolean( Context context, String key ) {
        return get( context ).getBoolean(key, false);
    }

    public static String getString( Context context, String key ) {
        return get( context ).getString( key, "" );
    }

    public static void set( Context context, String key, Boolean b ) {
        if ( b == null ) {
            get( context ).edit().remove( key ).apply();
        } else {
            get( context ).edit().putBoolean( key, b ).apply();
        }
    }

    public static void set( Context context, String key, String s) {
        get( context ).edit().putString( key, s ).apply();
    }

    public static void remove( Context context, String key ) {
        get( context ).edit().remove( key ).apply();
    }
}
