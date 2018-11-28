package br.com.getmo.smartpushshowcase.minimal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import br.com.getmo.smartpushshowcase.R;
import br.com.getmo.smartpushshowcase.SharePreferencesUtil;
import br.com.smartpush.Smartpush;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentMinimal extends Fragment {

    @BindView( R.id.alias )
    TextView tvAlias;

    @BindView( R.id.hwid )
    TextView tvHwid;

    @BindView( R.id.switch_push_status )
    Switch swPushStatus;

    @BindView( R.id.label_push_status )
    TextView tvPushStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_minimal, container, false );
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );

        ButterKnife.bind( this, view );

        refreshUI( getContext() );

        swPushStatus.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged( CompoundButton compoundButton, boolean b ) {
                    // Just for convenience
                    SharePreferencesUtil.set(
                            getContext(), SharePreferencesUtil.KEY_PUSH_STATUS, new Boolean( b ) );
                    // Just for convenience

                    // This is important to (un)block push notification!
                    Smartpush.blockPush( getContext(), b );

                    refreshUI( getContext() );
                }
            }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager
                .getInstance( getActivity() )
                .registerReceiver( mRefreshBroadcastReceiver,
                        new IntentFilter( "REFRESH" ) );

//        LocalBroadcastManager
//                .getInstance( getActivity() )
//                .registerReceiver( mDeviceInfoBroadcastReceiver,
//                        new IntentFilter( SmartpushDeviceInfo.EXTRA_DEVICE_INFO ) );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager
                .getInstance( getActivity() )
                .unregisterReceiver( mRefreshBroadcastReceiver );

//        LocalBroadcastManager
//                .getInstance( getActivity() )
//                .unregisterReceiver( mRefreshBroadcastReceiver );
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data ) {
        if ( data.getAction().equals( "REFRESH" ) ) {
            refreshUI( context );

//            // Just for convenience - Get Device User Infos
//            Smartpush.getUserInfo( getContext() );
        }
        }
    };

//    private BroadcastReceiver mDeviceInfoBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent data ) {
//            if ( data.getAction().equals( SmartpushDeviceInfo.EXTRA_DEVICE_INFO ) ) {
//                SmartpushDeviceInfo device =
//                        data.getParcelableExtra( SmartpushDeviceInfo.EXTRA_DEVICE_INFO );
//
//                SharePreferencesUtil.set( context, SharePreferencesUtil.KEY_ALIAS, device.alias );
//                SharePreferencesUtil.set( context, SharePreferencesUtil.KEY_HWID, device.hwId );
//                SharePreferencesUtil.set( context, SharePreferencesUtil.KEY_PUSH_STATUS, device.optout );
//
//                refreshUI( context );
//            }
//        }
//    };

    private void refreshUI( Context context ) {

        boolean pushStatus =
                SharePreferencesUtil.getBoolean( context, SharePreferencesUtil.KEY_PUSH_STATUS );

        swPushStatus.setChecked( pushStatus );
        tvPushStatus.setText( getTextStatus( pushStatus ) );
        tvPushStatus.setTextColor( getTextColor( pushStatus ) );

        tvAlias.setText(
                SharePreferencesUtil.getString(
                        context, SharePreferencesUtil.KEY_ALIAS ) );

        tvHwid.setText(
                SharePreferencesUtil.getString(
                        context, SharePreferencesUtil.KEY_HWID ) );
    }

    private int getTextColor( boolean pushStatus ) {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            return getContext().getColor( ( pushStatus ) ? R.color.green : R.color.red );
        } else {
            return getResources().getColor( ( pushStatus ) ? R.color.green : R.color.red );
        }
    }

    private String getTextStatus( boolean pushStatus ) {
        return ( pushStatus ) ? getString( R.string.active ) : getString( R.string.inactive );
    }

}