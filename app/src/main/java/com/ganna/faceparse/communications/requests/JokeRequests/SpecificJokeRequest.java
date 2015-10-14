package com.ganna.faceparse.communications.requests.JokeRequests;

import com.ganna.faceparse.data.model.Joke;
import com.parse.GetCallback;
import com.parse.ParseQuery;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class SpecificJokeRequest {

    public SpecificJokeRequest() {
    }

    public void makeSpecificRequest(String jokeId,GetCallback<Joke> callback){
        ParseQuery<Joke> query = ParseQuery.getQuery(Joke.class);
        query.getInBackground(jokeId, callback);
    }
}
