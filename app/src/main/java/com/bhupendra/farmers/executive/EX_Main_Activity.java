package com.bhupendra.farmers.executive;

import android.app.ProgressDialog;
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
import android.support.design.widget.Snackbar;
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

import com.bhupendra.farmers.Ex_Leave_dashboadActivity;
import com.bhupendra.farmers.login.LoginActivity;
import com.bhupendra.farmers.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class EX_Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public TextView name,location,location1;
    public ProgressDialog progressDialog;
    String far_activity,location_geopoint, location_address;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String PREFS_NAME = "LoginPrefs";
    public EditText et1 , et2,et3,et4,et5;
    ProgressDialog mProgress;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex__main_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        location =  findViewById(R.id.location_text);
//        location1 =  findViewById(R.id.location_text_address);

        et1 =  findViewById(R.id.gr_far_name);
        et2 =  findViewById(R.id.gr_far_issue);
        et3 =  findViewById(R.id.gr_far_team_no);
        et4 =  findViewById(R.id.gr_far_topic);
        et5 =  findViewById(R.id.gr_far_no);
        submit = (Button)findViewById(R.id.submit);

        //side nav email id
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        txtProfileName.setText(user.getEmail());

        /// feb  menu button
        final FABsMenu menu = findViewById(R.id.fabs_menu);
        menu.setMenuUpdateListener(new FABsMenuListener() {
            // You don't need to override all methods. Just the ones you want.

            @Override
            public void onMenuClicked(FABsMenu fabsMenu) {
                super.onMenuClicked(fabsMenu); // Default implementation opens the menu on click
                // Toast.makeText(MainActivity.this, "\"You pressed the menu!\"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuCollapsed(FABsMenu fabsMenu) {
                super.onMenuCollapsed(fabsMenu);
                // Toast.makeText(MainActivity.this, "\"You pressed the collapsed!\"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuExpanded(FABsMenu fabsMenu) {
                super.onMenuExpanded(fabsMenu);
                // Toast.makeText(MainActivity.this, "\"You pressed the expanded!\"", Toast.LENGTH_SHORT).show();
            }
        });

        //feb button
        TitleFAB clickableTitle = findViewById(R.id.group_visit);
        clickableTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EX_Main_Activity.this, "group_visit", Toast.LENGTH_SHORT).show();

//                Intent i = new Intent();
//                i.setClass(EX_Main_Activity.this , EX_Main_Activity.class);
//                startActivity(i);

            }
        });

        TitleFAB single_visit = findViewById(R.id.single_visit);
        single_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EX_Main_Activity.this, "single_visit", Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setClass(EX_Main_Activity.this , MainActivity.class);
                startActivity(i);
            }
        });

        //location
        getLocation();

        //spinner

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Select Activity","Demonstration","Group Meeting", "Technology Transfer", "Training","Mela","Exhibition","Expert Visit");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                far_activity = item;
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //on submit clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertdata();



            }
        });



    }

    public void insertdata() {
        FirebaseUser active_user = FirebaseAuth.getInstance().getCurrentUser();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("executive_email",active_user.getEmail() );
        user.put("farmer_contact_no", et5.getText().toString());
        user.put("farmer_name",et1.getText().toString());
        user.put("issue", et2.getText().toString());
        user.put("no_of_member_group", et3.getText().toString());
        user.put("activity", far_activity);
        user.put("address",location_address);
        user.put("topic", et4.getText().toString());
        user.put("farmer_type", "group");
        user.put("img", "gs:/farmersapp-4478e.appspot.com/issue_img/images-2.jpeg");
        user.put("location",location_geopoint);

        // Add a new document with a generated ID
        db.collection("Issue")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("done_msg", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(EX_Main_Activity.this, "Data Submitted", Toast.LENGTH_SHORT).show();
                        black_textfiled();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail_msg", "Error adding document", e);
                    }
                });

    }

    public void black_textfiled() {
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");

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
        getMenuInflater().inflate(R.menu.ex__main_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out_ex) {
           // showProgressDialog();

            FirebaseAuth.getInstance().signOut();
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("logged");

            editor.commit();

            finish();
          //  hideProgressDialog();
            Intent i = new Intent();
            i.setClass(EX_Main_Activity.this , LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait!");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent i = new Intent();
            i.setClass(EX_Main_Activity.this , Ex_Leave_dashboadActivity.class);
            startActivity(i);


        } else if (id == R.id.nav_report) {
            Intent i = new Intent();
            i.setClass(EX_Main_Activity.this , ViewReportActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_send) {

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

            Geocoder gc = new Geocoder(EX_Main_Activity.this, Locale.getDefault());

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
