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

        startEnterKeyActivity(); // Password is required in order to access other passwords
    }

    private void startEnterKeyActivity(){
        Intent intent = new Intent(MainActivity.this, EnterKeyActivity.class);
        intent.putExtra("com.tomi5548319.getpass.GET", "KEY");
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : { // AddNewPasswordActivity closed
                if (resultCode == RESULT_OK) { // Activity closed successfully
					// Get data from returned intent
                    String name = data.getStringExtra("NAME");
                    int length = data.getIntExtra("LENGTH", 16);
                    boolean small = data.getBooleanExtra("SMALL", true);
                    boolean big = data.getBooleanExtra("BIG", true);
                    boolean numbers = data.getBooleanExtra("NUMBERS", true);
                    boolean basicChars = data.getBooleanExtra("BASIC_CHARS", true);
                    boolean advancedChars = data.getBooleanExtra("ADVANCED_CHARS", true);
                    String customChars = data.getStringExtra("CUSTOM_CHARS");
                    String seed = data.getStringExtra("SEED");
                    int flag = data.getIntExtra("FLAG", 0);
					
					// Add a new item
                    insertItem(name, length, small, big, numbers, basicChars, advancedChars, customChars, seed, flag);
                }
                break;
            }
            case (3) : { // EnterKeyActivity closed
                if (resultCode == RESULT_OK) { // Activity closed successfully
                    mKey = data.getStringExtra("KEY");
                    main();
                }else{ // EnterKeyActivity didn't close properly, close the app
                    finish();
                }
                break;
            }
            case (4) : { // EditPasswordActivity closed
                if(resultCode == RESULT_OK) { // Activity closed successfully
					// Get data from the returned intent
                    int ID = data.getIntExtra("ID", -1);
                    int position = data.getIntExtra("POSITION", -1);
                    String name = data.getStringExtra("NAME");

                    myDb.updateEditData(ID, name);
                    mRecyclerList.get(position).changeText1(name);
                    mAdapter.notifyItemChanged(position);
                }
                break;
            }
        }
    }
	
	public void insertItem(String name, int length, boolean small, boolean big, boolean numbers, boolean basicChars, boolean advancedChars, String customChars, String seed, int flags){
        boolean inserted = myDb.insertData(name, length, small, big, numbers, basicChars, advancedChars, customChars, seed, flags);
        if (inserted) {
            int ID = myDb.getHighestID();
            mRecyclerList.add(new RecyclerViewItem(ID, R.drawable.ic_android, name));
            mAdapter.notifyItemInserted(mRecyclerList.size());
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
        myDb = new DatabaseHelper(this);
        mRecyclerList = new ArrayList<>();

        Cursor res = myDb.getRecyclerData();

        if(res.getCount() == 0){ // Database is empty
			// TODO Display some text on the screen, like: "You don't have any passwords" + Add a new password button
        }else{
            while(res.moveToNext()){ // Insert data into each row
                mRecyclerList.add(new RecyclerViewItem(res.getInt(0), R.drawable.ic_android, res.getString(1)));
            }
        }
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView1);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapter(mRecyclerList);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

		// Recycler view clicks handling
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(int position) {
                viewItem(mRecyclerList.get(position).getID());
            }

            @Override
            public void onItemClick(int position) { // TODO handle onItemClick here (recycler view item)
                //changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                deleteItem(position, mRecyclerList.get(position).getID());
            }

            @Override
            public void onEditClick(int position) {
                editItem(mRecyclerList.get(position).getID(), position);
            }
        });
    }

    public void viewItem(int ID){

        Cursor res = myDb.getViewData(ID);

        if(res.getCount() != 0){ // Data was acquired successfully
            res.moveToNext();

            String name = res.getString(0);
            String seed = res.getString(1);
            int length = res.getInt(2);
            int smallInt = res.getInt(3);
            int bigInt = res.getInt(4);
            int numbersInt = res.getInt(5);
            int basicCharsInt = res.getInt(6);
            int advancedCharsInt = res.getInt(7);
            String customChars =  res.getString(8);

            // Convert integers to boolean
            boolean small = (smallInt == 1);
            boolean big = (bigInt == 1);
            boolean numbers = (numbersInt == 1);
            boolean basicChars = (basicCharsInt == 1);
            boolean advancedChars = (advancedCharsInt == 1);

			startViewPasswordActivity(name, seed, length, small, big, numbers, basicChars, advancedChars, customChars);
        }
    }
	
	private void startViewPasswordActivity(String name, String seed, int length, boolean small, boolean big, boolean numbers, boolean basicChars, boolean advancedChars, String customChars){
		Intent intent = new Intent(MainActivity.this, ViewPasswordActivity.class);
        intent.putExtra("com.tomi5548319.getpass.VIEW_NAME", name);
        intent.putExtra("com.tomi5548319.getpass.VIEW_SEED", seed);
        intent.putExtra("com.tomi5548319.getpass.VIEW_LENGTH", length);
        intent.putExtra("com.tomi5548319.getpass.VIEW_SMALL", small);
        intent.putExtra("com.tomi5548319.getpass.VIEW_BIG", big);
        intent.putExtra("com.tomi5548319.getpass.VIEW_NUMBERS", numbers);
        intent.putExtra("com.tomi5548319.getpass.VIEW_BASIC_CHARS", basicChars);
        intent.putExtra("com.tomi5548319.getpass.VIEW_ADVANCED_CHARS", advancedChars);
        intent.putExtra("com.tomi5548319.getpass.VIEW_CUSTOM_CHARS", customChars);
        intent.putExtra("com.tomi5548319.getpass.VIEW_KEY", mKey);
        startActivity(intent);
	}

    public void editItem(int ID, int position){

        String name = "";
        //String seed = ""; // TODO regenerate a password using seed

        Cursor res = myDb.getEditData(ID);

        if(res.getCount() != 0){ // Data was acquired successfully
            res.moveToNext();
            name = res.getString(0);
            // seed = res.getString(1);
			startEditPasswordActivity(ID, position, name);
        }

        
    }
	
	private void startEditPasswordActivity(int ID, int position, String name){
		Intent intent = new Intent(MainActivity.this, EditPasswordActivity.class);
        intent.putExtra("com.tomi5548319.getpass.EDIT_ID", ID);
        intent.putExtra("com.tomi5548319.getpass.EDIT_POSITION", position);
        intent.putExtra("com.tomi5548319.getpass.EDIT_NAME", name);
        startActivityForResult(intent, 4);
	}
	
	public void deleteItem(int position, int ID){
        mRecyclerList.remove(position);
        mAdapter.notifyItemRemoved(position); // Make an animation
        int deleted = myDb.deleteData(ID);
        if (deleted == 1) { // Item was deleted successfully
            Toast.makeText(this, "Successfully deleted 1 item", Toast.LENGTH_LONG).show(); // TODO put theese into strings.xml
        }else{
            Toast.makeText(this, "Error, please submit a bug report", Toast.LENGTH_LONG).show(); // TODO add bug reports
        }
    }

    public void changeItem(int position, String text){ // TODO Handle onItemClick here (recycler view item)
        mRecyclerList.get(position).changeText1(text); // Change recycler view item
        mAdapter.notifyItemChanged(position); // Make an animation
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
}



// request codes used: 1,2,3,4

// test this before usage - Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
// this works - Toast.makeText(this, "Successfully deleted 1 item", Toast.LENGTH_LONG).show();

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