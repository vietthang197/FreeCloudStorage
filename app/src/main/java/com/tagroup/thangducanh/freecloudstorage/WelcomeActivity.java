package com.tagroup.thangducanh.freecloudstorage;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private ProgressBar loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
        initFacebookSignIn();
        initFirebaseAuth();
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }
    // init facebook sdk
    private void initFacebookSignIn() {
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    // init view
    private void initView() {
        loadingBar = findViewById(R.id.loading_bar);
        loadingBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN );
    }

    // check if user logged in
    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (null == currentUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intentLogin = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
            }, 2000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intentMain = new Intent(WelcomeActivity.this, MainActivity.class);
                    intentMain.putExtra("currentUser", currentUser);
                    startActivity(intentMain);
                }
            }, 1000);
        }
    }
}
