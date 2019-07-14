package com.bhupendra.farmers.admin;

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
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.bhupendra.farmers.Ex_Leave_dashboadActivity;
import com.bhupendra.farmers.R;
import com.bhupendra.farmers.executive.EX_Main_Activity;
import com.bhupendra.farmers.executive.Ex_dashboradActivity;
import com.bhupendra.farmers.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Add_ExActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public EditText et1 ,et2,et3,et4,et5;
    Button submit;
    String district ,taluka, village,location_geopoint, location_address;
    private FirebaseAuth firebaseAuth;
    private EditText emailET;
    private EditText passwordET;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user = new HashMap<>();
    MaterialSpinner dis_spinner, taluka_spinner,village_spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__ex);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        firebaseAuth = FirebaseAuth.getInstance();

        getLocation();


        dis_spinner = (MaterialSpinner) findViewById(R.id.spinner_district);
        taluka_spinner = (MaterialSpinner) findViewById(R.id.spinner_taluka);
        village_spinner = (MaterialSpinner) findViewById(R.id.spinner_village);
        dis_spinner.setItems("Select District","Pune");
        dis_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                district = item;
                if (district.equals("Pune")){
                    taluka_spinner.setItems("Select Taluka","haveli","bhor","velha","mulshi","purndhar","baramati");
                    taluka_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                            taluka = item;

                                if (taluka.equals("haveli")) {
                                    village_spinner.setItems("Select village", "aglambe", "alandi mhatobachi", "ahire", "ambee","ambegoan kh", "ambegoan bk","dhayri","khed", "guad dara", "bhilarewadi","mangdewadi", "donaje", "hadpsar", "jambhulwadi","khadkwasla");


                                }else if (taluka.equals("bhor")){
                                    village_spinner.setItems("Select village", "karandi", "nasrapur", "rajne", "kamthadi", "kelwade", "malegoan","kambre", "karandi kh", "karandi bk", "kapurhol", "kasurdi", "sarola", "kikvi", "naygoan", "degoan");

                                }
                            }

                    });

//                    taluka = item;
//
//                    if (taluka.equals("haveli")) {
//                        village_spinner.setItems("Select village", "aglambe", "alandi mhatobachi", "ahire", "ambee","ambegoan kh", "ambegoan bk","dhayri","khed", "guad dara", "bhilarewadi","mangdewadi", "donaje", "hadpsar", "jambhulwadi","khadkwasla");
//
//                    }else if (taluka.equals("bhor")){
//                        village_spinner.setItems("Select village", "karandi", "nasrapur", "rajne", "kamthadi", "kelwade", "malegoan","kambre", "karandi kh", "karandi bk", "kapurhol", "kasurdi", "sarola", "kikvi", "naygoan", "degoan");
//
//                    }
                }


            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        et1 =  findViewById(R.id.emp_name);
        emailET =  findViewById(R.id.emp_mail_id);
        passwordET =  findViewById(R.id.emp_Password);
        et4 =  findViewById(R.id.emp_address);
        et5 =  findViewById(R.id.emp_con_no);
        submit = (Button)findViewById(R.id.submit);


        //on submit clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(Add_ExActivity.this, ""+et2.getText().toString()+""+et3.getText().toString(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(Add_ExActivity.this, "", Toast.LENGTH_SHORT).show();
               // insertdata();

               // createAccount(Email,Pass);
                handleRegistrationLogin();
               // performLoginOrAccountCreation(et2.toString(),et3.toString());

            }
        });

    }

    private void  handleRegistrationLogin(){
        final String email = emailET.getText().toString();
        final String password = passwordET.getText().toString();

        if (!validateEmailPass(email, password)) {
            return;
        }

        //show progress dialog
       // showProgressDialog();
        createAccount(email,password);

        //perform login and account creation depending on existence of email in firebase

    }

    private boolean validateEmailPass(String email , String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Required.");
            valid = false;
        }else if(!email.contains("@")){
            emailET.setError("Not an email id.");
            valid = false;
        } else{
            emailET.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Required.");
            valid = false;
        }else if(password.length() < 6){
            passwordET.setError("Min 6 chars.");
            valid = false;
        }else {
            passwordET.setError(null);
        }

        return valid;
    }




    private void createAccount(String email, String password) {
        Log.d("msg", "createAccount:" + email);

        Toast.makeText(Add_ExActivity.this, "email - "+email+" pASS - "+password, Toast.LENGTH_SHORT).show();


        //showProgressDialog();

        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("msg", "createUserWithEmail:success");
                           // FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(Add_ExActivity.this, "doneeee", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                            insertdata();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("msg", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Add_ExActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                    }
                });

    }

    public void insertdata() {
       // FirebaseUser active_user = FirebaseAuth.getInstance().getCurrentUser();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Full_name",et1.getText().toString() );
        user.put("Mail_id",emailET.getText().toString());
        user.put("Birth_Date", "");
        user.put("Contact", et5.getText().toString());
        user.put("Allocated_Area", location_geopoint);
        user.put("address",et4.getText().toString());


        // Add a new document with a generated ID
        db.collection("Executive").document(emailET.getText().toString())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("done_msg", "DocumentSnapshot added with ID: ");
                        Toast.makeText(Add_ExActivity.this, "Data Submitted", Toast.LENGTH_SHORT).show();
                        black_textfiled();
                    }

//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//
//                    }
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
        getMenuInflater().inflate(R.menu.add__ex, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

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

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location loc) {
//            String message = String.format(
//                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    loc.getLongitude(), loc.getLatitude()
//            );
            String get_location = String.format(
                    "%1$s , %2$s",
                    loc.getLatitude(),loc.getLongitude()
            );

            Geocoder gc = new Geocoder(Add_ExActivity.this, Locale.getDefault());

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
