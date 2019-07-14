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
import com.bhupendra.farmers.R;
import com.bhupendra.farmers.admin.Admin_dashBorad_Activity;
import com.bhupendra.farmers.login.LoginActivity;
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

public class MainActivity extends AppCompatActivity
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //permission
//        requestMultiplePermissions();


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
                Toast.makeText(MainActivity.this, "group_visit", Toast.LENGTH_SHORT).show();

                Intent i = new Intent();
                i.setClass(MainActivity.this , EX_Main_Activity.class);
                startActivity(i);

            }
        });

        TitleFAB single_visit = findViewById(R.id.single_visit);
        single_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "single_visit", Toast.LENGTH_SHORT).show();
            }
        });

        //spinner

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Select Activity","Demonstration", "Technology Transfer", "Training");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //location.setText(item);
                far_activity = item;
               // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

       // NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

      //location
        getLocation();



            bt =  findViewById(R.id.cap_imp);
        et1 =  findViewById(R.id.far_name);
        et2 =  findViewById(R.id.far_no);
        et3 =  findViewById(R.id.far_issue);
        et4 =  findViewById(R.id.far_topic);
        activity_far =  findViewById(R.id.sppinnner_text);
        location =  findViewById(R.id.location_text);
        location1 =  findViewById(R.id.location_text_address);
        bt1 =  findViewById(R.id.upload_imp);
       // location1.setText(sb.toString());
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivity(i);
            }
        });

        submit = (Button)findViewById(R.id.submit);
      //  getloction = (Button)findViewById(R.id.getLoaction);
       // locationText = (TextView)findViewById(R.id.locationText);
//        getloction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLocation();
//            }
//        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertdata();



            }
        });


    }

    public void black_textfiled() {
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");

    }




//    private void  requestMultiplePermissions(){
//
//        Dexter.withActivity(this)
//                .withPermissions(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        // check if all permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        // check for permanent denial of any permission
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // show alert dialog navigating to Settings
//                           // openSettingsDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).
//                withErrorListener(new PermissionRequestErrorListener() {
//                    @Override
//                    public void onError(DexterError error) {
//                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .onSameThread()
//                .check();
//    }


    public void insertdata() {
        FirebaseUser active_user = FirebaseAuth.getInstance().getCurrentUser();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("executive_email",active_user.getEmail() );
        user.put("farmer_contact_no", et2.getText().toString());
        user.put("farmer_name",et1.getText().toString());
        user.put("issue", et3.getText().toString());
        user.put("activity", far_activity);
        user.put("address",location_address);
        user.put("no_of_member_group","");
        user.put("topic", et4.getText().toString());
        user.put("farmer_type", "single");
        user.put("img", "gs:/farmersapp-4478e.appspot.com/issue_img/images-2.jpeg");
        user.put("location",location_geopoint);

        // Add a new document with a generated ID
        db.collection("Issue")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("done_msg", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this, "Data Submitted", Toast.LENGTH_SHORT).show();
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
            i.setClass(MainActivity.this , LoginActivity.class);
            startActivity(i);
//            Intent i = new Intent();
//            i.setClass(MainActivity.this , LoginActivity.class);
//            startActivity(i);
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent i = new Intent();
            i.setClass(MainActivity.this , Ex_Leave_dashboadActivity.class);
            startActivity(i);


        } else if (id == R.id.nav_report) {
            Intent i = new Intent();
            i.setClass(MainActivity.this , ViewReportActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

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

            Geocoder gc = new Geocoder(MainActivity.this, Locale.getDefault());

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
