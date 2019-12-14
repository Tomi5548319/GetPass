package com.tomi5548319.getpass;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewPasswordActivity extends AppCompatActivity {

    private TextView mTextViewPassword;
    private String mPassword;
    private Button mButtonShow;
    private Button mButtonHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_password);


        if(getIntent().hasExtra("com.tomi5548319.getpass.VIEW_NAME") && getIntent().hasExtra("com.tomi5548319.getpass.VIEW_SEED") && getIntent().hasExtra("com.tomi5548319.getpass.VIEW_KEY")) {
            String name = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_NAME");
            String seed = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_SEED");
            String key = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_KEY");

            mTextViewPassword = findViewById(R.id.textView_view_key);

            mPassword = (Password.generate(name, key, seed));

            mTextViewPassword.setText("*****");

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
}
