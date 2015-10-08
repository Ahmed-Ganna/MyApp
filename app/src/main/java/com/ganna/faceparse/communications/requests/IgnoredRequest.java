package com.ganna.faceparse.communications.requests;

import com.ganna.faceparse.Constants.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class IgnoredRequest {

    public IgnoredRequest() {
    }

    public void makeIgnoredRequest(ParseUser user,FindCallback<ParseObject> callback){
        ParseQuery<ParseObject> query =new ParseQuery<>(ParseConstants.JOKES_LIKES_CLASS);
        query.whereEqualTo(ParseConstants.USER_POINTER, user);
        query.findInBackground(callback);
    }
}
