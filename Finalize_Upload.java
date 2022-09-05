package com.example.systemprototypev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Finalize_Upload extends AppCompatActivity {

    ImageView imageview_car_pt, imageview_battery_pt;
    TextInputEditText Month, Day, Year, city, address, country,
                      zip_code,car_plate_number, battery_life,
                      tz_id, tz_display_name,time;
    Button upload;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//This line hides the app title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this line allows the application to go fullscreen
        getSupportActionBar().hide();//This line hides the action bar

        setContentView(R.layout.activity_finalize_upload);

        city = findViewById(R.id.textEditCity);
        address = findViewById(R.id.textEditAddress);
        country = findViewById(R.id.textEditCountry);
        zip_code = findViewById(R.id.textEditZipCode);
        imageview_car_pt = findViewById(R.id.imageview_carphoto_taking);
        imageview_battery_pt = findViewById(R.id.imageview_batteryphoto_taking);
        car_plate_number = findViewById(R.id.textEditCarPlateNumber);
        battery_life = findViewById(R.id.textEditBatteryLife);
        tz_id = findViewById(R.id.textEditTZID);
        tz_display_name = findViewById(R.id.textEditTZDisplayname);
        time =findViewById(R.id.textEditTime);
        Month = findViewById(R.id.textEditMonth);
        Day = findViewById(R.id.textEditDay);
        Year = findViewById(R.id.textEditYear);
        upload = findViewById(R.id.upload_button);
        progressBar = findViewById(R.id.progressbar);


        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        String[] splitDate = currentTime.toString().split(" ");

        //Log.d("mylog",splitDate[1].trim());
        //Log.d("mylog",tz.getID());

        Month.setText(splitDate[1]+ " " + splitDate[2]);
        Day.setText(splitDate[0]);
        Year.setText(splitDate[5]);
        time.setText(splitDate[3]);
        String month = String.valueOf(Month.getText());
        String day = String.valueOf(Day.getText());
        String year = String.valueOf(Year.getText());
        String Time = String.valueOf(time.getText());

        TimeZone tz = TimeZone.getDefault();

        tz_id.setText(tz.getID());
        tz_display_name.setText(tz.getDisplayName());

        String time_zone_id = String.valueOf(tz_id.getText());
        String time_zone_display_name = String.valueOf(tz_display_name.getText());



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                String Car_Plate_Number = getIntent().getStringExtra("keycarplatenumber");
                String Battery_Life = getIntent().getStringExtra("keybatterylife");
                Intent upload = getIntent();
                Bitmap car_photo_bitmap = (Bitmap) upload.getParcelableExtra("keycarphoto");
                Bitmap battery_photo_bitmap = (Bitmap) upload.getParcelableExtra("keybatteryphoto");


                ByteArrayOutputStream car = new ByteArrayOutputStream();
                car_photo_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, car);
                byte[] carImageBytes = car.toByteArray();
                String car_photo = Base64.encodeToString(carImageBytes, Base64.DEFAULT);

                ByteArrayOutputStream bat = new ByteArrayOutputStream();
                battery_photo_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bat);
                byte[] batImageBytes = bat.toByteArray();
                String bat_photo = Base64.encodeToString(batImageBytes, Base64.DEFAULT);
                //Log.d("mylog","String:"+ bat_photo);


                if (!username.equals("") && !City.equals("") && !Address.equals("") && !Country.equals("") && !Zip_Code.equals("")
                        && !Car_Plate_Number.equals("") && !Battery_Life.equals("")
                        && !time_zone_id.equals("") && !time_zone_display_name.equals("")
                        && !month.equals("") && !day.equals("") && !year.equals("") && !Time.equals("")) {
                    Log.d("my log","Im here");

                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URl
                            //Creating array for parameters
                            String[] field = new String[15];
                            field[0] = "username";
                            field[1] = "City";
                            field[2] = "Address";
                            field[3] = "Country";
                            field[4] = "Zip_Code";
                            field[5] = "Car_Plate_Number";
                            field[6] = "Battery_Life";
                            field[7] = "car_photo";
                            field[8] = "bat_photo";
                            field[9] = "time_zone_id";
                            field[10] = "time_zone_display_name";
                            field[11] = "month";
                            field[12] = "day";
                            field[13] = "year";
                            field[14] = "Time";
                            //Creating array for data
                            String[] data = new String[15];
                            data[0] = username;
                            data[1] = City;
                            data[2] = Address;
                            data[3] = Country;
                            data[4] = Zip_Code;
                            data[5] = Car_Plate_Number;
                            data[6] = Battery_Life;
                            data[7] = car_photo;
                            data[8] = bat_photo;
                            data[9] = time_zone_id;
                            data[10] = time_zone_display_name;
                            data[11] = month;
                            data[12] = day;
                            data[13] = year;
                            data[14] = Time;
                            PutData putData = new PutData("http://192.168.1.144/Upload/upload.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Upload Successful")){
                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                                        Log.d("Cadlog", result);
                                        Intent intent = new Intent(getApplicationContext(),WelcomePage.class);

                                        intent.putExtra("keyID",id);
                                        intent.putExtra("keyfullname", fullname);
                                        intent.putExtra("keyusername", username);
                                        intent.putExtra("keyemail", email);
                                        intent.putExtra("keypassword", password);
                                        intent.putExtra("keymobilenumber", mobile_no);
                                        intent.putExtra("keydrivinglicense", driving_license);
                                        intent.putExtra("keyprofilepicture", profile_picture);

                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                                        Log.d("Cadlog", result);
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                    }
        });

        String City = getIntent().getStringExtra("keycity");
        String Address = getIntent().getStringExtra("keyaddress");
        String Country = getIntent().getStringExtra("keycountry");
        String Zip_Code= getIntent().getStringExtra("keyzipcode");
        String Car_Plate_Number = getIntent().getStringExtra("keycarplatenumber");
        String Battery_Life = getIntent().getStringExtra("keybatterylife");
        Intent upload = getIntent();
        Bitmap car_photo_bitmap = (Bitmap) upload.getParcelableExtra("keycarphoto");
        Bitmap battery_photo_bitmap = (Bitmap) upload.getParcelableExtra("keybatteryphoto");
        //Log.d("mylog","City"+ City);


        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            city.setText(City);
            address.setText(Address);
            country.setText(Country);
            zip_code.setText(Zip_Code);
            car_plate_number.setText(Car_Plate_Number);
            battery_life.setText(Battery_Life);
            imageview_car_pt.setImageBitmap(car_photo_bitmap);
            imageview_battery_pt.setImageBitmap(battery_photo_bitmap);

        }
    }
}