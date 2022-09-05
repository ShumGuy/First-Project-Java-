package com.example.systemprototypev2;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.chromium.base.Log;
import org.chromium.base.task.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class LoginTabFragment extends Fragment {

    TextView forgetPass;
    Button login_btn;
    TextInputEditText pass, username_login;
    ProgressBar progressBar;

    String fullname, username, password, email, mobile_no;




    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        ViewGroup root =(ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        username_login = root.findViewById(R.id.textEditUserNameLogin);
        pass = root.findViewById(R.id.textEditPassword);
        forgetPass = root.findViewById(R.id.forget_pass);
        login_btn = root.findViewById(R.id.login_button);
        progressBar = root.findViewById(R.id.progressbar);


        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForgetPassword.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name, pass_word;
                user_name = String.valueOf(username_login.getText());
                pass_word = String.valueOf(pass.getText());



                if(!user_name.equals("") && !pass_word.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URl
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "user_name";
                            field[1] = "pass_word";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = user_name;
                            data[1] = pass_word;
                            PutData putData = new PutData("http://192.168.1.144/LoginRegister/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.contains("Login Success")){
                                        Toast.makeText(getActivity().getApplicationContext(),"Login Success!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity().getApplicationContext(),WelcomePage.class);

                                        String change = result.replaceAll("[\",:{}/[\\[\\]']]", " ");
                                        String[] datas = change.split("   ");

                                        intent.putExtra("keyID", datas[2]);
                                        intent.putExtra("keyfullname",datas[4]);
                                        intent.putExtra("keyusername",datas[6]);
                                        intent.putExtra("keypassword", datas[8]);
                                        intent.putExtra("keyemail",datas[10]);
                                        intent.putExtra("keymobilenumber",datas[12]);
                                        intent.putExtra("keydrivinglicense",datas[14]);
                                        intent.putExtra("keyprofilepicture",datas[16]);

                                        //Log.d("Logcat",change);
                                        /**
                                        Log.d("Logcat",result);
                                        Log.d("Logcat",datas[2]);
                                        Log.d("Logcat",datas[4]);
                                        Log.d("Logcat",datas[6]);
                                        Log.d("Logcat",datas[14]);
                                         **/




                                        getActivity().startActivity(intent);
                                        getActivity().finish();

                                    }
                                    else{
                                        Toast.makeText(getActivity().getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}
