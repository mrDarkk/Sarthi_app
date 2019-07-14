package com.bhupendra.farmers.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bhupendra.farmers.Adminn_Dash_board_new_MainActivity;
import com.bhupendra.farmers.Ex_Leave_dashboadActivity;
import com.bhupendra.farmers.admin.Admin_dashBorad_Activity;
import com.bhupendra.farmers.executive.Ex_dashboradActivity;
import com.bhupendra.farmers.executive.MainActivity;
import com.bhupendra.farmers.R;
import com.bhupendra.farmers.listViewData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoginActivity<d> extends AppCompatActivity {

    private static final String TAG = "EmailPasswordAuth";

    private EditText emailET;
    double d ;
    double d2 ;

    double dis_location;

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    private EditText passwordET;
    public StringBuilder object;
    double lon,lat;
    Button btt1;
    CheckBox checkBox;
    private FirebaseAuth firebaseAuth;

    SharedPreferences sp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @VisibleForTesting
    public ProgressDialog progressDialog;


   // public static final String PREFS_NAME = "MyPrefsFile";
   public static final String PREFS_NAME = "LoginPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //permission
        requestMultiplePermissions();


        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.pass);

        distance(d,d2,lat,lon);

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
//                double d =  73.8652;
//                double d2 = 18.4529;

                    distance(d,d2,lat,lon);
            }
        });


//        checkBox = findViewById(R.id.check_leav_or_visit);
        getLocation();
        firebaseAuth = FirebaseAuth.getInstance();

        setButtonListeners();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(LoginActivity.this, Ex_Leave_dashboadActivity.class);
            startActivity(intent);
        }



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






        public void goToMainActivity(){
        Intent i =
                new Intent(this,Ex_Leave_dashboadActivity.class);
        startActivity(i);
    }

    public void goToLOgin(){
        Intent i =
                new Intent(this,LoginActivity.class);
        startActivity(i);
    }


    private void setButtonListeners(){
        //login button
        findViewById(R.id.logIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegistrationLogin();
            }
        });
        //reset password - for unauthenticated user
      //  findViewById(R.id.rest_password_b).setOnClickListener(new View.OnClickListener() {
      //      @Override
       //     public void onClick(View view) {
          //      sendResetPasswordEmail();
      //      }
       // });sendResetPasswordEmail

        //logout button
      //  findViewById(R.id.logout_b).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logOut();
//            }
//        });

        //Verify email button
       // findViewById(R.id.verify_b).setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View view) {
         //       sendEmailVerificationMsg();
         //   }
       // });

        //update password - for signed in user
       // findViewById(R.id.update_password_b).setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {
        //        updatePassword();
       //     }
       // });

        //Order functionality to show how to secure firestore data
        //using firebase authentication and firestore security rules
