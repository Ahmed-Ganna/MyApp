package com.ganna.faceparse.communications.requests.JokeRequests;

import com.ganna.faceparse.Constants.ParseConstants;
import com.ganna.faceparse.data.model.Joke;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Ahmed on 08/10/2015.
 */
public class SeenRequest {

    public SeenRequest() {
    }

    public void makeSeenRequest(ParseUser user,Joke joke){
        final ParseObject object = new ParseObject(ParseConstants.JOKE_USER_CLASS);
        object.put(ParseConstants.USER_POINTER, user);
        object.put(ParseConstants.JOKE_POINTER, joke);
        object.put(ParseConstants.INTERACTION_COLUMN, 0);
        object.saveInBackground();
        }
}
