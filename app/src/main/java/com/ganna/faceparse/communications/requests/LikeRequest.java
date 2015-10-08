package com.ganna.faceparse.communications.requests;

import com.ganna.faceparse.Constants.ParseConstants;
import com.ganna.faceparse.data.models.Joke;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class LikeRequest {

    public LikeRequest() {
    }

    public void makeLikeRequest(ParseUser user,Joke joke, final SaveCallback callback){
        final ParseObject object = new ParseObject(ParseConstants.JOKE_USER_CLASS);
        object.put(ParseConstants.USER_POINTER, user);
        object.put(ParseConstants.JOKE_POINTER, joke);
        object.put(ParseConstants.INTERACTION_COLUMN, 1);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ParseObject jokeObject = object.getParseObject(ParseConstants.JOKE_POINTER);
                jokeObject.increment(ParseConstants.LIKES_COLUMN);
                jokeObject.saveInBackground(callback);
            }
        });
    }
}
