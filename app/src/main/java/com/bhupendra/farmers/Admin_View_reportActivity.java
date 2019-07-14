package com.bhupendra.farmers;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class Admin_View_reportActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore db;

//    private AbstractTableAdapter mTableViewAdapter;
//    private TableView mTableView;
//    private Filter mTableFilter; // This is used for filtering the table.
//    private Pagination mPagination; // This is used for paginating the table.
//
//    // This is a sample class that provides the cell value objects and other configurations.
//    private TableViewModel mTableViewModel;

    int scrollX = 0;

    List<Issue> issueList = new ArrayList<>();

    RecyclerView rvClub;

    HorizontalScrollView headerScroll;

    SearchView searchView;

    IssueAdapter issueAdapter;
    FirebaseUser active_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__view_report);
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
        // Let's get TableView
//        mTableView = findViewById(R.id.tableview);
//        initializeTableView();
    }

    private void initViews()
    {
        rvClub = findViewById(R.id.rvClub);
        headerScroll = findViewById(R.id.headerScroll);
    }

    /**
     * Prepares dummy data
     */
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

                                issueList.add(new Issue(document.getString("executive_email"), "https://firebasestorage.googleapis.com/v0/b/farmersapp-4478e.appspot.com/o/issue_img%2Fimages-2.jpeg?alt=media&token=58b60f4f-5f19-4a18-8881-78a8d9e3ed26", document.getString("farmer_name"), document.getString("farmer_contact_no"),document.getString("farmer_type"),document.getString("issue"),document.getString("topic"),document.getString("no_of_member_group"),document.getString("activity"),document.getString("address")));
                                issueAdapter.notifyDataSetChanged();
                               // Toast.makeText(Admin_View_reportActivity.this, issueList.toString(), Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.w("error_user", "Error getting documents.", task.getException());
                        }
                    }
                });
//        issueList.add(new Issue("Galatasaray", "https://tmssl.akamaized.net/images/wappen/head/141.png", "Istanbul, Turkey", "Ali Sami Yen", "Süper Lig", "Fatih Terim", "Bafetimbi Gomis"));
//        issueList.add(new Issue("Real Madrid", "https://tmssl.akamaized.net//images/wappen/head/418.png", "Madrid, Spain", "Santiago Barnabeu", "La Liga", "Zidane", "Cristiano Ronaldo"));
//        issueList.add(new Issue("Barcelona", "https://tmssl.akamaized.net//images/wappen/head/131.png", "Barcelona, Spain", "Camp Nou", "La Liga", "Ernesto Valverde", "Lionel Messi"));
//        issueList.add(new Issue("Bayern München", "https://tmssl.akamaized.net//images/wappen/head/27.png", "München, Germany", "Allianz Arena", "Bundesliga", "Jupp Heynckes", "Robert Lewandowski"));
//        issueList.add(new Issue("Manchester United", "https://tmssl.akamaized.net//images/wappen/head/985.png", "Manchester, England", "Old Trafford", "Premier League", "Jose Mourinho", "Paul Pogba"));
//        issueList.add(new Issue("Manchester City", "https://tmssl.akamaized.net//images/wappen/head/281.png", "Manchester, England", " Etihad Stadium", "Premier League", "Pep Guardiola", "Kevin de Bruyne"));
//        issueList.add(new Issue("Atletico Madrid", "https://tmssl.akamaized.net//images/wappen/head/13.png", "Madrid, Spain", "Estadio Metropolitano de Madrid ", "La Liga", "Diego Simeone", "Antoine Griezmann"));
//        issueList.add(new Issue("Liverpool", "https://tmssl.akamaized.net//images/wappen/head/31.png", "Liverpool, Spain", "Anfield", "Premier League", "Klopp", "Mo Salah"));
//        issueList.add(new Issue("Juventus", "https://tmssl.akamaized.net//images/wappen/head/506.png", "Turin, Italy", "Allianz Stadium", "Serie A", "Massimiliano Allegri", "Paulo Dybala"));
//        issueList.add(new Issue("Arsenal", "https://tmssl.akamaized.net//images/wappen/head/11.png", "London, England", "Emirates Stadium", "Premier League", "Arsene Wenger", "Mesut Özil"));
//        issueList.add(new Issue("Roma", "https://tmssl.akamaized.net//images/wappen/head/12.png", "Rome, Italy", " Olimpico di Roma", "Serie A", "Eusebio Di Francesco", "Cengiz Ünder"));
//        issueList.add(new Issue("PSG", "https://tmssl.akamaized.net//images/wappen/head/583.png", "Paris, France", "Parc des Princes ", "Ligue 1", "Unai Emery", "Neymar"));
//        issueList.add(new Issue("Chelsea", "https://tmssl.akamaized.net//images/wappen/head/631.png", "London, England", "Stamford Bridge", "Premier League", "Conte", "Eden Hazard"));
//        issueList.add(new Issue("Tottenham", "https://tmssl.akamaized.net//images/wappen/head/148.png", "London, England", "Wembley Stadium ", "Premier League", "Mauricio Pochettino", "Harry Kane"));
    }

    /**
     * Handles RecyclerView for the action
     */
    private void setUpRecyclerView()
    {
        issueAdapter = new IssueAdapter(Admin_View_reportActivity.this, issueList);

        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(1);
        rvClub.setLayoutManager(manager);
        rvClub.setAdapter(issueAdapter);
        rvClub.addItemDecoration(new DividerItemDecoration(Admin_View_reportActivity.this, DividerItemDecoration.VERTICAL));
    }

