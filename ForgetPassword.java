package com.example.systemprototypev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.w3c.dom.Text;


public class ForgetPassword extends AppCompatActivity {

    TextView return_main_page;
    EditText retrieve_username,retrieve_mobile_number, retrieved_password;
    Button forget_password_button, showpassword_button;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//This line hides the app title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this line allows the application to go fullscreen
        getSupportActionBar().hide();//This line hides the action bar
        setContentView(R.layout.activity_forget_password);

        return_main_page = findViewById(R.id.return_main_activity);
        retrieve_username = findViewById(R.id.textEditRetrieveUsername);
        retrieve_mobile_number = findViewById(R.id.textEditRetrieveMobile);
        retrieved_password = findViewById(R.id.textEditRetrievedPassword);
        forget_password_button = findViewById(R.id.forget_password_button);
        showpassword_button = findViewById(R.id.show_password_button);
        progressbar = findViewById(R.id.progressbar);

        return_main_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        forget_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String rt_username = String.valueOf(retrieve_username.getText());
                    String rt_mobile = String.valueOf(retrieve_mobile_number.getText());

                    if(rt_username.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please key in username!", Toast.LENGTH_SHORT).show();
                    }
                    if(rt_mobile.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please key in mobile number!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    progressbar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URl
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "user_name";
                            field[1] = "mobile_no";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = rt_username;
                            data[1] = rt_mobile;
                            PutData putData = new PutData("http://192.168.1.144/LoginRegister/Retrieve_Password.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressbar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.contains("Password Retrieved!")) {
                                        Toast.makeText(getApplicationContext(), "Password Retrieved!", Toast.LENGTH_SHORT).show();
                                        Log.d("mylog", result);

                                        showpassword_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String change = result.replaceAll("[\",:{}/[\\[\\]']]", " ");
                                                String[] datas = change.split("   ");
                                                Log.d("mylog",change);
                                                retrieved_password.setText(datas[4]);
                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Retrieved Failed! Invalid Username or Mobile number!", Toast.LENGTH_SHORT).show();
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
}