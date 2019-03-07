package br.com.getmo.smartpushshowcase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import br.com.getmo.smartpushshowcase.geo.MyLocationDemoActivity;
import br.com.getmo.smartpushshowcase.inbox.FragmentInbox;
import br.com.getmo.smartpushshowcase.minimal.FragmentMinimal;
import br.com.getmo.smartpushshowcase.tag.FragmentTags;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
// https://stackoverflow.com/questions/13904505/how-to-get-center-of-map-for-v2-android-maps
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView( R.id.toolbar )
    Toolbar toolbar;

    @BindView( R.id.fab )
    FloatingActionButton fab;

    @BindView( R.id.drawer_layout )
    DrawerLayout drawer;

    @BindView( R.id.nav_view )
    NavigationView navigationView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        ButterKnife.bind(this );

        setSupportActionBar( toolbar );

        initMenuDrawer();

        initFragment( savedInstanceState );
    }

    @Override
    public void onBackPressed() {
        if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings ) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if ( id == R.id.nav_home ) {
            navigateTo( new FragmentMinimal() );
        } else if ( id == R.id.nav_tag ) {
            navigateTo( new FragmentTags() );
        } else if ( id == R.id.nav_geo ) {
            startActivity( new Intent( this, MyLocationDemoActivity.class ) );
        } else if ( id == R.id.nav_inbox ) {
            navigateTo( new FragmentInbox() );
        } else if (id == R.id.nav_share) {
//            message.setText( "Share" );
            Toast.makeText( this, "Share", Toast.LENGTH_SHORT ).show();
        } else if (id == R.id.nav_send) {
//            message.setText( "Send" );
            Toast.makeText( this, "Send", Toast.LENGTH_SHORT ).show();
        }

        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    @OnClick( R.id.fab )
    public void onClick( View view ) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();

    }

    private void initMenuDrawer() {
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close );

        drawer.addDrawerListener( toggle );
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener( this );
    }

    private void initFragment( Bundle savedInstanceState ) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if ( findViewById( R.id.container ) != null ) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if ( savedInstanceState != null ) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .add( R.id.container, new FragmentMinimal() ).commit();
        }
    }

    private void navigateTo( Fragment fragment ) {
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.container, fragment ).commit();
    }
}
