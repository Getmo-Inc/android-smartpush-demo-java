package br.com.getmo.smartpushshowcase.inbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.getmo.smartpushshowcase.R;
import br.com.smartpush.Smartpush;
import br.com.smartpush.SmartpushService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentInbox extends Fragment {

    @BindView( R.id.message )
    TextView message;

    @BindView( R.id.progress_bar )
    ProgressBar progress;

    @BindView( R.id.my_message_list )
    RecyclerView myList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_inbox, container, false );
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        myList.setHasFixedSize( true );

        // use a linear layout manager
        myList.setLayoutManager( new LinearLayoutManager( getContext() ) );

        progress.setVisibility( View.VISIBLE );
        message.setVisibility( View.GONE );
        myList.setVisibility( View.GONE );

        Smartpush.getLastMessages( getActivity(), null );
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager
                .getInstance( getActivity() )
                .registerReceiver(receiver,
                        new IntentFilter( SmartpushService.ACTION_LAST_10_NOTIF ) );

    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager
                .getInstance( getActivity() )
                .unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data ) {
            if ( data.getAction().equals( SmartpushService.ACTION_LAST_10_NOTIF ) ) {
                progress.setVisibility( View.GONE );

                String json = data.getStringExtra( "extra.VALUE" );
                JSONArray array = null;

                try {
                    array = new JSONArray( json );
                } catch ( JSONException e ) {
                    Log.e( "DEBUG", e.getMessage(), e );
                    message.setVisibility( View.VISIBLE );
                }

                ArrayList<Notification> dataset = new ArrayList<>();
                if ( array != null ) {
                    for ( int i = 0; i < array.length(); i++ ) {
                        try {
                            dataset.add( new Notification( array.getJSONObject( i ) ) );
                        } catch ( JSONException e ) {
                            Log.e( "DEBUG", e.getMessage(), e );
                        }
                    }
                }

                // specify an adapter (see also next example)
                myList.setAdapter( new MyAdapter( dataset ) );
                myList.setVisibility( View.VISIBLE );
            }
        }
    };

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<Notification> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextViewTitle;
            public TextView mTextViewDetail;
            public ImageView mImageViewBanner;

            public MyViewHolder( View v ) {
                super( v );

                mTextViewTitle = v.findViewById( R.id.title );
                mTextViewDetail = v.findViewById( R.id.detail );
                mImageViewBanner = v.findViewById( R.id.banner );
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter( ArrayList<Notification> myDataset ) {
            this.mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
            // create a new view
            View v = LayoutInflater
                    .from( parent.getContext() )
                    .inflate(R.layout.inbox_item, parent, false );
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextViewTitle.setText( mDataset.get(position).getPayloadValue( "title" ));
            holder.mTextViewDetail.setText( mDataset.get(position).getPayloadValue( "detail" ));

            if ( mDataset.get(position).getPayloadValue( "banner" ) != null ) {
                Picasso.get( )
                        .load( mDataset.get(position).getPayloadValue( "banner" ) )
                        .into( holder.mImageViewBanner );
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
