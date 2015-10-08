package com.ganna.faceparse.ui.joke;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.ganna.faceparse.Constants.ParseConstants;
import com.ganna.faceparse.callbacks.requestscallbacks.AnalyzeCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.DeleteCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.IgnoredJokesCallback;
import com.ganna.faceparse.communications.requests.AnalyzeFriendRequest;
import com.ganna.faceparse.communications.requests.DeleteFriendsRequests;
import com.ganna.faceparse.communications.requests.DislikeRequest;
import com.ganna.faceparse.communications.requests.IgnoredRequest;
import com.ganna.faceparse.communications.requests.LikeRequest;
import com.ganna.faceparse.communications.requests.NewJokeRequest;
import com.ganna.faceparse.communications.requests.SeenRequest;
import com.ganna.faceparse.communications.requests.SpecificJokeRequest;
import com.ganna.faceparse.data.models.Joke;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 01/10/2015.
 */
public class JokeControl {

    private IgnoredRequest ignoredJokesRequest;
    private NewJokeRequest newJokeRequest;
    private LikeRequest likeJokeRequest;
    private DislikeRequest dislikeRequest;
    private SeenRequest seenRequest;
    private AnalyzeFriendRequest analyzeFriendRequest;
    private DeleteFriendsRequests deleteFriendsRequests;
    private SpecificJokeRequest specificJokeRequest;

    public JokeControl() {
        ignoredJokesRequest = new IgnoredRequest();
        newJokeRequest= new NewJokeRequest();
        likeJokeRequest=new LikeRequest();
        dislikeRequest = new DislikeRequest();
        seenRequest =new SeenRequest();
        analyzeFriendRequest =new AnalyzeFriendRequest();
        deleteFriendsRequests=new DeleteFriendsRequests();
        specificJokeRequest=new SpecificJokeRequest();
    }

    public void getIgnoredJokes(final ParseUser user, final IgnoredJokesCallback callback){
        ignoredJokesRequest.makeIgnoredRequest(user, new FindCallback<ParseObject>() {
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

    public void getNewJoke(ArrayList<String> ignoredIds, final GetCallback<Joke> callback){
        newJokeRequest.makeNewJokeRequest(ignoredIds, callback);
    }

    public void interactWithJoke(ParseUser user,Joke joke,boolean isLike, final SaveCallback callback) {
        if (isLike) {
            likeJokeRequest.makeLikeRequest(user, joke, callback);
        }else {
            dislikeRequest.makeDislikeRequest(user, joke, callback);
        }
    }

    public void setJokeSeen(ParseUser user,Joke joke){
        seenRequest.makeSeenRequest(user, joke);
    }

    public void analyzeRequest(AccessToken accessToken,String requestId, final AnalyzeCallback callback){
        analyzeFriendRequest.makeRequestAnalyst(accessToken, requestId, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject graphObject = response.getJSONObject();
                try {
                    String jokeId = graphObject.getString("data");
                    callback.onCompleted(jokeId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteRequest(AccessToken accessToken,String requestId, final DeleteCallback callback){
        deleteFriendsRequests.makeDeleteRequest(accessToken, requestId, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                callback.onCompleted(error);
            }
        });
    }

    public void getSpecificJoke(String jokeId,GetCallback<Joke> callback){
        specificJokeRequest.makeSpecificRequest(jokeId, callback);
    }
}
