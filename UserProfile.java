package com.example.systemprototypev2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

public class UserProfile extends AppCompatActivity {

    TextView textview_username, textview_fullname;
    EditText et_fullname, et_username, et_password, et_email, et_mobile, et_license;
    ImageView view_userimage, edit_user_image, edit_username, edit_email, edit_license, edit_password;
    Button update_Button, Logout, welcomepage_button;
    ProgressBar progressbar;
    Bitmap profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//This line hides the app title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this line allows the application to go fullscreen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_profile);

        textview_fullname = findViewById(R.id.textview_fullname);
        textview_username = findViewById(R.id.textview_username);
        et_fullname = findViewById(R.id.edittext_fullname);
        et_username = findViewById(R.id.edittext_username);
        et_password = findViewById(R.id.edittext_password);
        et_email = findViewById(R.id.edittext_email);
        et_mobile = findViewById(R.id.edittext_mobile);
        et_license = findViewById(R.id.edittext_license);
        view_userimage = findViewById(R.id.user_image);
        update_Button = findViewById(R.id.update_button);
        progressbar = findViewById(R.id.progressbar);
        Logout = findViewById(R.id.buttonLogout);
        edit_user_image = findViewById(R.id.edit_user_image);
        welcomepage_button = findViewById(R.id.welcomepage_button);
        edit_username = findViewById(R.id.edit_username);
        edit_password = findViewById(R.id.edit_password);
        edit_email = findViewById(R.id.edit_email);
        edit_license = findViewById(R.id.edit_license);


        //Extract user sign up data
        String id = getIntent().getStringExtra("keyID");
        String fullname = getIntent().getStringExtra("keyfullname");
        String username = getIntent().getStringExtra("keyusername");
        String email = getIntent().getStringExtra("keyemail");
        String password = getIntent().getStringExtra("keypassword");
        String mobile_no = getIntent().getStringExtra("keymobilenumber");
        String driving_license = getIntent().getStringExtra("keydrivinglicense");
        String profile_picture = getIntent().getStringExtra("keyprofilepicture");

        //Log.d("mylog", id);
        Log.d("mylog", fullname);
        //Log.d("mylog", username);
        Log.d("mylog", email);
        //Log.d("mylog", password);
        //Log.d("mylog", mobile_no);
        //Log.d("mylog", driving_license);
        //Log.d("mylog", profile_picture);




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            et_fullname.setText(fullname);
            et_username.setText(username);
            et_password.setText(password);
            et_email.setText(email);
            et_mobile.setText(mobile_no);
            et_license.setText(driving_license);
            textview_username.setText(username);
            textview_fullname.setText(fullname);


