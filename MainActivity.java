package com.example.systemprototypev2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton facebook,google,twitter;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_pager,new SignupTabFragment());
        ft.commit();
        requestWindowFeature(Window.FEATURE_NO_TITLE);//This line hides the app title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // this line allows the application to go fullscreen
        getSupportActionBar().hide();//This line hides the action bar
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        //facebook = findViewById(R.id.fab_facebook);
        google = findViewById(R.id.fab_google);
        //twitter = findViewById(R.id.fab_twitter);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct=GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            navigateToGoogleLogin();
        }

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });


        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //facebook.setTranslationY(300);
        google.setTranslationY(300);
        //twitter.setTranslationY(300);
        tabLayout.setTranslationY(300);

        //facebook.setAlpha(v);
        google.setAlpha(v);
        //twitter.setAlpha(v);
        tabLayout.setAlpha(v);

        //facebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        //twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();


    }
    void signUp(){
        Intent signUpIntent = gsc.getSignInIntent();
        startActivityForResult(signUpIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToGoogleLogin();
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Something went wrong with the google login",Toast.LENGTH_SHORT).show();
            }
        }
    }
    void navigateToGoogleLogin(){
        finish();
        Intent Google = new Intent(MainActivity.this, GoogleLogin.class);
        startActivity(Google);
    }

}