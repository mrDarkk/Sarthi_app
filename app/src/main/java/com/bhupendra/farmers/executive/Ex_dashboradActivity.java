package com.bhupendra.farmers.executive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bhupendra.farmers.Ex_Leave_dashboadActivity;
import com.bhupendra.farmers.R;
import com.bhupendra.farmers.admin.Admin_dashBorad_Activity;
import com.bhupendra.farmers.admin.Update_ExActivity;
import com.bhupendra.farmers.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Ex_dashboradActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    public TextView name,location,location1,activity_far;
    SharedPreferences sp;
    public Button bt,bt1,submit,getloction;
    public EditText et1 , et2,et3,et4;
    public String get_location;
    LocationManager locationManager;
    String far_activity,location_geopoint, location_address;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String PREFS_NAME = "LoginPrefs";
    CardView single_visit,group_visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_dashborad);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        txtProfileName.setText(user.getEmail());

        single_visit = findViewById(R.id.single_card);
        group_visit = findViewById(R.id.group_card);

        //location
        getLocation();



        single_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Ex_dashboradActivity.this , MainActivity.class);
                startActivity(i);
            }
        });
        group_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Ex_dashboradActivity.this , EX_Main_Activity.class);
                startActivity(i);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
       // NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ex_dashborad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {

            FirebaseAuth.getInstance().signOut();
//            sp = getSharedPreferences("login",
//                    MODE_PRIVATE);
//            sp.edit().putBoolean("logged",false).apply();
//            finish();

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("logged");
            editor.commit();
            finish();
            Intent i = new Intent();
            i.setClass(Ex_dashboradActivity.this , LoginActivity.class);
            startActivity(i);
//            Intent i = new Intent();
//            i.setClass(MainActivity.this , LoginActivity.class);
//            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Intent i = new Intent();
            i.setClass(Ex_dashboradActivity.this , Ex_Leave_dashboadActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_report) {
            Intent i = new Intent();
            i.setClass(Ex_dashboradActivity.this , ViewReportActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_profile) {



        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void getLocation() {
        try {
            LocationManager mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            LocationListener mLocListener = new MyLocationListener();
            mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);


        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public class MyLocationListener implements LocationListener{

        public void onLocationChanged(Location loc) {
//            String message = String.format(
//                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    loc.getLongitude(), loc.getLatitude()
//            );
            String get_location = String.format(
                    "%1$s , %2$s",
                    loc.getLongitude(), loc.getLatitude()
            );

            Geocoder gc = new Geocoder(Ex_dashboradActivity.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    sb.append(address.getAddressLine(i)).append(", ");
                sb.append(address.getLocality()).append(", ");
                sb.append(address.getAdminArea()).append(", ");
                sb.append(address.getPostalCode()).append("");

            }
            // Toast.makeText(MainActivity.this,sb.toString() , Toast.LENGTH_SHORT).show();

            //location into text area
//
//            location1.setText(sb.toString());
//            location.setText(get_location);
            location_geopoint = get_location;
            location_address = sb.toString();

            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String arg0) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}
