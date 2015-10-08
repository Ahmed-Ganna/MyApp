package com.ganna.faceparse.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.ganna.faceparse.Constants.FBConstants;
import com.ganna.faceparse.R;
import com.ganna.faceparse.ui.joke.JokeScreen;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginScreen extends AppCompatActivity  {
      private Button mBtnFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLogin();
        mBtnFb = (Button) findViewById(R.id.btn_fb_login);
        mBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLogin();
            }
        });
    }

    private void saveNewUserToParse() {

        GraphRequest request = GraphRequest.newMeRequest( AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,GraphResponse response) {
                        String email ="";
                        try {
                            String name = response.getJSONObject().getString("name");
                            if (response.getJSONObject().getString("email")!=null) {
                                email = response.getJSONObject().getString("email");
                            }
                            ParseUser user = ParseUser.getCurrentUser();
                            user.setUsername(name);
                            user.setEmail(email);
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    startActivity(new Intent(LoginScreen.this, JokeScreen.class));
                                    finish();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
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
                    startActivity(new Intent(LoginScreen.this, JokeScreen.class));
                    finish();
                }
            }
        });
    }

}
