package com.ganna.faceparse.ui.login;

import com.ganna.faceparse.callbacks.requestscallbacks.UserDetailsCallback;
import com.ganna.faceparse.communications.requests.FbUserRequest;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class LoginControl {

    private FbUserRequest fbUserRequest;
    public LoginControl(){

        fbUserRequest =new FbUserRequest();
    }

    public void getUserDetails(final UserDetailsCallback callback){
        fbUserRequest.makeUserRequest(callback);
    }
}