//    private void initializeTableView() {
//        // Create TableView View model class  to group view models of TableView
//        mTableViewModel = new TableViewModel(Admin_View_reportActivity.this);
//
//        // Create TableView Adapter
//        mTableViewAdapter = new TableViewAdapter(Admin_View_reportActivity.this, mTableViewModel);
//
//        mTableView.setAdapter(mTableViewAdapter);
//        mTableView.setTableViewListener(new TableViewListener(mTableView));
//
//        // Create an instance of a Filter and pass the TableView.
//        //mTableFilter = new Filter(mTableView);
//
//        // Load the dummy data to the TableView
//        mTableViewAdapter.setAllItems(mTableViewModel.getColumnHeaderList(), mTableViewModel
//                .getRowHeaderList(), mTableViewModel.getCellList());
//
//
//        //mTableView.setHasFixedWidth(true);
//
//        /*for (int i = 0; i < mTableViewModel.getCellList().size(); i++) {
//            mTableView.setColumnWidth(i, 200);
//        }*)
//
//        //mTableView.setColumnWidth(0, -2);
//        //mTableView.setColumnWidth(1, -2);
//
//        /*mTableView.setColumnWidth(2, 200);
//        mTableView.setColumnWidth(3, 300);
//        mTableView.setColumnWidth(4, 400);
//        mTableView.setColumnWidth(5, 500);*/
//
//    }
//
//
//
//    public void filterTable(String filter) {
//        // Sets a filter to the table, this will filter ALL the columns.
//        mTableFilter.set(filter);
//    }
//
//    public void filterTableForMood(String filter) {
//        // Sets a filter to the table, this will only filter a specific column.
//        // In the example data, this will filter the mood column.
//        mTableFilter.set(TableViewModel.MOOD_COLUMN_INDEX, filter);
//    }
//
//    public void filterTableForGender(String filter) {
//        // Sets a filter to the table, this will only filter a specific column.
//        // In the example data, this will filter the gender column.
//        mTableFilter.set(TableViewModel.GENDER_COLUMN_INDEX, filter);
//    }
//
//    // The following four methods below: nextTablePage(), previousTablePage(),
//    // goToTablePage(int page) and setTableItemsPerPage(int itemsPerPage)
//    // are for controlling the TableView pagination.
//    public void nextTablePage() {
//        mPagination.nextPage();
//    }
//
//    public void previousTablePage() {
//        mPagination.previousPage();
//    }
//
//    public void goToTablePage(int page) {
//        mPagination.goToPage(page);
//    }
//
//    public void setTableItemsPerPage(int itemsPerPage) {
//        mPagination.setItemsPerPage(itemsPerPage);
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // close search view on back button pressed
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
        getMenuInflater().inflate(R.menu.admin__view_report, menu);
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
                issueAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                // filter recycler view when text is changed
                issueAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
