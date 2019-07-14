package com.bhupendra.farmers.admin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.SearchManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.support.v7.widget.SearchView;

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
import com.bhupendra.farmers.Executive;
import com.bhupendra.farmers.ExecutiveAdapter;
import com.bhupendra.farmers.FixedGridLayoutManager;
import com.bhupendra.farmers.Issue;
import com.bhupendra.farmers.IssueAdapter;
import com.bhupendra.farmers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Update_ExActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SearchView searchView;

    FirebaseFirestore db;
    int scrollX = 0;

    List<Executive> executiveList = new ArrayList<>();

    RecyclerView rvClub;

    HorizontalScrollView headerScroll;


    ExecutiveAdapter executiveAdapter;
    FirebaseUser active_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__ex);
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


        //active_user = FirebaseAuth.getInstance().getCurrentUser();
        //search



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        db = FirebaseFirestore.getInstance();
        active_user = FirebaseAuth.getInstance().getCurrentUser();


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
    private void setUpRecyclerView()
    {
        executiveAdapter = new ExecutiveAdapter(Update_ExActivity.this, executiveList);

        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(1);
        rvClub.setLayoutManager(manager);
        rvClub.setAdapter(executiveAdapter);
        rvClub.addItemDecoration(new DividerItemDecoration(Update_ExActivity.this, DividerItemDecoration.VERTICAL));
    }


    private void prepareClubData()
    {

        db.collection("Executive")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("user", document.getId() + " => " + document.getString("Mail_id"));
                                //  Map data = (Map) document.getData();

                                executiveList.add(new Executive(document.getString("Full_name"), "https://firebasestorage.googleapis.com/v0/b/farmersapp-4478e.appspot.com/o/user_img%2Fuser_demo.jpeg?alt=media&token=30f384b2-9bb4-4ecf-a16a-4bc705e71997", document.getString("Mail_id"), document.getString("Contact"),document.getString("address"),document.getString("Allocated_Area")));
                                executiveAdapter.notifyDataSetChanged();
                                // Toast.makeText(Admin_View_reportActivity.this, issueList.toString(), Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.w("error_user", "Error getting documents.", task.getException());
                        }
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

        if (!searchView.isIconified())
        {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update__ex, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // filter recycler view when query submitted
                executiveAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                // filter recycler view when text is changed
                executiveAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search)
        {
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
