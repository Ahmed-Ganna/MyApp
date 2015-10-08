package com.ganna.faceparse.communications.requests;

import com.ganna.faceparse.Constants.ParseConstants;
import com.ganna.faceparse.data.models.Joke;
import com.parse.GetCallback;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class NewJokeRequest {

    public NewJokeRequest() {
    }

    public void makeNewJokeRequest(ArrayList<String> ignoredIds , GetCallback<Joke> callback){
        ParseQuery<Joke> query = ParseQuery.getQuery(Joke.class);
        query.whereNotContainedIn(ParseConstants.ID_COLUMN, ignoredIds);
        query.orderByDescending(ParseConstants.LIKES_COLUMN);
        query.getFirstInBackground(callback);
    }
}
