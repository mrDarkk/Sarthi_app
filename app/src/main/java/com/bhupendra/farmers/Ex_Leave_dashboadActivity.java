package com.bhupendra.farmers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import android.widget.Toast;

import com.bhupendra.farmers.executive.EX_Main_Activity;
import com.bhupendra.farmers.executive.Ex_dashboradActivity;
import com.bhupendra.farmers.executive.MainActivity;
import com.bhupendra.farmers.executive.ViewReportActivity;
import com.bhupendra.farmers.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Ex_Leave_dashboadActivity extends AppCompatActivity
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
    CardView leave_visit,farm_visit,exdash_report,ex_logout;

    double lon,lat;
    double d ;
    double d2 ;
    double dis_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex__leave_dashboad);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        txtProfileName.setText(user.getEmail());

        leave_visit = findViewById(R.id.leave_card);
        farm_visit = findViewById(R.id.visit_card);
        exdash_report = findViewById(R.id.exdash_report);
        ex_logout = findViewById(R.id.exdash_log_Out);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);


        //location
        getLocation();

       // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        DocumentReference docRef = db.collection("Executive").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d("data", document.getId() + " => " + document.getData());
                        String currentString = document.getString("Allocated_Area");
                        String[] separated = currentString.split(",");
                        d = Double.parseDouble(separated[0]); // this will contain "Fruit"
                        d2 = Double.parseDouble(separated[1]);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });


        distance(d,d2,lat,lon);



        leave_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Ex_Leave_dashboadActivity.this , TakeLeaveActivity.class);
                startActivity(i);
            }
        });

        exdash_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Ex_Leave_dashboadActivity.this , ViewReportActivity.class);
                startActivity(i);
            }
        });

        ex_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                i.setClass(Ex_Leave_dashboadActivity.this , LoginActivity.class);
                startActivity(i);
            }
        });
        farm_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                distance(d,d2,lat,lon);

                if (1.0 >= dis_location)
                {
                    Intent i = new Intent();
                    i.setClass(Ex_Leave_dashboadActivity.this , Ex_dashboradActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(Ex_Leave_dashboadActivity.this, "not in location "+dis_location +"km to go ", Toast.LENGTH_SHORT).show();
                }

            }
        });


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
        getMenuInflater().inflate(R.menu.ex__leave_dashboad, menu);
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
            i.setClass(Ex_Leave_dashboadActivity.this , LoginActivity.class);
            startActivity(i);
//            Intent i = new Intent();
//            i.setClass(MainActivity.this , LoginActivity.class);
//            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    private void distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        Toast.makeText(this, "dbhsd " + dist, Toast.LENGTH_SHORT).show();
        //return (dist);
        dis_location = dist;
        Toast.makeText(this, "as"+dist, Toast.LENGTH_SHORT).show();

    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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

            Geocoder gc = new Geocoder(Ex_Leave_dashboadActivity.this, Locale.getDefault());

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

            lon = loc.getLongitude();
            lat = loc.getLatitude();
//
//            location1.setText(sb.toString());
//            location.setText(get_location);
//            location_geopoint = get_location;
//            location_address = sb.toString();

            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String arg0) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

//            Intent i = new Intent();
//            i.setClass(Ex_Leave_dashboadActivity.this , ViewReportActivity.class);
//            startActivity(i);

            // Handle the camera action
        } else if (id == R.id.nav_report) {

            Intent i = new Intent();
            i.setClass(Ex_Leave_dashboadActivity.this , ViewReportActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_profile) {

            Intent i = new Intent();
            i.setClass(Ex_Leave_dashboadActivity.this , Ex_ProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
