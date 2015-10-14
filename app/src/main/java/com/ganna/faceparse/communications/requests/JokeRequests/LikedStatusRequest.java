package com.ganna.faceparse.communications.requests.JokeRequests;

import com.ganna.faceparse.Constants.ParseConstants;
import com.ganna.faceparse.data.model.Joke;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class LikedStatusRequest {

    public LikedStatusRequest() {
    }

    public void makeLikeStatusRequest(ParseUser user,Joke joke, final boolean isLike, final SaveCallback callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.JOKE_USER_CLASS);
        query.whereEqualTo(ParseConstants.USER_POINTER, user);
        query.whereEqualTo(ParseConstants.JOKE_POINTER, joke);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                Joke jokeObj = (Joke) object.getParseObject(ParseConstants.JOKE_POINTER);
                object.put(ParseConstants.INTERACTION_COLUMN, 1);
                if (isLike){
                    object.put(ParseConstants.LIKED_COLUMN, 1);
                    jokeObj.setLikes();
                }else {
                    object.put(ParseConstants.LIKED_COLUMN, 0);
                    jokeObj.setDislikes();
                }
                jokeObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        object.saveInBackground(callback);
                    }
                });
            }
        });
    }
}
