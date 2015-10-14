package com.ganna.faceparse.communications.requests;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.ganna.faceparse.Constants.FBConstants;
import com.ganna.faceparse.callbacks.requestscallbacks.UserDetailsCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class FbUserRequest {

    public FbUserRequest(){

    }

    public void makeUserRequest(final UserDetailsCallback callback ){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
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
        Bundle parameters = new Bundle();
        parameters.putString(FBConstants.FIELDS_NAME_SPACE, FBConstants.USER_FIELDS);
        request.setParameters(parameters);
        request.executeAsync();
    }
}
