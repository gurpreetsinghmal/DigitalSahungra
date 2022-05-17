package com.technominds.digitalsahungrav2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class Auth_activity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText number;
    Button next;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        countryCodePicker = findViewById(R.id.cpp);
        number = findViewById(R.id.phone_number);
        next = findViewById(R.id.next);
        countryCodePicker.registerCarrierNumberEditText(number);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(number.getText().toString())) {
                    number.setError("Number is required");
                } else if (number.getText().toString().replace(" ", "").length() != 10) {
                    number.setError("Enter correct number");
                } else {



                    Intent i = new Intent(Auth_activity.this, Verification.class);
                    i.putExtra("number", countryCodePicker.getFullNumberWithPlus().replace(" ", ""));
                    startActivity(i);
                    finish();


                }}
        });
    }
    }