//        findViewById(R.id.order_b).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent();
//                i.setClass(LoginActivity.this, MainActivity.class);
//                startActivity(i);
//            }
//        });
    }
    @Override
    public void onStart() {
        super.onStart();
        showAppropriateOptions();
    }
    private void  handleRegistrationLogin(){
        final String email = emailET.getText().toString();
        final String password = passwordET.getText().toString();

        if (!validateEmailPass(email, password)) {
            return;
        }

        //show progress dialog
        showProgressDialog();

        if(email.equals("admin@gmail.com")&&password.equals("123456"))
        {
            Intent i = new Intent();
            i.setClass(LoginActivity.this , Admin_dashBorad_Activity.class);
            startActivity(i);

        }else{
//            if (checkBox.isChecked())
//            {
//                performLoginforLeave(email,password);
//            }
//            else{
//
////                if (1.0 >= dis_location)
                    performLogin(email, password);
//                else {
//                    Toast.makeText(this, "not in location" + dis_location, Toast.LENGTH_SHORT).show();
//                }

          //  }


        }

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




//    private void performLoginOrAccountCreation(final String email, final String password){
//        firebaseAuth.fetchProvidersForEmail(email).addOnCompleteListener(
//                this, new OnCompleteListener<ProviderQueryResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "checking to see if user exists in firebase or not");
//                            ProviderQueryResult result = task.getResult();
//
//                            if(result != null && result.getProviders()!= null
//                                    && result.getProviders().size() > 0){
//                                Log.d(TAG, "User exists, trying to login using entered credentials");
//                                performLogin(email, password);
//                            }else{
//                                Log.d(TAG, "User doesn't exist, creating account");
//                                registerAccount(email, password);
//                            }
//                        } else {
//                            Log.w(TAG, "User check failed", task.getException());
//                            Toast.makeText(LoginActivity.this,
//                                    "There is a problem, please try again later.",
//                                    Toast.LENGTH_SHORT).show();
//
//                        }
//                        //hide progress dialog
//                        hideProgressDialog();
//                        //enable and disable login, logout buttons depending on signin status
//                        showAppropriateOptions();
//                    }
//                });
//    }
    private void performLogin(final String email, final String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "login success");

                            //make SharedPreferences object
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("logged", "logged");
                            editor.commit();

                           // sp.edit().putBoolean("logged",true).apply();
                            Intent i = new Intent();
                            i.setClass(LoginActivity.this , Ex_Leave_dashboadActivity.class);
                            startActivity(i);
                        } else {
                            Log.e(TAG, "Login fail", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }


    private void performLoginforLeave(final String email, final String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "login success");

                            //make SharedPreferences object
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("logged", "logged");
                            editor.commit();

                            // sp.edit().putBoolean("logged",true).apply();
                            Intent i = new Intent();
                            i.setClass(LoginActivity.this , Ex_Leave_dashboadActivity.class);
                            startActivity(i);
                        } else {
                            Log.e(TAG, "Login fail", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }

    private void  requestMultiplePermissions(){

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            // openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    public float calculateDistance(double d, double d2, double d3, double d4) {
        double d5 = Math.toRadians((double)(d - d3));
        d2 = Math.toRadians((double)(d2 - d4));
        d4 = Math.sin((double)(d5 /= 2.0));
        d5 = Math.sin((double)d5);
        d = Math.cos((double)Math.toRadians((double)d));
        d3 = Math.cos((double)Math.toRadians((double)d3));
        d = d4 * d5 + d * d3 * Math.sin((double)d2) * Math.sin((double)(d2 /= 2.0));
        return Math.round((double)(Math.atan2((double)Math.sqrt((double)d), (double)Math.sqrt((double)(1.0 - d))) * 2.0 * 6371.0));
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait!");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

//    private void registerAccount(String email, String password) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "account created");
//                        } else {
//                            Log.d(TAG, "register account failed", task.getException());
//                            Toast.makeText(LoginActivity.this,
//                                    "account registration failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        //hide progress dialog
//                        hideProgressDialog();
//                        //enable and disable login, logout buttons depending on signin status
//                        showAppropriateOptions();
//                    }
//                });
//    }
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void showAppropriateOptions(){
        hideProgressDialog();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
           // findViewById(R.id.login_items).setVisibility(View.GONE);
           // findViewById(R.id.logout_items).setVisibility(View.VISIBLE);

            //findViewById(R.id.verify_b).setEnabled(!user.isEmailVerified());
        } else {
           // findViewById(R.id.login_items).setVisibility(View.VISIBLE);
           // findViewById(R.id.logout_items).setVisibility(View.GONE);
        }
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

            Geocoder gc = new Geocoder(LoginActivity.this, Locale.getDefault());

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
            lon = loc.getLongitude();
            lat = loc.getLatitude();
            // Toast.makeText(MainActivity.this,sb.toString() , Toast.LENGTH_SHORT).show();

            //location into text area
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
//    private void logOut() {
//        firebaseAuth.signOut();
//        //sp.edit().putBoolean("logged",false).apply();
//
//        showAppropriateOptions();
//    }

}
