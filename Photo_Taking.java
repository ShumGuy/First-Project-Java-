package com.example.systemprototypev2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Photo_Taking extends AppCompatActivity {

    ImageView imageview_car_pt, imageview_battery_pt;
    Button photo_battery_button, photo_car_button, cf_image_button;
    TextInputEditText car_plate_number, battery_life;
    Bitmap car_photo, battery_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//This line hides the app title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this line allows the application to go fullscreen
        getSupportActionBar().hide();//This line hides the action bar

        setContentView(R.layout.activity_photo_taking);

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

        imageview_car_pt = findViewById(R.id.imageview_carphoto_taking);
        imageview_battery_pt = findViewById(R.id.imageview_batteryphoto_taking);
        photo_battery_button = findViewById(R.id.batteryphoto_button);
        photo_car_button = findViewById(R.id.carphoto_button);
        cf_image_button = findViewById(R.id.cf_photo_button);
        car_plate_number = findViewById(R.id.textEditCarPlateNumber);
        battery_life = findViewById(R.id.textEditBatteryLife);

        //Request Camera usage permission within the app
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA
            },100);
        }
        //Upon clicking the button, the app will allow the user to take a photo with the mobiles camera and it will link the image to requestCode 100
        photo_car_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent car_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (car_photo.resolveActivity(getPackageManager()) == null) {
                            startActivityForResult(car_photo, 100);
                            //Log.d("my log","im here");
                        } else {
                            Toast.makeText(getApplicationContext(), "There is no app that support this action", Toast.LENGTH_SHORT);
                        }
                }
            });

        photo_battery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent battery_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(battery_photo.resolveActivity(getPackageManager()) == null){
                    startActivityForResult(battery_photo,101);
                }
            }
        });

        cf_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(battery_life.getText().toString())) {
                    //Log.d("mylog","I'm here!");
                    Toast.makeText(getApplicationContext(), "Please enter battery life percent!", Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(car_plate_number.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter car plate number!", Toast.LENGTH_SHORT).show();
                }
                if(imageview_car_pt.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), "Please take an image of the car!", Toast.LENGTH_SHORT).show();
                }
                if(imageview_battery_pt.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), "Please take an image of the battery life!", Toast.LENGTH_SHORT).show();
                } else {

                    String bt_life = battery_life.getText().toString();
                    String car_plate_n = car_plate_number.getText().toString();
                    int bat_life = Integer.parseInt(bt_life);
                    Log.d("mylog","value of "+ bat_life);


                    if (bat_life < 36 && bat_life > 0) {
                        Toast.makeText(Photo_Taking.this, "Battery life too low! Please park this vehicle at the charging station!", Toast.LENGTH_SHORT).show();
                    }
                    if (bat_life < 0 || bat_life > 100) {
                        Toast.makeText(Photo_Taking.this, "Irrelevant Battery Life! Please enter battery life in range of 0-100!", Toast.LENGTH_SHORT).show();
                        //Log.d("mylog","I'm here!");
                    }
                    if (bat_life > 35 && bat_life < 101){
                        if(!car_plate_n.equals("") && imageview_car_pt.getDrawable() != null && imageview_battery_pt.getDrawable() !=null){
                            Intent finalize = new Intent(getApplicationContext(), Finalize_Upload.class);

                            finalize.putExtra("keyID",id);
                            finalize.putExtra("keyfullname", fullname);
                            finalize.putExtra("keyusername", username);
                            finalize.putExtra("keyemail", email);
                            finalize.putExtra("keypassword", password);
                            finalize.putExtra("keymobilenumber", mobile_no);
                            finalize.putExtra("keydrivinglicense", driving_license);
                            finalize.putExtra("keyprofilepicture", profile_picture);
                            finalize.putExtra("keyusername",username);
                            finalize.putExtra("keycity",City);
                            finalize.putExtra("keyaddress",Address);
                            finalize.putExtra("keycountry",Country);
                            finalize.putExtra("keyzipcode",Zip_Code);
                            finalize.putExtra("keycarplatenumber",car_plate_n);
                            finalize.putExtra("keybatterylife",bt_life);
                            finalize.putExtra("keycarphoto",car_photo);
                            finalize.putExtra("keybatteryphoto",battery_photo);

                            startActivity(finalize);
                        }
                    }
                }

            }
        });
    }

// function call which assigns the camera image into a bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data !=null){
            Bundle bundle = data.getExtras();
            Bitmap car_pt = (Bitmap) bundle.get("data");
            imageview_car_pt.setImageBitmap(car_pt);
            car_photo = car_pt;

        }

        if(requestCode == 101 && resultCode == RESULT_OK && data !=null) {
            Bundle bundle = data.getExtras();
            Bitmap battery_pt = (Bitmap) bundle.get("data");
            imageview_battery_pt.setImageBitmap(battery_pt);
            battery_photo = battery_pt;

        }
    }
}