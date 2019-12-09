package com.tomi5548319.getpass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class AddNewPasswordActivity extends AppCompatActivity{

    private EditText mEditTextName;
    private EditText mEditTextLength;
    private TextView mTextViewLength;
    private Switch mSwitchSmall;
    private Switch mSwitchBig;
    private Switch mSwitchNumbers;
    private Switch mSwitchBasicCharacters;
    private Switch mSwitchAdvancedCharacters;
    private EditText mEditTextCustom;
    private TextView mTextViewCustom;

    private String mName;
    private String mKey;
    private boolean mAdvancedHidden;

    //ExpandableListAdapter listAdapter;
    //ExpandableListView expListView;
    //List<String> listDataHeader;
    //HashMap<String, List<String>> listDataChild;

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
        //expListView = findViewById(R.id.Advanced_generation_settings);

        // preparing list data
        // prepareListData();

        // listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        // expListView.setAdapter(listAdapter);

        /*expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                TextView test = (TextView) findViewById(R.id.textView_list_item0);
                test.setVisibility(View.VISIBLE);
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                TextView test = (TextView) findViewById(R.id.textView_list_item0);
                test.setVisibility(View.GONE);
            }
        });*/



        // ADVANCED
        mAdvancedHidden = true;

        Button buttonAdvanced = (Button) findViewById(R.id.button_add_advanced);
        buttonAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mEditTextLength = (EditText) findViewById(R.id.editText_add_length);
                mTextViewLength = (TextView) findViewById(R.id.textView_add_length);
                mSwitchSmall = (Switch) findViewById(R.id.switch_add_small);
                mSwitchBig = (Switch) findViewById(R.id.switch_add_big);
                mSwitchNumbers = (Switch) findViewById(R.id.switch_add_numbers);
                mSwitchBasicCharacters = (Switch) findViewById(R.id.switch_add_basic_symbols);
                mSwitchAdvancedCharacters = (Switch) findViewById(R.id.switch_add_advanced_symbols);
                mEditTextCustom = (EditText) findViewById(R.id.editText_add_custom_characters);
                mTextViewCustom = (TextView) findViewById(R.id.textView_add_custom_characters);

                if(mAdvancedHidden){
                    mEditTextLength.setVisibility(View.VISIBLE);
                    mTextViewLength.setVisibility(View.VISIBLE);
                    mSwitchSmall.setVisibility(View.VISIBLE);
                    mSwitchBig.setVisibility(View.VISIBLE);
                    mSwitchNumbers.setVisibility(View.VISIBLE);
                    mSwitchBasicCharacters.setVisibility(View.VISIBLE);
                    mSwitchAdvancedCharacters.setVisibility(View.VISIBLE);
                    mEditTextCustom.setVisibility(View.VISIBLE);
                    mTextViewCustom.setVisibility(View.VISIBLE);

                    mAdvancedHidden = false;
                }else{
                    mEditTextLength.setVisibility(View.GONE);
                    mTextViewLength.setVisibility(View.GONE);
                    mSwitchSmall.setVisibility(View.GONE);
                    mSwitchBig.setVisibility(View.GONE);
                    mSwitchNumbers.setVisibility(View.GONE);
                    mSwitchBasicCharacters.setVisibility(View.GONE);
                    mSwitchAdvancedCharacters.setVisibility(View.GONE);
                    mEditTextCustom.setVisibility(View.GONE);
                    mTextViewCustom.setVisibility(View.GONE);

                    mAdvancedHidden = true;
                }

            }
        });

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
    /*private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding header data
        listDataHeader.add("Advanced");
        /*listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

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
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), adv); // Header, Child data
        /*listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }*/

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
