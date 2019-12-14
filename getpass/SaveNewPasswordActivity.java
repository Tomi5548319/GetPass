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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_password);
        setTitle(R.string.title_activity_save);

        // Give access only to this app
        if(getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_NAME") || getIntent().hasExtra("com.example.tomi.getpassv100.SAVE_KEY")) {
            final String name = getIntent().getExtras().getString("com.example.tomi.getpassv100.SAVE_NAME");
            final String key = getIntent().getExtras().getString("com.example.tomi.getpassv100.SAVE_KEY");

            String returnValue = Password.generate(name, key);


            mTextViewPassword = findViewById(R.id.textView_save_key);
            mTextViewPassword.setText(returnValue);

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
                    mTextViewPassword.setText(Password.generate(name, key));
                }
            });
        }
    }

private void endThisActivity(){
    String seed = Password.getSeed();

    Intent returnIntent = new Intent();
    returnIntent.putExtra("SEED",seed);
    setResult(Activity.RESULT_OK,returnIntent);
    finish();
}
}
