package com.bhupendra.farmers.admin;

import android.content.Intent;
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
import android.widget.Toast;

import com.bhupendra.farmers.Admin_View_reportActivity;
import com.bhupendra.farmers.R;
import com.bhupendra.farmers.executive.EX_Main_Activity;
import com.bhupendra.farmers.executive.MainActivity;
import com.bhupendra.farmers.executive.ViewReportActivity;
import com.bhupendra.farmers.login.LoginActivity;

public class Admin_dashBorad_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CardView add_ex,upadte_ex,report_ex,logout_admin,update_ex,searxh_ex,remove_ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_borad_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        add_ex = findViewById(R.id.dash_add_ex);
        upadte_ex = findViewById(R.id.dash_upadte_ex);
        searxh_ex = findViewById(R.id.dash_search_ex);
        remove_ex = findViewById(R.id.dash_remove_ex);
        report_ex = findViewById(R.id.dash_report);
        logout_admin = findViewById(R.id.dash_log_Out);

        add_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Admin_dashBorad_Activity.this , Add_ExActivity.class);
                startActivity(i);
            }
        });

        searxh_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Admin_dashBorad_Activity.this , Update_ExActivity.class);
                startActivity(i);
            }
        });
        remove_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Admin_dashBorad_Activity.this , Update_ExActivity.class);
                startActivity(i);
            }
        });
        upadte_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Admin_dashBorad_Activity.this , Update_ExActivity.class);
                startActivity(i);
            }
        });
        report_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Admin_dashBorad_Activity.this , Admin_View_reportActivity.class);
                startActivity(i);
            }
        });
        logout_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Admin_dashBorad_Activity.this , LoginActivity.class);
                startActivity(i);
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
        getMenuInflater().inflate(R.menu.admin_dash_borad_, menu);
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
}
