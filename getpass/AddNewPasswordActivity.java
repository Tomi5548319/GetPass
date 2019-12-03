package com.tomi5548319.getpass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNewPasswordActivity extends AppCompatActivity{

    private EditText mEditTextName;

    private String mName;
    private String mKey;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_password);
        setTitle(R.string.title_activity_add);

        // Give access only to this app
        if(getIntent().hasExtra("com.tomi5548319.getpass.ADD")) {
            //String key = (String) getIntent().getExtras().getString("com.tomi5548319.getpass.ADD");
            if(getIntent().getExtras().getString("com.tomi5548319.getpass.ADD").equals("ADD NEW PASSWORD!")) {

                mKey = getIntent().getExtras().getString("com.tomi5548319.getpass.ADD_KEY");
                onCreate();

            }
        }
    }

    private void onCreate(){

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.Advanced_generation_settings);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        /*expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                TextView test = (TextView) findViewById(R.id.textViewAddTest);
                test.setVisibility(View.VISIBLE);
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                TextView test = (TextView) findViewById(R.id.textViewAddTest);
                test.setVisibility(View.GONE);
            }
        });*/

        // GENERATE
        Button buttonGenerate = (Button) findViewById(R.id.button_add_generate);
        buttonGenerate.setOnClickListener(new View.OnClickListener() { // TODO close keyboard
            @Override
            public void onClick(View view) {

                mEditTextName = (EditText) findViewById(R.id.editText_add_name);
                //mEditTextKey = (EditText) findViewById(R.id.editTextKey);

                mName = mEditTextName.getText().toString();
                //String key = mEditTextKey.getText().toString();

                startSaveNewPasswordActivity(mName, mKey);

                //String returnValue = Password.generate(mName, password);
                //mTextViewPassword.setText(returnValue);

                        /*mSeed = Password.getSeed();
                        mFlag = 123; // TODO implement flags*/

            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding header data
        listDataHeader.add("Advanced");
        /*listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");*/

        // Adding child data
        List<String> adv = new ArrayList<String>();
        adv.add("Password length");
        adv.add("a-z ");
        adv.add("A-Z");
        adv.add("0-9");
        adv.add("Basic Symbols        ,.,-?:_" + 0x22 + "!/;*+@()%");
        adv.add("Advanced Symbols,|" + 0x5c + "€$&^<>~'[]{}");
        adv.add("Custom characters:");
        /*top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");*/

        listDataChild.put(listDataHeader.get(0), adv); // Header, Child data
        /*listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
    }

    private void startSaveNewPasswordActivity(String name, String key){
        Intent intent = new Intent(AddNewPasswordActivity.this, SaveNewPasswordActivity.class);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_NAME", name);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_KEY", key);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (2) : { // SaveNewPasswordActivity closed by clicking SAVE
                if (resultCode == RESULT_OK) { // Activity closed successfully

                    String seed = data.getStringExtra("SEED");

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("NAME",mName);
                    returnIntent.putExtra("SEED",seed);
                    returnIntent.putExtra("FLAG",123);

                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                break;
            }
            default: { // SaveNewPasswordActivity closed, but SAVE was not clicked
                onCreate();
            }
        }
    }
}
