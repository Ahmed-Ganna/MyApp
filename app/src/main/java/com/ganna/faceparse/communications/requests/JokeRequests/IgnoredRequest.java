package com.ganna.faceparse.communications.requests.JokeRequests;

import com.ganna.faceparse.Constants.ParseConstants;
import com.ganna.faceparse.callbacks.requestscallbacks.IgnoredJokesCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class IgnoredRequest {

    public IgnoredRequest() {
    }

    public void makeIgnoredRequest(ParseUser user,final IgnoredJokesCallback callback){
        ParseQuery<ParseObject> query =new ParseQuery<>(ParseConstants.JOKE_USER_CLASS);
        query.whereEqualTo(ParseConstants.USER_POINTER, user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                ArrayList<String> ids = new ArrayList<>();
                for (ParseObject object : list) {
                    ParseObject jokeObject = object.getParseObject(ParseConstants.JOKE_POINTER);
                    String jokeId = jokeObject.getObjectId();
                    ids.add(jokeId);
                }
                callback.onCompleted(ids);
            }
        });
    }
}
