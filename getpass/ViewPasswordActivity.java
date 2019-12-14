package com.tomi5548319.getpass;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_password);


        if(getIntent().hasExtra("com.tomi5548319.getpass.VIEW_NAME") && getIntent().hasExtra("com.tomi5548319.getpass.VIEW_SEED") && getIntent().hasExtra("com.tomi5548319.getpass.VIEW_KEY")) {
            String name = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_NAME");
            String seed = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_SEED");
            String key = getIntent().getExtras().getString("com.tomi5548319.getpass.VIEW_KEY");

            final TextView textViewFinalPassword = findViewById(R.id.textView_view_key);

            final String password = (Password.generate(name, key, seed));

            textViewFinalPassword.setText("*****");

            final Button buttonShow = findViewById(R.id.button_view_show);
            final Button buttonHide = findViewById(R.id.button_view_hide);

            buttonShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textViewFinalPassword.setText(password);
                    buttonShow.setVisibility(View.INVISIBLE);
                    buttonHide.setVisibility(View.VISIBLE);
                }
            });

            buttonHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textViewFinalPassword.setText("*****");
                    buttonShow.setVisibility(View.VISIBLE);
                    buttonHide.setVisibility(View.INVISIBLE);
                }
            });

        }
    }
}
