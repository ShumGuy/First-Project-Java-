package com.example.systemprototypev2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.common.sdkinternal.SharedPrefManager;

import org.chromium.base.Log;

public class WelcomePage extends AppCompatActivity {

    ImageButton blueSG_button, getGo_button;
    TextView text_username, profile_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//This line hides the app title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this line allows the application to go fullscreen
        getSupportActionBar().hide();//This line hides the action bar
        setContentView(R.layout.welcome_page);

        blueSG_button = findViewById(R.id.blueSG_button);
        profile_bt = findViewById(R.id.profile_button);
        text_username =findViewById(R.id.text_username);


        //Extract user sign up data
        String id = getIntent().getStringExtra("keyID");
        String fullname = getIntent().getStringExtra("keyfullname");
        String username = getIntent().getStringExtra("keyusername");
        String email = getIntent().getStringExtra("keyemail");
        String password = getIntent().getStringExtra("keypassword");
        String mobile_no = getIntent().getStringExtra("keymobilenumber");
        String driving_license = getIntent().getStringExtra("keydrivinglicense");
        String profile_picture = getIntent().getStringExtra("keyprofilepicture");

        Log.d("mylog", password);

        text_username.setText(username + "!");

        if(username== null){
            text_username.setText(fullname +"!");
        }

       // Log.d("mylog",profile_picture );

       //Log.d("cadlog",fullname);



        profile_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profile = new Intent( getApplicationContext(),UserProfile.class);
                if(profile !=null) {
                    //parse user sign up data to WelcomePage.class
                    profile.putExtra("keyID",id);
                    profile.putExtra("keyfullname", fullname);
                    profile.putExtra("keyusername", username);
                    profile.putExtra("keyemail", email);
                    profile.putExtra("keypassword", password);
                    profile.putExtra("keymobilenumber", mobile_no);
                    profile.putExtra("keydrivinglicense", driving_license);
                    profile.putExtra("keyprofilepicture", profile_picture);
                    startActivity(profile);
                }

            }

        });


        blueSG_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent info = new Intent( getApplicationContext(),CarParkingMap.class);

                //Extract user sign up data
                String id = getIntent().getStringExtra("keyID");
                String fullname = getIntent().getStringExtra("keyfullname");
                String username = getIntent().getStringExtra("keyusername");
                String email = getIntent().getStringExtra("keyemail");
                String password = getIntent().getStringExtra("keypassword");
                String mobile_no = getIntent().getStringExtra("keymobilenumber");
                String driving_license = getIntent().getStringExtra("keydrivinglicense");
                String profile_picture = getIntent().getStringExtra("keyprofilepicture");

                //parse user sign up data to WelcomePage.class
                info.putExtra("keyfullname",fullname);
                info.putExtra("keyusername",username);
                info.putExtra("keyemail",email);
                info.putExtra("keypassword",password);
                info.putExtra("keymobilenumber",mobile_no);
                info.putExtra("keydrivinglicense",driving_license);
                info.putExtra("keyprofilepicture",profile_picture);
                startActivity(info);

            }
        });
    }
}



