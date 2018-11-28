package br.com.getmo.smartpushshowcase.minimal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import br.com.getmo.smartpushshowcase.MainActivity;
import br.com.getmo.smartpushshowcase.R;
import br.com.smartpush.SmartpushListenerService;

/**
 * Created by GETMO on 16/11/18.
 */
public class MySmartpushListenerService extends SmartpushListenerService {

    @Override
    protected void handleMessage( Bundle data ) {
        String message = data.getString( "detail" );

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

        sendNotification( message, data );
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification( String message, Bundle extras ) {
        Intent intent = new Intent( this, MainActivity.class );
        intent.putExtras( extras );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        PendingIntent pendingIntent =
                PendingIntent
                        .getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder( this )
                .setSmallIcon( R.drawable.ic_stat_getmo_icon )
                .setContentTitle( "Push Notification!" )
                .setContentText( message )
                .setAutoCancel( true )
                .setSound( defaultSoundUri )
                .setContentIntent( pendingIntent );

        NotificationManager notificationManager =
                ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );

        notificationManager.notify( 1000 /* ID of notification */, notificationBuilder.build() );
    }
}