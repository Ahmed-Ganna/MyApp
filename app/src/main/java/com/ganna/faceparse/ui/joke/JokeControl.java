package com.ganna.faceparse.ui.joke;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.ganna.faceparse.callbacks.controlsCallbacks.LikeCallback;
import com.ganna.faceparse.callbacks.controlsCallbacks.SaveOfflineCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.AnalyzeCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.DeleteRequestCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.IgnoredJokesCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.NewJokeCallback;
import com.ganna.faceparse.communications.requests.AnalyzeFriendRequest;
import com.ganna.faceparse.communications.requests.DeleteFriendsRequests;
import com.ganna.faceparse.communications.requests.JokeRequests.IgnoredRequest;
import com.ganna.faceparse.communications.requests.JokeRequests.LikedStatusRequest;
import com.ganna.faceparse.communications.requests.JokeRequests.NewJokeRequest;
import com.ganna.faceparse.communications.requests.JokeRequests.SaveOfflineRequest;
import com.ganna.faceparse.communications.requests.JokeRequests.SavedStatusRequest;
import com.ganna.faceparse.communications.requests.JokeRequests.SeenRequest;
import com.ganna.faceparse.communications.requests.JokeRequests.SpecificJokeRequest;
import com.ganna.faceparse.communications.requests.JokeRequests.UnSaveOfflineRequest;
import com.ganna.faceparse.data.model.Joke;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by Ahmed on 01/10/2015.
 */
// handle relation between ui and requests
public class JokeControl {

    private IgnoredRequest ignoredJokesRequest;
    private NewJokeRequest newJokeRequest;
    private LikedStatusRequest likeJokeRequest;
    private SeenRequest seenRequest;
    private AnalyzeFriendRequest analyzeFriendRequest;
    private DeleteFriendsRequests deleteFriendsRequests;
    private SpecificJokeRequest specificJokeRequest;
    private SavedStatusRequest savedStatusRequest;
    private SaveOfflineRequest saveOfflineRequest;
    private UnSaveOfflineRequest unSaveOfflineRequest;
    private ArrayList<String> ignoredIds;

    public JokeControl() {
        ignoredJokesRequest = new IgnoredRequest();
        newJokeRequest= new NewJokeRequest();
        likeJokeRequest=new LikedStatusRequest();
        seenRequest =new SeenRequest();
        analyzeFriendRequest =new AnalyzeFriendRequest();
        deleteFriendsRequests=new DeleteFriendsRequests();
        specificJokeRequest=new SpecificJokeRequest();
        savedStatusRequest =new SavedStatusRequest();
        saveOfflineRequest =new SaveOfflineRequest();
        unSaveOfflineRequest = new UnSaveOfflineRequest();
        ignoredIds = new ArrayList<>();
    }

    public void getIgnoredJokes(final ParseUser user, final IgnoredJokesCallback callback){
        ignoredJokesRequest.makeIgnoredRequest(user, callback);
    }

    public void getNewJoke(final ParseUser user,final com.ganna.faceparse.callbacks.controlsCallbacks.NewJokeCallback callback){
        getIgnoredJokes(user, new IgnoredJokesCallback() {
            @Override
            public void onCompleted(ArrayList<String> ids) {
                ignoredIds = ids;
                newJokeRequest.makeNewJokeRequest(ignoredIds, new NewJokeCallback() {
                    @Override
                    public void onCompleted(Joke joke, ParseException e) {
                        if (e == null) {
                            ignoredIds.add(joke.getObjectId());
                            setJokeSeen(user, joke);
                            callback.onCompleted(joke);
                        } else {
                            callback.onError(e.getMessage());
                        }
                    }
                });

            }
        });
        }

    public void changeLikeStatus(ParseUser user,Joke joke,boolean isLike, final LikeCallback callback) {
            likeJokeRequest.makeLikeStatusRequest(user, joke, isLike, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        callback.onCompleted();
                    }else {
                        callback.onError(e.getMessage());
                    }
                }
            });
    }

    public void setJokeSeen(ParseUser user,Joke joke){
        seenRequest.makeSeenRequest(user, joke);
    }

    public void analyzeRequest(AccessToken accessToken,String requestId, final AnalyzeCallback callback){
        analyzeFriendRequest.makeRequestAnalyst(accessToken, requestId, callback);
    }

    public void deleteRequest(AccessToken accessToken,String requestId, final DeleteRequestCallback callback){
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

    private void setSavedStatus(ParseUser user,Joke joke,boolean isSave){
        savedStatusRequest.makeSaveRequest(user, joke, isSave);
    }

    public void saveJoke(final ParseUser user, final Joke joke, final SaveOfflineCallback callback){
            saveOfflineRequest.makeSaveOfflineRequest(joke, new SaveOfflineCallback() {
                @Override
                public void onCompleted() {
                    setSavedStatus(user, joke, true);
                    callback.onCompleted();
                }

                @Override
                public void onError(String message) {
                    callback.onError(message);
                }
            });
    }

    public void unSaveJoke(final ParseUser user, final Joke joke, final SaveOfflineCallback callback){
        unSaveOfflineRequest.makeRequest(joke, new SaveOfflineCallback() {
            @Override
            public void onCompleted() {
                setSavedStatus(user,joke,false);
                callback.onCompleted();
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}
