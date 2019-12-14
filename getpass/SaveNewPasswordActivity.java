package com.tomi5548319.getpass;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SaveNewPasswordActivity extends AppCompatActivity {

    private TextView mTextViewPassword;

    private String mName;
    private int mLength;
    private boolean mSmall;
    private boolean mBig;
    private boolean mNumbers;
    private boolean mBasicChars;
    private boolean mAdvancedChars;
    private String mCustomChars;
    private String mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_password);
        setTitle(R.string.title_activity_save);

        // Give access only to this app
        if(accessGranted()) {

            getIntentData();

            String password = Password.generate(mName, mLength, mSmall, mBig, mNumbers, mBasicChars, mAdvancedChars, mCustomChars, mKey);

            mTextViewPassword = findViewById(R.id.textView_save_key);
            mTextViewPassword.setText(password);

            Button buttonSave = findViewById(R.id.button_save_save);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    endThisActivity();
                }
            });

            Button buttonRegenerate = findViewById(R.id.button_save_regenerate);
            buttonRegenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTextViewPassword.setText(Password.generate(mName, mLength, mSmall, mBig, mNumbers, mBasicChars, mAdvancedChars, mCustomChars, mKey));
                }
            });
        }
    }

    private boolean accessGranted(){
        return  getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_NAME") &&
                getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_LENGTH") &&
                getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_SMALL") &&
                getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_BIG") &&
                getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_NUMBERS") &&
                getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_BASIC_CHARS") &&
                getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_ADVANCED_CHARS") &&
                getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_CUSTOM_CHARS") &&
                getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_KEY");
    }

    private void getIntentData(){
        mName = getIntent().getExtras().getString("com.example.tomi.getpassv100.SAVE_NAME");
        mLength = getIntent().getExtras().getInt("com.example.tomi.getpassv100.SAVE_LENGTH");
        mSmall = getIntent().getExtras().getBoolean("com.example.tomi.getpassv100.SAVE_SMALL");
        mBig = getIntent().getExtras().getBoolean("com.example.tomi.getpassv100.SAVE_BIG");
        mNumbers = getIntent().getExtras().getBoolean("com.example.tomi.getpassv100.SAVE_NUMBERS");
        mBasicChars = getIntent().getExtras().getBoolean("com.example.tomi.getpassv100.SAVE_BASIC_CHARS");
        mAdvancedChars = getIntent().getExtras().getBoolean("com.example.tomi.getpassv100.SAVE_ADVANCED_CHARS");
        mCustomChars = getIntent().getExtras().getString("com.example.tomi.getpassv100.SAVE_CUSTOM_CHARS");
        mKey = getIntent().getExtras().getString("com.example.tomi.getpassv100.SAVE_KEY");
    }

    private void endThisActivity(){
        String seed = Password.getSeed();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("SEED",seed);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
