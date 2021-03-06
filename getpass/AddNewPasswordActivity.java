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
import android.widget.Toast;

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
        if(accessGranted()) {
            mKey = getIntent().getExtras().getString("com.tomi5548319.getpass.ADD_KEY");
            main();
        }
    }

    private boolean accessGranted(){
        if(getIntent().hasExtra("com.tomi5548319.getpass.ADD")) {
            if(getIntent().getExtras().getString("com.tomi5548319.getpass.ADD").equals("ADD NEW PASSWORD!")) {
                return true;
            }
        }
        return false;
    }

    private void main(){

        mEditTextLength = findViewById(R.id.editText_add_length);
        mTextViewLength = findViewById(R.id.textView_add_length);
        mSwitchSmall = findViewById(R.id.switch_add_small);
        mSwitchBig = findViewById(R.id.switch_add_big);
        mSwitchNumbers = findViewById(R.id.switch_add_numbers);
        mSwitchBasicCharacters = findViewById(R.id.switch_add_basic_symbols);
        mSwitchAdvancedCharacters = findViewById(R.id.switch_add_advanced_symbols);
        mEditTextCustom = findViewById(R.id.editText_add_custom_characters);
        mTextViewCustom = findViewById(R.id.textView_add_custom_characters);

        // ADVANCED
        mAdvancedHidden = true;

        Button buttonAdvanced = findViewById(R.id.button_add_advanced);
        buttonAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                mLength = Integer.parseInt(mEditTextLength.getText().toString());
                mSmall = mSwitchSmall.isChecked();
                mBig = mSwitchBig.isChecked();
                mNumbers = mSwitchNumbers.isChecked();
                mBasicChars = mSwitchBasicCharacters.isChecked();
                mAdvancedChars = mSwitchAdvancedCharacters.isChecked();
                mCustomChars = mEditTextCustom.getText().toString();

                if(dataIsOkay())
                    startSaveNewPasswordActivity();
                else
                    Toast.makeText(AddNewPasswordActivity.this, "Can't generate a password with data you entered", Toast.LENGTH_LONG).show();

                // TODO implement flags
            }
        });
    }

    private boolean dataIsOkay(){
        return (mLength > 0 &&
                (mSmall || mBig || mNumbers || mBasicChars || mAdvancedChars || !mCustomChars.equals("")));
    }

    private void startSaveNewPasswordActivity(){
        Intent intent = new Intent(AddNewPasswordActivity.this, SaveNewPasswordActivity.class);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_NAME", mName);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_LENGTH", mLength);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_SMALL", mSmall);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_BIG", mBig);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_NUMBERS", mNumbers);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_BASIC_CHARS", mBasicChars);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_ADVANCED_CHARS", mAdvancedChars);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_CUSTOM_CHARS", mCustomChars);
        intent.putExtra("com.example.tomi.getpassv100.SAVE_KEY", mKey);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (2) : { // SaveNewPasswordActivity closed
                if (resultCode == RESULT_OK) { // Activity closed successfully

                    String seed = Password.getSeed();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("NAME",mName);
                    returnIntent.putExtra("LENGTH",mLength);
                    returnIntent.putExtra("SMALL",mSmall);
                    returnIntent.putExtra("BIG",mBig);
                    returnIntent.putExtra("NUMBERS",mNumbers);
                    returnIntent.putExtra("BASIC_CHARS",mBasicChars);
                    returnIntent.putExtra("ADVANCED_CHARS",mAdvancedChars);
                    returnIntent.putExtra("CUSTOM_CHARS",mCustomChars);
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
