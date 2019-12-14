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
    private int mLength;
    private boolean mSmall;
    private boolean mBig;
    private boolean mNumbers;
    private boolean mBasicChars;
    private boolean mAdvancedChars;
    private String mCustomChars;
    private String mKey;
    private boolean mAdvancedHidden;

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
                main();

            }
        }
    }

    private void main(){

        // ADVANCED
        mAdvancedHidden = true;

        Button buttonAdvanced = findViewById(R.id.button_add_advanced);
        buttonAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mEditTextLength = findViewById(R.id.editText_add_length);
                mTextViewLength = findViewById(R.id.textView_add_length);
                mSwitchSmall = findViewById(R.id.switch_add_small);
                mSwitchBig = findViewById(R.id.switch_add_big);
                mSwitchNumbers = findViewById(R.id.switch_add_numbers);
                mSwitchBasicCharacters = findViewById(R.id.switch_add_basic_symbols);
                mSwitchAdvancedCharacters = findViewById(R.id.switch_add_advanced_symbols);
                mEditTextCustom = findViewById(R.id.editText_add_custom_characters);
                mTextViewCustom = findViewById(R.id.textView_add_custom_characters);

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
        Button buttonGenerate = findViewById(R.id.button_add_generate);
        buttonGenerate.setOnClickListener(new View.OnClickListener() { // TODO close keyboard
            @Override
            public void onClick(View view) {

                mEditTextName = findViewById(R.id.editText_add_name);

                mName = mEditTextName.getText().toString();

                startSaveNewPasswordActivity(mName, mKey);

                // TODO implement flags

            }
        });
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
                main();
            }
        }
    }
}
