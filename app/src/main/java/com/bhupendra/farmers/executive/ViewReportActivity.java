package com.bhupendra.farmers.executive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.HorizontalScrollView;

import com.bhupendra.farmers.Admin_View_reportActivity;
import com.bhupendra.farmers.Ex_Leave_dashboadActivity;
import com.bhupendra.farmers.FixedGridLayoutManager;
import com.bhupendra.farmers.Issue;
import com.bhupendra.farmers.IssueAdapter;
import com.bhupendra.farmers.R;
import com.bhupendra.farmers.adapter.RecyclerViewAdapter;
import com.bhupendra.farmers.listViewData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewReportActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //RecyclerViewAdapter adapter;
   // ArrayList<listViewData> list = new ArrayList<>();
   // RecyclerView recyclerView;
    FirebaseUser active_user;

    int scrollX = 0;

    List<Issue> issueList = new ArrayList<>();

    RecyclerView rvClub;

    HorizontalScrollView headerScroll;

    SearchView searchView;

    IssueAdapter issueAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
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

        active_user = FirebaseAuth.getInstance().getCurrentUser();


//        adapter = new RecyclerViewAdapter(this,list);
//        recyclerView = findViewById(R.id.recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        db = FirebaseFirestore.getInstance();


        initViews();

        prepareClubData();

        setUpRecyclerView();

        rvClub.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                scrollX += dx;

                headerScroll.scrollTo(scrollX, 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


    }

    private void initViews()
    {
        rvClub = findViewById(R.id.rvClub);
        headerScroll = findViewById(R.id.headerScroll);
    }

    private void prepareClubData()
    {

        db.collection("Issue")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("user", document.getId() + " => " + document.getString("executive_email"));
                                //  Map data = (Map) document.getData();
                                if(active_user.getEmail().equals(document.getString("executive_email"))) {

                                    issueList.add(new Issue(document.getString("executive_email"), "https://firebasestorage.googleapis.com/v0/b/farmersapp-4478e.appspot.com/o/issue_img%2Fimages-2.jpeg?alt=media&token=58b60f4f-5f19-4a18-8881-78a8d9e3ed26", document.getString("farmer_name"), document.getString("farmer_contact_no"), document.getString("farmer_type"), document.getString("issue"), document.getString("topic"), document.getString("no_of_member_group"), document.getString("activity"), document.getString("address")));
                                    issueAdapter.notifyDataSetChanged();
                                    // Toast.makeText(Admin_View_reportActivity.this, issueList.toString(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        } else {
                            Log.w("error_user", "Error getting documents.", task.getException());
                        }
                    }
                });
     }


    private void setUpRecyclerView()
    {
        issueAdapter = new IssueAdapter(ViewReportActivity.this, issueList);

        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(1);
        rvClub.setLayoutManager(manager);
        rvClub.setAdapter(issueAdapter);
        rvClub.addItemDecoration(new DividerItemDecoration(ViewReportActivity.this, DividerItemDecoration.VERTICAL));
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
        getMenuInflater().inflate(R.menu.view_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out ) {
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
            Intent i = new Intent();
            i.setClass(ViewReportActivity.this , Ex_Leave_dashboadActivity.class);
            startActivity(i);
            // Handle the camera action

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