           /** byte[] encodeByte=Base64.decode(profile_picture, Base64.URL_SAFE);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            edit_user_image.setImageBitmap(bitmap);
            **/
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        welcomepage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WelcomePage.class);

                intent.putExtra("keyID",id);
                intent.putExtra("keyfullname", fullname);
                intent.putExtra("keyusername", username);
                intent.putExtra("keyemail", email);
                intent.putExtra("keypassword", password);
                intent.putExtra("keymobilenumber", mobile_no);
                intent.putExtra("keydrivinglicense", driving_license);
                intent.putExtra("keyprofilepicture", profile_picture);
                startActivity(intent);
                finish();
            }
        });

        edit_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile_picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (profile_picture.resolveActivity(getPackageManager()) == null) {
                    startActivityForResult(profile_picture, 100);


                    //Log.d("my log","im here");
                } else {
                    Toast.makeText(getApplicationContext(), "There is no app that support this action", Toast.LENGTH_SHORT);
                }
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = getIntent();
                AlertDialog.Builder builder = new AlertDialog.Builder(Logout.getContext());
                builder.setTitle("Confirmation PopUp!").
                        setMessage("You sure, that you want to logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(),
                                        MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        update_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap new_prof_pic = profile_image;
                String new_fullname = et_fullname.getText().toString().trim();
                String new_username = et_username.getText().toString().trim();
                String new_password = et_password.getText().toString().trim();
                String new_email = et_email.getText().toString().trim();
                String new_mobile_no = et_mobile.getText().toString().trim();
                String new_license = et_license.getText().toString().trim();


                /**
                 Log.d("Logcat", fullname);
                 Log.d("Logcat", username);
                 Log.d("Logcat", password);
                 Log.d("Logcat", email);
                 Log.d("Logcat", mobile_no);

                 Log.d("Logcat", new_fullname);
                 Log.d("Logcat", new_username);
                 Log.d("Logcat", new_password);
                 Log.d("Logcat", new_email);
                 Log.d("Logcat", new_mobile_no);
                 **/

                //If any of the edit text fields are empty
                if (new_fullname.isEmpty() || new_username.isEmpty() || new_password.isEmpty() || new_email.isEmpty() || new_mobile_no.isEmpty()
                        || new_license.isEmpty() || view_userimage.getDrawable() == null) {
                    Toast.makeText(getApplicationContext(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
                }
                //If every text field is the same
                if (new_fullname.equals(fullname) && new_username.equals(username) && new_password.equals(password)
                        && new_email.equals(email) && new_mobile_no.equals(mobile_no) && new_license.equals(driving_license)
                        && new_prof_pic == view_userimage.getDrawingCache()) {
                    Toast.makeText(getApplicationContext(), "No update detected!", Toast.LENGTH_SHORT).show();
                }
                //If any of the text field is different
                else {
                    //Never validate require text fields
                    if (!validateNewEmail() || !validateNewUsername() || !validateNewPassword() || !validateLicense()) {
                        Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }



                    /**
                    ByteArrayOutputStream profile_pic = new ByteArrayOutputStream();
                    profile_image.compress(Bitmap.CompressFormat.JPEG, 100, profile_pic);
                    byte[] profileImageBytes = profile_pic.toByteArray();
                    String new_profilepic = Base64.encodeToString(profileImageBytes, Base64.DEFAULT);
                     **/

                    String new_profilepic =convertBitmapToString(profile_image);

                    //Log.d("mylog", new_profilepic);

                    /**
                     String input = "Email: " + et_email.getEditableText().toString();
                     input += "\n";
                     input += "Username: " + et_username.getEditableText().toString();
                     input += "\n";
                     input += "Password: " + et_password.getEditableText().toString();

                     Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
                     **/

                    progressbar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URl
                            //Creating array for parameters
                            String[] field = new String[8];
                            field[0] = "id";
                            field[1] = "full_name";
                            field[2] = "user_name";
                            field[3] = "e_mail";
                            field[4] = "pass_word";
                            field[5] = "mobile_no";
                            field[6] = "driving_license";
                            field[7] = "profile_picture";
                            //Creating array for data
                            String[] data = new String[8];

                            if(id == null)
                            {
                                String new_id ="0";
                                data[0] = new_id;
                                data[1] = new_fullname;
                                data[2] = new_username;
                                data[3] = new_email;
                                data[4] = new_password;
                                data[5] = new_mobile_no;
                                data[6] = new_license;
                                data[7] = new_profilepic;
                            }
                            if( id != null) {
                                data[0] = id;
                                data[1] = new_fullname;
                                data[2] = new_username;
                                data[3] = new_email;
                                data[4] = new_password;
                                data[5] = new_mobile_no;
                                data[6] = new_license;
                                data[7] = new_profilepic;

                            }

                            PutData putData = new PutData("http://192.168.1.144/LoginRegister/update.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressbar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.contains("Update Success!")) {
                                        Toast.makeText(getApplicationContext(), "Update was successful!", Toast.LENGTH_SHORT).show();
                                        Log.d("mylog", result);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                                        Log.d("mylog", result);
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            view_userimage.setImageBitmap(bitmap);
            profile_image= bitmap;

        }
    }

    private boolean validateNewEmail() {
        String emailInput = et_email.getEditableText().toString().trim();

        if (emailInput.isEmpty()) {
            edit_email.setVisibility(View.GONE);
            et_email.setError("Field can't be empty");
            return false;

        } else if (!EMAIL_ADDRESS.matcher(emailInput).matches()) {
            edit_email.setVisibility(View.GONE);
            et_email.setError("Please enter a valid email address");
            return false;
        } else {
            et_email.setError(null);
            return true;

        }
    }

    private boolean validateNewUsername() {
        String usernameInput = et_username.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            edit_username.setVisibility(View.GONE);
            et_username.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            edit_username.setVisibility(View.GONE);
            et_username.setError("Username too long");
            return false;
        } else {
            et_username.setError(null);
            return true;
        }
    }

    private boolean validateNewPassword() {
        String passwordInput = et_password.getText().toString().trim();
        //Log.d("Logcat", passwordInput);

        if (passwordInput.isEmpty()) {
            edit_password.setVisibility(View.GONE);
            // pw.setPasswordVisibilityToggleEnabled(false);
            et_password.setError("Field can't be empty");
            return false;

        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            edit_password.setVisibility(View.GONE);
            // pw.setPasswordVisibilityToggleEnabled(false);
            ///Log.d("Logcat", passwordInput);
            et_password.setError("Password too weak");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }
    }

    private boolean validateLicense() {
        String licenseInput = et_license.getText().toString().trim();

        if (licenseInput.isEmpty()) {
            edit_license.setVisibility(View.GONE);
            //pw.setPasswordVisibilityToggleEnabled(false);
            et_license.setError("Field can't be empty");
            return false;
        } else if (!LICENSE_PATTERN.matcher(licenseInput).matches()) {
            edit_license.setVisibility(View.GONE);
            //pw.setPasswordVisibilityToggleEnabled(false);
            et_license.setError("Invalid Driving License!");

            return false;
        } else {
            et_license.setError(null);
            return true;
        }
    }

    private static final Pattern EMAIL_ADDRESS =
            Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                            "\\@" +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                            "(" +
                            "\\." +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                            ")+"
            );

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private static final Pattern LICENSE_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[ST])" +
                    "(?=.*\\d{7})" +
                    "(?=.*[A-JZ])" +
                    ".{9,}" +
                    "$");

    public Bitmap StringToBitMap(String image){
        try{
            byte[] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static String convertBitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}