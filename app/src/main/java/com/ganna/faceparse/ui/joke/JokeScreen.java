package com.ganna.faceparse.ui.joke;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.widget.GameRequestDialog;
import com.ganna.faceparse.Constants.FBConstants;
import com.ganna.faceparse.R;
import com.ganna.faceparse.callbacks.requestscallbacks.AnalyzeCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.DeleteCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.IgnoredJokesCallback;
import com.ganna.faceparse.data.models.Joke;
import com.ganna.faceparse.ui.login.LoginScreen;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class JokeScreen extends Activity implements SwipyRefreshLayout.OnRefreshListener {
    private ParseUser user;
    private JokeControl controller;
    private ImageView imageView;
    private Button like, dislike, share;
    private TextView likes, dislikes,indicator;
    private Joke curJoke;
    private ArrayList<String> ignoredIds;
    private ArrayList<Joke> jokes;
    private SwipyRefreshLayout swipyRefreshLayout;
    private GameRequestDialog requestDialog;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);
        init();
        if (user!=null){
            // get new Joke at first time app launch
            getIgnoredJokes();
        }else {
            startActivity(new Intent(this, LoginScreen.class));
            finish();
        }


    }

    // get jokes excluded from search
    private void getIgnoredJokes() {
        controller.getIgnoredJokes(user, new IgnoredJokesCallback() {
            @Override
            public void onCompleted(ArrayList<String> ids) {
                ignoredIds = ids;
                getNewJoke();
            }
        });
    }


    private void getNewJoke() {
        controller.getNewJoke(ignoredIds, new GetCallback<Joke>() {
            @Override
            public void done(Joke joke, ParseException e) {
                if (e == null) {
                    ignoredIds.add(joke.getObjectId());
                    if (jokes.size()==0) {
                        setNewJoke(joke);
                    }else {
                        jokes.add(joke);
                        setIndicatorText();
                    }
                }
            }
        });
    }

    private void setNewJoke(Joke joke) {
        jokes.add(joke);
        curJoke = joke;
        Picasso.with(this)
                .load(joke.getImgUrl())
                .into(imageView);
        setLikesAndDislikes();
        setBtnsAttr();
        setIndicatorText();
        setJokeSeen();
    }


    private void setJokeSeen() {
        controller.setJokeSeen(user, curJoke);
    }

    private void setBtnsAttr() {
        int status = curJoke.getStatus();
        switch (status) {
            case 1:
                like.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                break;
            case 2:
                dislike.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                like.setBackgroundColor(getResources().getColor(android.R.color.white));
                dislike.setBackgroundColor(getResources().getColor(android.R.color.white));
                break;
        }

    }

    private void setLikesAndDislikes() {
        likes.setText(""+curJoke.getLikes());
        dislikes.setText(""+curJoke.getDislikes());
    }

    private void init() {
        user = ParseUser.getCurrentUser();
        controller = new JokeControl();
        like = (Button) findViewById(R.id.like);
        dislike = (Button) findViewById(R.id.dislike);
        share = (Button) findViewById(R.id.shareFb);
        likes = (TextView) findViewById(R.id.likes_tv);
        dislikes = (TextView) findViewById(R.id.dislikes_tv);
        indicator = (TextView) findViewById(R.id.indicator);
        imageView = (ImageView) findViewById(R.id.img);
        ignoredIds = new ArrayList<>();
        jokes = new ArrayList<>();
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe_layout);
        swipyRefreshLayout.setOnRefreshListener(this);
        swipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        callbackManager = CallbackManager.Factory.create();
        
        requestDialog = new GameRequestDialog(this);
        requestDialog.registerCallback(callbackManager, new FacebookCallback<GameRequestDialog.Result>() {
            public void onSuccess(GameRequestDialog.Result result) {
                Log.d("requestId", result.getRequestId());
                getNewJoke();
            }

            public void onCancel() {
            }

            public void onError(FacebookException error) {
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!curJoke.isInteracted()) {
                    interactWithJoke(true);
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!curJoke.isInteracted()) {
                    interactWithJoke(false);
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken().getPermissions().contains(FBConstants.permissions.get(2))) {
                    shareJokeByFb();
                } else {
                    requestFriendsPermission();
                }
            }
        });
    }

    // Request friends permission from user
    private void requestFriendsPermission() {
        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, this, Arrays.asList(FBConstants.permissions.get(2)), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    shareJokeByFb();
                }else {

                }
            }
        });
    }

    // App request
    private void shareJokeByFb() {
        GameRequestContent content = new GameRequestContent.Builder()
                .setMessage("see this joke")
                .setData(curJoke.getObjectId())
                .build();
        requestDialog.show(content);
    }

    // like - dislikle
    private void interactWithJoke(boolean isLike) {
        if (isLike) {
            curJoke.setStatus(1);
        } else {
            curJoke.setStatus(2);
        }
        controller.interactWithJoke(user, curJoke, isLike, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                curJoke.setIsInteracted(true);
                setLikesAndDislikes();
                setBtnsAttr();
                getNewJoke();
            }
        });
    }

    // on Swipe Top - Bottom
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        int curIndex = jokes.indexOf(curJoke);
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            if (curIndex!=0) {
                getLastJoke();
            }else {
                swipyRefreshLayout.setRefreshing(false);
            }
        } else {
            if (curIndex!=jokes.size()-1) {
                getNestJoke();
            }else {
                swipyRefreshLayout.setRefreshing(false);
            }
        }
    }

    private void getNestJoke() {
        Joke nextJoke = jokes.get(jokes.indexOf(curJoke)+1);
        setExistingJoke(nextJoke);
    }

    private void getLastJoke() {
        Joke lastJoke = jokes.get(jokes.indexOf(curJoke)-1);
        setExistingJoke(lastJoke);
    }

    // navigate to existing joke [last - next]
    private void setExistingJoke(Joke joke) {
        curJoke=joke;
        Picasso.with(this)
                .load(joke.getImgUrl())
                .into(imageView);
        setLikesAndDislikes();
        setBtnsAttr();
        setIndicatorText();
        swipyRefreshLayout.setRefreshing(false);
    }

    // handling receiving App request
    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            Uri target = getIntent().getData();
            if (target != null) {
                String requestIds = target.getQueryParameter("request_ids");
                String[] sendersRequestsIds = requestIds.split(",");
                final String lastRequestID = sendersRequestsIds[sendersRequestsIds.length - 1];
                if (lastRequestID != null) {
                    controller.analyzeRequest(AccessToken.getCurrentAccessToken(), lastRequestID, new AnalyzeCallback() {
                        @Override
                        public void onCompleted(String jokeId) {
                            setSpecificJoke(jokeId,lastRequestID);
                        }
                    });
                }

            }
        }else {
            startActivity(new Intent(this, LoginScreen.class));
            finish();
        }
    }

    private void setSpecificJoke(String jokeId, final String requestId) {
        controller.getSpecificJoke(jokeId, new GetCallback<Joke>() {
            @Override
            public void done(Joke joke, ParseException e) {
                setNewJoke(joke);
                deleteFriendRequest(requestId);
            }
        });
    }

    // delete App request after consumed
    private void deleteFriendRequest(String requestId) {
        controller.deleteRequest(AccessToken.getCurrentAccessToken(), requestId, new DeleteCallback() {
            @Override
            public void onCompleted(FacebookRequestError error) {
                if (error == null) {
                    Log.d("request", "consumed and deleted");
                } else {
                    Log.d("request", "consumed but not deleted");
                }
            }
        });
    }

    private void setIndicatorText(){
        int curIndex = jokes.indexOf(curJoke)+1;
        indicator.setText(""+curIndex+"/"+jokes.size());
    }

    // handling facebook- Activity life cycle
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
