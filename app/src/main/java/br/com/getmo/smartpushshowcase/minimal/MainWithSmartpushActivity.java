package br.com.getmo.smartpushshowcase.minimal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import br.com.getmo.smartpushshowcase.MainActivity;
import br.com.getmo.smartpushshowcase.SharePreferencesUtil;
import br.com.smartpush.Smartpush;
import br.com.smartpush.SmartpushDeviceInfo;
import br.com.smartpush.SmartpushService;

public class MainWithSmartpushActivity extends MainActivity {
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        // Register at Smartpush!
        Smartpush.subscribe( this );
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager
                .getInstance( this )
                .registerReceiver( mRegistrationBroadcastReceiver,
                        new IntentFilter(
                                Smartpush.ACTION_REGISTRATION_RESULT ) );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager
                .getInstance( this )
                .unregisterReceiver( mRegistrationBroadcastReceiver );
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data ) {

            if ( data.getAction().equals( Smartpush.ACTION_REGISTRATION_RESULT ) ) {

                SmartpushDeviceInfo device =
                        data.getParcelableExtra( Smartpush.EXTRA_DEVICE_INFO );

                // Use this event to set your custom TAG device Id!
                Smartpush.setTag( context, "DEMO_APP_ID", device.alias );

                // Just for convenience - begin
                SharePreferencesUtil.set( context, SharePreferencesUtil.KEY_ALIAS, device.alias );
                SharePreferencesUtil.set( context, SharePreferencesUtil.KEY_HWID, device.hwId );
                SharePreferencesUtil.set( context, SharePreferencesUtil.KEY_PUSH_STATUS, true );

                // Notify fragments with these keys
                LocalBroadcastManager
                        .getInstance( getApplicationContext() )
                        .sendBroadcast( new Intent( "REFRESH" ) );
                // Just for convenience - end
            }
        }
    };

}
