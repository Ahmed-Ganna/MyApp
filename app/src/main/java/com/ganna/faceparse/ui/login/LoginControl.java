package com.ganna.faceparse.ui.login;

import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.ganna.faceparse.callbacks.requestscallbacks.UserDetailsCallback;
import com.ganna.faceparse.communications.requests.FbUserRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class LoginControl {

    private FbUserRequest fbUserRequest;
    public LoginControl(){

        fbUserRequest =new FbUserRequest();
    }

    public void getUserDetails(final UserDetailsCallback callback){
        fbUserRequest.makeUserRequest(new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String email ="";
                    Log.d("response", response.toString());
                    String name = response.getJSONObject().getString("name");
                    if (response.getJSONObject().has("email")) {
                        email = response.getJSONObject().getString("email");
                    }
                    callback.onCompleted(name,email);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
