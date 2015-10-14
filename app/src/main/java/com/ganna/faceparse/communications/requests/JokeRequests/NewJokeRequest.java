package com.ganna.faceparse.communications.requests.JokeRequests;

import com.ganna.faceparse.Constants.ParseConstants;
import com.ganna.faceparse.callbacks.requestscallbacks.NewJokeCallback;
import com.ganna.faceparse.data.model.Joke;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class NewJokeRequest {

    public NewJokeRequest() {
    }

    public void makeNewJokeRequest(ArrayList<String> ignoredIds , final NewJokeCallback callback){
        ParseQuery<Joke> query = ParseQuery.getQuery(Joke.class);
        query.whereNotContainedIn(ParseConstants.ID_COLUMN, ignoredIds);
        query.orderByDescending(ParseConstants.LIKES_COLUMN);
        query.getFirstInBackground(new GetCallback<Joke>() {
            @Override
            public void done(Joke joke, ParseException e) {
               callback.onCompleted(joke,e);
            }
        });
    }
}
