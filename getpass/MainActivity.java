package com.tomi5548319.getpass;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseHelper myDb;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<RecyclerViewItem> mRecyclerList;
    private String mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_title);

        startEnterKeyActivity(); // Password is required to access other passwords
    }

    private void startEnterKeyActivity(){
        Intent intent = new Intent(MainActivity.this, EnterKeyActivity.class);
        intent.putExtra("com.tomi5548319.getpass.GET", "KEY");
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // Handle activity results here
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : { // AddNewPasswordActivity closed
                if (resultCode == RESULT_OK) { // Activity closed successfully
					// Get data from returned intent
                    String name = data.getStringExtra("NAME");
                    String seed = data.getStringExtra("SEED");
                    int flag = data.getIntExtra("FLAG", 0);
					
					// Add a new item
                    insertItem(name, seed, flag);
                }
                break;
            }
            case (3) : { // EnterKeyActivity closed
                if (resultCode == RESULT_OK) { // Activity closed successfully
					// Get the key from the returned intent and start main
                    mKey = data.getStringExtra("KEY");
                    main();
                }else{ // Activity didn't close properly, it has to be reloaded
                    startEnterKeyActivity();
                }
                break;
            }
            case (4) : { // EditPasswordActivity closed
                if(resultCode == RESULT_OK) { // Activity closed successfully
					// Get data from the returned intent
                    int ID = data.getIntExtra("ID", -1);
                    int position = data.getIntExtra("POSITION", -1);
                    String name = data.getStringExtra("NAME");

					// TODO check if this if is necessary
                    if(ID != -1 && position != -1){
                        myDb.updateEditData(ID, name);
                        mRecyclerList.get(position).changeText1(name);
                        mAdapter.notifyItemChanged(position);
                    }
                }
                break;
            }
        }
    }
	
	public void insertItem(String text, String seed, int flags){
        boolean inserted = myDb.insertData(text, seed, flags);
        if (inserted) { // Item was successfully inserted into the database
            int ID = myDb.getHighestID(); // Get the highest ID in the databse (last item added)
            mRecyclerList.add(new RecyclerViewItem(ID, R.drawable.ic_android, text)); // Add a new item into the recycler list
            mAdapter.notifyItemInserted(mRecyclerList.size()); // Make an animation
        }
    }

	private void main(){
        createDrawerAndToolbar();
        createFAB();
        createRecyclerList();
        buildRecyclerView();
	}
	
	public void createDrawerAndToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) { // ...
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // ... Item clicked
        /* Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
         as you specify a parent activity in AndroidManifest.xml.*/
        int id = item.getItemId();

        // Put switch here if you want more options
        if (id == R.id.action_changeKey) { // Change Key
            startEnterKeyActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody") // TODO Delete this later
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { // TODO Create menu (drawer)
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case (R.id.nav_home) : {

            }
            break;

            case (R.id.nav_gallery) : {

            }
            break;

            case (R.id.nav_slideshow) : {

            }
            break;

            case (R.id.nav_tools) : {

            }
            break;

            case (R.id.nav_share) : {

            }
            break;

            case (R.id.nav_send) : {

            }
            break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START); // Close the drawer
        return true;
    }

	public void createFAB(){ // Button (+)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddNewPasswordActivity();
            }
        });
    }

    private void startAddNewPasswordActivity(){
        Intent intent = new Intent(MainActivity.this, AddNewPasswordActivity.class);
        intent.putExtra("com.tomi5548319.getpass.ADD", "ADD NEW PASSWORD!");
        intent.putExtra("com.tomi5548319.getpass.ADD_KEY", mKey);
        startActivityForResult(intent, 1);
    }

    public void createRecyclerList(){
        myDb = new DatabaseHelper(this); // Initialize the database
        mRecyclerList = new ArrayList<>(); // Initialize the recycler list

        Cursor res = myDb.getRecyclerData(); // Get data from the database and store it in a Cursor object

        if(res.getCount() == 0){ // Database is empty
            // mRecyclerList.add(new RecyclerViewItem(-1, R.drawable.ic_adb, "Create a password")); // "Add new password" recycler view item
        }else{ // Database is not empty
            while(res.moveToNext()){ // Do this for each row
                mRecyclerList.add(new RecyclerViewItem(res.getInt(0), R.drawable.ic_android, res.getString(1))); // Item 0,1,2,...
            }
        }
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView1); // Initialize recycler view
        mLayoutManager = new LinearLayoutManager(this); // Initialize the layout manager
        mAdapter = new RecyclerViewAdapter(mRecyclerList); // Initialize the recycler view adapter

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager); // mLayoutManager = layout manager for recycler view
        mRecyclerView.setAdapter(mAdapter); // mAdapter = layout adapter for recycler view

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(int position) {
                viewItem(mRecyclerList.get(position).getID());
            }

            @Override
            public void onItemClick(int position) { // TODO change this (onItemClick) (recycler view item)
                //changeItem(position, "Clicked"); // TIP: changeItem can be found below
            }

            @Override
            public void onDeleteClick(int position) { // Delete item (position for the recycler list + ID for the database)
                deleteItem(position, mRecyclerList.get(position).getID());
            }

            @Override
            public void onEditClick(int position) {
                editItem(mRecyclerList.get(position).getID(), position);
            }
        });
    }

    public void viewItem(int ID){

        String name = "";
        String seed = "";

        // Name, Seed

        Cursor res = myDb.getViewData(ID); // Get NAME and SEED from the database and store it in a Cursor object

        if(res.getCount() != 0){ // Database is not empty
            res.moveToNext();
            name = res.getString(0);
            seed = res.getString(1);
        }

        Intent intent = new Intent(MainActivity.this, ViewPassword.class);
        intent.putExtra("com.tomi5548319.getpass.VIEW_NAME", name);
        intent.putExtra("com.tomi5548319.getpass.VIEW_SEED", seed);
        intent.putExtra("com.tomi5548319.getpass.VIEW_KEY", mKey);
        startActivity(intent);
    }

    public void editItem(int ID, int position){

        String name = "";
        String seed = "";

        Cursor res = myDb.getEditData(ID); // Get NAME and SEED from the database and store it in a Cursor object

        if(res.getCount() != 0){ // Database is not empty
            res.moveToNext();
            name = res.getString(0);
            seed = res.getString(1);
        }

        Intent intent = new Intent(MainActivity.this, EditPasswordActivity.class);
        intent.putExtra("com.tomi5548319.getpass.EDIT_ID", ID);
        intent.putExtra("com.tomi5548319.getpass.EDIT_POSITION", position);
        intent.putExtra("com.tomi5548319.getpass.EDIT_NAME", name);
        startActivityForResult(intent, 4);

    }
	
	public void deleteItem(int position, int ID){
        mRecyclerList.remove(position); // Remove the item from recycler list
        mAdapter.notifyItemRemoved(position); // Make an animation
        int deleted = myDb.deleteData(ID); // Remove the item from the database
        if (deleted == 1) { // Item successfully deleted
            Toast.makeText(this, "Successfully deleted 1 item", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Error, please submit a bug report", Toast.LENGTH_LONG).show();
        }
    }

    public void changeItem(int position, String text){ // TODO Handle onItemClick here (recycler view item)
        mRecyclerList.get(position).changeText1(text); // Change recycler view item
        mAdapter.notifyItemChanged(position); // Make an animation
    }
	
	@Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) { // Drawer is opened
            drawer.closeDrawer(GravityCompat.START); // Close drawer
        } else { // Drawer is closed
            super.onBackPressed(); // Act as normal
        }
    }
}



// request codes used: 1,2,3,4 - Edit

// Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

/*Runnable r = new Runnable() {
                @Override
                public void run(){
                    doSomething(); //<-- put your code in here.
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 1000); // <-- the "1000" is the delay time in miliseconds.*/

/*BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String action = intent.getAction();
                                if(action.equals("finish_activity")){
                                    finish();
                                }
                            }
                        };
                        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));*/