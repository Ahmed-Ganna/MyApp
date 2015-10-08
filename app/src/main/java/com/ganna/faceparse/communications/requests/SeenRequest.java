package com.ganna.faceparse.communications.requests;

import com.ganna.faceparse.Constants.ParseConstants;
import com.ganna.faceparse.data.models.Joke;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Ahmed on 08/10/2015.
 */
public class SeenRequest {

    public SeenRequest() {
    }

    public void makeSeenRequest(ParseUser user,Joke joke){
        final ParseObject object = new ParseObject(ParseConstants.JOKES_LIKES_CLASS);
        object.put(ParseConstants.USER_POINTER, user);
        object.put(ParseConstants.JOKE_POINTER, joke);
        object.put(ParseConstants.INTERACTION_COLUMN, 0);
        object.saveInBackground();
        }
}