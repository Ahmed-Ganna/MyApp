package com.ganna.faceparse.communications.requests;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class FbUserRequest {

    public FbUserRequest(){

    }

    public void makeUserRequest(GraphRequest.GraphJSONObjectCallback callback){
        GraphRequest request = GraphRequest.newMeRequest( AccessToken.getCurrentAccessToken(),callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
