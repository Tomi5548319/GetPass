package com.tomi5548319.getpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditPasswordActivity extends AppCompatActivity {

    private int mID;
    private int mPosition;
    private String mName;
	private EditText mEditTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);


        // Give access only to this app
        if(getIntent().hasExtra("com.tomi5548319.getpass.EDIT_ID")
		&& getIntent().hasExtra("com.tomi5548319.getpass.EDIT_POSITION")
		&& getIntent().hasExtra("com.tomi5548319.getpass.EDIT_NAME")) {

            mID = getIntent().getExtras().getInt("com.tomi5548319.getpass.EDIT_ID");
            mPosition = getIntent().getExtras().getInt("com.tomi5548319.getpass.EDIT_POSITION");
            mName = getIntent().getExtras().getString("com.tomi5548319.getpass.EDIT_NAME");

            mEditTextName = findViewById(R.id.editText_edit_name);
            mEditTextName.setText(mName);
			
            Button buttonSave = findViewById(R.id.button_edit_save);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mName = mEditTextName.getText().toString();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("ID", mID);
                    returnIntent.putExtra("POSITION", mPosition);
                    returnIntent.putExtra("NAME", mName);

                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });

        }

    }
}
