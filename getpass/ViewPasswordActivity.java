package com.tomi5548319.getpass;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewPasswordActivity extends AppCompatActivity {

    private TextView mTextViewPassword;

    private String mName;
    private String mSeed;
    private int mLength;
    private boolean mSmall;
    private boolean mBig;
    private boolean mNumbers;
    private boolean mBasicChars;
    private boolean mAdvancedChars;
    private String mCustomChars;
    private String mKey;

    private String mPassword;
    private Button mButtonShow;
    private Button mButtonHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_password);


        if(accessGranted()) {
            getIntentData();

            mTextViewPassword = findViewById(R.id.textView_view_key);
            mTextViewPassword.setText("*****");

            mPassword = (Password.generate(mName, mSeed, mLength, mSmall, mBig, mNumbers, mBasicChars, mAdvancedChars, mCustomChars, mKey));

            mButtonShow = findViewById(R.id.button_view_show);
            mButtonHide = findViewById(R.id.button_view_hide);

            mButtonShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTextViewPassword.setText(mPassword);
                    mButtonShow.setVisibility(View.INVISIBLE);
                    mButtonHide.setVisibility(View.VISIBLE);
                }
            });

            mButtonHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTextViewPassword.setText("*****");
                    mButtonShow.setVisibility(View.VISIBLE);
                    mButtonHide.setVisibility(View.INVISIBLE);
                }
            });

        }
    }

    private boolean accessGranted(){
        return  getIntent().hasExtra("com.tomi5548319.getpass.VIEW_NAME") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_SEED") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_LENGTH") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_SMALL") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_BIG") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_NUMBERS") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_BASIC_CHARS") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_ADVANCED_CHARS") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_CUSTOM_CHARS") &&
                getIntent().hasExtra("com.tomi5548319.getpass.VIEW_KEY");
    }

    private void getIntentData(){
        mName = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_NAME");
        mSeed = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_SEED");
        mLength = getIntent().getExtras().getInt("com.tomi5548319.getpass.VIEW_LENGTH");
        mSmall = getIntent().getExtras().getBoolean("com.tomi5548319.getpass.VIEW_SMALL");
        mBig = getIntent().getExtras().getBoolean("com.tomi5548319.getpass.VIEW_BIG");
        mNumbers = getIntent().getExtras().getBoolean("com.tomi5548319.getpass.VIEW_NUMBERS");
        mBasicChars = getIntent().getExtras().getBoolean("com.tomi5548319.getpass.VIEW_BASIC_CHARS");
        mAdvancedChars = getIntent().getExtras().getBoolean("com.tomi5548319.getpass.VIEW_ADVANCED_CHARS");
        mCustomChars = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_CUSTOM_CHARS");
        mKey = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_KEY");
    }
}
