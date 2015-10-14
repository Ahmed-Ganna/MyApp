package com.ganna.faceparse.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ganna.faceparse.Constants.FBConstants;
import com.ganna.faceparse.R;
import com.ganna.faceparse.callbacks.requestscallbacks.UserDetailsCallback;
import com.ganna.faceparse.managers.ScreenManager;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class LoginScreen extends AppCompatActivity  {
    private Button mBtnFb;
    private LoginControl controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initLogin();
    }

    private void init() {
        controller = new LoginControl();
        mBtnFb = (Button) findViewById(R.id.btn_fb_login);
        mBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLogin();
            }
        });
    }

    private void saveNewUserToParse() {
        controller.getUserDetails(new UserDetailsCallback() {
            @Override
            public void onCompleted(String name, String email) {
                ParseUser user = ParseUser.getCurrentUser();
                user.setUsername(name);
                user.setEmail(email);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ScreenManager.launchJokeScreen(LoginScreen.this);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void initLogin(){
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginScreen.this, FBConstants.permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {

                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    saveNewUserToParse();
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    ScreenManager.launchJokeScreen(LoginScreen.this);
                }
            }
        });
    }

}
