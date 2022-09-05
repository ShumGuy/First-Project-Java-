package com.example.systemprototypev2;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class SignupTabFragment extends Fragment {

    TextInputEditText username,fullname, email, password, re_password, mobile_number, drivinglicense;
    TextInputLayout pw;
    Button btn_register;
    ProgressBar loading;
    TextView logintext;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        username = root.findViewById(R.id.textEditUserName);
        fullname = root.findViewById(R.id.textEditFullName);
        email = root.findViewById(R.id.textEditEmail);
        password = root.findViewById(R.id.textEditPassword);
        drivinglicense =root.findViewById(R.id.textEditDrivingLicense);
        re_password = root.findViewById(R.id.textEditRe_Password);
        mobile_number = root.findViewById(R.id.textEditMobileNumber);
        btn_register = root.findViewById(R.id.button_register);
        loading = root.findViewById(R.id.loading);
        logintext = root.findViewById(R.id.loginText);
        pw = root.findViewById(R.id.password);


        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent rt = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(rt);
                //FragmentTransaction rt =getParentFragmentManager().beginTransaction();

                //rt.replace(R.id.view_pager, new LoginTabFragment());
                //rt.commit();


            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String full_name, user_name, pass_word, e_mail, mobile_no, driving_license;
                full_name = String.valueOf(fullname.getText());
                user_name = String.valueOf(username.getText());
                pass_word = String.valueOf(password.getText());
                e_mail = String.valueOf(email.getText());
                mobile_no = String.valueOf(mobile_number.getText());
                driving_license = String.valueOf(drivinglicense.getText());

                Log.d("mylog",full_name);
                Log.d("mylog",user_name);
                Log.d("mylog",pass_word);
                Log.d("mylog",e_mail);
                Log.d("mylog",full_name);
                Log.d("mylog",full_name);


                if(!full_name.equals("") && !user_name.equals("") && !pass_word.equals("") && !e_mail.equals("") && !mobile_no.equals("") && !driving_license.equals("")) {
                    loading.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();

                    if (!validateEmail() | !validateUsername() | !validatePassword() | !validateLicense()) {
                        Toast.makeText(getActivity().getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                        String input = "Email: " + email.getEditableText().toString();
                        input += "\n";
                        input += "Username: " + username.getEditableText().toString();
                        input += "\n";
                        input += "Password: " + password.getEditableText().toString();
                        input += "\n";
                        input += "Driving License" + drivinglicense.getEditableText().toString();

                        Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();
                        Log.d("mylog",input);
                        return;
                    }
                    else
                    {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URl
                                //Creating array for parameters
                                String[] field = new String[6];
                                field[0] = "full_name";
                                field[1] = "user_name";
                                field[2] = "e_mail";
                                field[3] = "driving_license";
                                field[4] = "pass_word";
                                field[5] = "mobile_no";
                                //Creating array for data
                                String[] data = new String[6];
                                data[0] = full_name;
                                data[1] = user_name;
                                data[2] = e_mail;
                                data[3] = driving_license;
                                data[4] = pass_word;
                                data[5] = mobile_no;
                                PutData putData = new PutData("http://192.168.1.144/LoginRegister/signup.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        loading.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        Log.d("mylog","im here");
                                        if (result.equals("Sign Up Success")) {
                                            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            Log.d("mylog",result);
                                        }
                                    }
                                }
                                //End Write and Read data with URL
                            }
                        });
                    }
                }
                else {
                    if (!validateEmail() | !validateUsername() | !validatePassword() | !validateLicense()) {
                        Toast.makeText(getActivity().getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String input = "Email: " + email.getEditableText().toString();
                    input += "\n";
                    input += "Username: " + username.getEditableText().toString();
                    input += "\n";
                    input += "Password: " + password.getEditableText().toString();
                    input += "\n";
                    input += "Driving License: " + drivinglicense.getEditableText().toString();

                    Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();
                    Log.d("mylog",input);
                    }
                }
        });
        return root;
         }

        private boolean validateEmail() {
            String emailInput = email.getEditableText().toString().trim();

            if (emailInput.isEmpty()) {
                email.setError("Field can't be empty");
                return false;

            } else if (!EMAIL_ADDRESS.matcher(emailInput).matches()) {
                email.setError("Please enter a valid email address");
                return false;
            } else {
                email.setError(null);
                return true;

            }
        }

        private boolean validateUsername() {
            String usernameInput = username.getText().toString().trim();

            if (usernameInput.isEmpty()) {
                username.setError("Field can't be empty");
                return false;
            } else if (usernameInput.length() > 15) {
                username.setError("Username too long");
                return false;
            } else {
                username.setError(null);
                return true;
            }
        }

        private boolean validatePassword() {
            String passwordInput = password.getText().toString().trim();

            if (passwordInput.isEmpty()) {
                pw.setPasswordVisibilityToggleEnabled(false);
                password.setError("Field can't be empty");
                return false;
            } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
                pw.setPasswordVisibilityToggleEnabled(false);
                password.setError("Password too weak");

                return false;
            } else {
                password.setError(null);
                return true;
            }
        }

    private boolean validateLicense() {
        String licenseInput = drivinglicense.getText().toString().trim();

        if (licenseInput.isEmpty()) {
            //pw.setPasswordVisibilityToggleEnabled(false);
            drivinglicense.setError("Field can't be empty");
            return false;
        } else if (!LICENSE_PATTERN.matcher(licenseInput).matches()) {
            //pw.setPasswordVisibilityToggleEnabled(false);
            drivinglicense.setError("Invalid Driving License!");

            return false;
        } else {
            drivinglicense.setError(null);
            return true;
        }
    }

        private static final Pattern EMAIL_ADDRESS =
                    Pattern.compile(
                        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +        //Any characters ranging from a to z lower case, upper case and 0-9.
                                "\\@" +
                                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +     //same as previous but ranging from 0 to 64 characters
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
                Pattern.compile("^"+
                        "(?=.*[ST])" +          //at least 1 upper case letter
                        "(?=.*\\d{7})" +        //at least 7 digit numbers
                        "(?=.*[A-JZ])" +        //Letters A J and Z for this portion
                        ".{9,}" +               //At least 9 characters
                        "$");

}
