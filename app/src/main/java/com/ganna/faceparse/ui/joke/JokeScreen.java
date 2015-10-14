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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.widget.GameRequestDialog;
import com.ganna.faceparse.Constants.FBConstants;
import com.ganna.faceparse.R;
import com.ganna.faceparse.callbacks.controlsCallbacks.LikeCallback;
import com.ganna.faceparse.callbacks.controlsCallbacks.NewJokeCallback;
import com.ganna.faceparse.callbacks.controlsCallbacks.SaveOfflineCallback;
import com.ganna.faceparse.callbacks.requestscallbacks.AnalyzeCallback;
import com.ganna.faceparse.data.model.Joke;
import com.ganna.faceparse.managers.ScreenManager;
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
    private ImageView jokeImgView;
    private Button likeBtn, dislikeBtn, share,fav,saveBtn;
    private TextView likes, dislikes,indicator,jokeTv;
    private Joke curJoke;
    private ArrayList<String> ignoredIds;
    private ArrayList<Joke> jokes;
    private SwipyRefreshLayout swipyRefreshLayout;
    private GameRequestDialog requestDialog;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        init();
        if (user!=null){
            getNewJoke();
        }else {
            ScreenManager.launchLoginScreen(this);
        }


    }


    private void getNewJoke() {
        controller.getNewJoke(user, new NewJokeCallback() {
            @Override
            public void onCompleted(Joke joke) {
                if (joke != null) {
                    if (jokes.size() == 0) {
                        setNewJoke(joke);
                    } else {
                        // add to list but do not show it
                        jokes.add(joke);
                        setIndicatorText();
                    }
                }else {

                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setNewJoke(Joke joke) {
        jokes.add(joke);
        curJoke = joke;
        if (joke.getImg()!=null) {
            jokeImgView.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(joke.getImg().getUrl())
                    .into(jokeImgView);
        }else {
            jokeImgView.setVisibility(View.GONE);
        }
        if (joke.getTxt()!=null) {
            jokeTv.setVisibility(View.VISIBLE);
            jokeTv.setText(joke.getTxt());
        }else {
            jokeTv.setVisibility(View.GONE);
        }
        setLikesAndDislikes();
        setBtnsAttr();
        setIndicatorText();
    }

    private void setBtnsAttr() {
        int status = curJoke.getLikeStatus();
        switch (status) {
            case 1:
                likeBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                dislikeBtn.setBackgroundColor(getResources().getColor(android.R.color.white));
                break;
            case 2:
                dislikeBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                likeBtn.setBackgroundColor(getResources().getColor(android.R.color.white));
                break;
            default:
                likeBtn.setBackgroundColor(getResources().getColor(android.R.color.white));
                dislikeBtn.setBackgroundColor(getResources().getColor(android.R.color.white));
                break;
        }
        if (curJoke.isSaved()){
            saveBtn.setText("unSave");
        }else {
            saveBtn.setText("save");
        }
    }

    private void setLikesAndDislikes() {
        likes.setText(""+curJoke.getLikes());
        dislikes.setText(""+curJoke.getDislikes());
    }

    private void init() {
        user = ParseUser.getCurrentUser();
        controller = new JokeControl();
        likeBtn = (Button) findViewById(R.id.btn_like);
        dislikeBtn = (Button) findViewById(R.id.btn_dislike);
        share = (Button) findViewById(R.id.shareFb);
        saveBtn = (Button) findViewById(R.id.save_btn);
        likes = (TextView) findViewById(R.id.likes_tv);
        dislikes = (TextView) findViewById(R.id.dislikes_tv);
        fav= (Button) findViewById(R.id.favBtn);
        indicator = (TextView) findViewById(R.id.indicator);
        jokeImgView = (ImageView) findViewById(R.id.img);
        jokeTv= (TextView) findViewById(R.id.txt_joke);
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

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!curJoke.isInteracted()) {
                    setLikeStatus(true);
                }
            }
        });

        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!curJoke.isInteracted()) {
                    setLikeStatus(false);
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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curJoke.isSaved()){
                    unSaveJoke();
                }else {
                    saveJoke();
                }
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.launchFavouriteScreen(JokeScreen.this);
            }
        });
    }

    // Request friends permission from user
    private void requestFriendsPermission() {
        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, this, Arrays.asList(FBConstants.permissions.get(2)), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    shareJokeByFb();
                } else {

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

    // like - dislike
    private void setLikeStatus(boolean isLike) {
        if (isLike) {
            curJoke.setLikeStatus(1);
        } else {
            curJoke.setLikeStatus(2);
        }
        controller.changeLikeStatus(user, curJoke, isLike, new LikeCallback() {
            @Override
            public void onCompleted() {
                curJoke.setIsInteracted(true);
                setLikesAndDislikes();
                setBtnsAttr();
                getNewJoke();
            }

            @Override
            public void onError(String message) {

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
        Joke nextJoke = jokes.get(jokes.indexOf(curJoke) + 1);
        setExistingJoke(nextJoke);
    }

    private void getLastJoke() {
        Joke lastJoke = jokes.get(jokes.indexOf(curJoke)-1);
        setExistingJoke(lastJoke);
    }

    // navigate to existing joke [last - next]
    private void setExistingJoke(Joke joke) {

        curJoke=joke;
        if (joke.getImg()!=null) {
            jokeImgView.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(joke.getImg().getUrl())
                    .into(jokeImgView);
        }else {
            jokeImgView.setVisibility(View.GONE);
        }
        if (joke.getTxt()!=null) {
            jokeTv.setVisibility(View.VISIBLE);
            jokeTv.setText(joke.getTxt());
        }else {
            jokeTv.setVisibility(View.GONE);
        }
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
            ScreenManager.launchLoginScreen(this);
        }
    }

    private void setSpecificJoke(String jokeId, final String requestId) {
        controller.getSpecificJoke(jokeId, new GetCallback<Joke>() {
            @Override
            public void done(Joke joke, ParseException e) {
                setNewJoke(joke);
            }
        });
    }


    private void setIndicatorText(){
        int curIndex = jokes.indexOf(curJoke)+1;
        indicator.setText("" + curIndex + "/" + jokes.size());
    }

    private void saveJoke(){
        controller.saveJoke(user, curJoke, new SaveOfflineCallback() {
            @Override
            public void onCompleted() {
                saveBtn.setText("unSave");
                curJoke.setIsSaved(true);
                Toast.makeText(JokeScreen.this, "Save Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void unSaveJoke(){
        controller.unSaveJoke(ParseUser.getCurrentUser(), curJoke, new SaveOfflineCallback() {
            @Override
            public void onCompleted() {
                saveBtn.setText("save");
                curJoke.setIsSaved(false);
                Toast.makeText(JokeScreen.this,"Save Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    // handling facebook- Activity life cycle
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
