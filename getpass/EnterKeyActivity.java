package com.tomi5548319.getpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterKeyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_key);

        // Give access only to this app
        if(getIntent().hasExtra("com.tomi5548319.getpass.GET")) {
            String passKey = (String) getIntent().getExtras().getString("com.tomi5548319.getpass.GET");
            if(passKey.equals("KEY")) {
                main();
            }
        }
    }

    private void main(){
        Button buttonOk = (Button) findViewById(R.id.button_enter_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextKey = findViewById(R.id.editText_enter_key);
                String key = editTextKey.getText().toString();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("KEY", key);

                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
