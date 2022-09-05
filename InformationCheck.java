package com.example.systemprototypev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class InformationCheck extends AppCompatActivity {

    TextInputEditText city, address, country, zip_code;
    Button cf_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//This line hides the app title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this line allows the application to go fullscreen
        getSupportActionBar().hide();//This line hides the action bar

        setContentView(R.layout.activity_information_check);

        city = findViewById(R.id.textEditCity);
        address = findViewById(R.id.textEditAddress);
        country = findViewById(R.id.textEditCountry);
        zip_code = findViewById(R.id.textEditZipCode);
        cf_button = findViewById(R.id.cf_button);


        String id = getIntent().getStringExtra("keyID");
        String fullname = getIntent().getStringExtra("keyfullname");
        String username = getIntent().getStringExtra("keyusername");
        String email = getIntent().getStringExtra("keyemail");
        String password = getIntent().getStringExtra("keypassword");
        String mobile_no = getIntent().getStringExtra("keymobilenumber");
        String driving_license = getIntent().getStringExtra("keydrivinglicense");
        String profile_picture = getIntent().getStringExtra("keyprofilepicture");
        String City = getIntent().getStringExtra("keycity");
        String Address = getIntent().getStringExtra("keyaddress");
        String Country = getIntent().getStringExtra("keycountry");
        String Zip_Code= getIntent().getStringExtra("keyzipcode");

        //Log.d("mylog","Address:"+ City);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            city.setText(City);
            address.setText(Address);
            country.setText(Country);
            zip_code.setText(Zip_Code);
        }

        cf_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photo = new Intent(getApplicationContext(),Photo_Taking.class);


                photo.putExtra("keyID",id);
                photo.putExtra("keyfullname", fullname);
                photo.putExtra("keyusername", username);
                photo.putExtra("keyemail", email);
                photo.putExtra("keypassword", password);
                photo.putExtra("keymobilenumber", mobile_no);
                photo.putExtra("keydrivinglicense", driving_license);
                photo.putExtra("keyprofilepicture", profile_picture);
                photo.putExtra("keyusername",username);
                photo.putExtra("keycity",City);
                photo.putExtra("keyaddress",Address);
                photo.putExtra("keycountry",Country);
                photo.putExtra("keyzipcode",Zip_Code);
                startActivity(photo);
            }
        });
    }
}