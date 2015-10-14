package com.ganna.faceparse.communications.requests;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.ganna.faceparse.callbacks.requestscallbacks.AnalyzeCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmed on 08/10/2015.
 */
public class AnalyzeFriendRequest {
    public AnalyzeFriendRequest() {
    }
    public void makeRequestAnalyst(AccessToken accessToken,String requestId, final AnalyzeCallback callback){
        GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, requestId, new GraphRequest.Callback() {
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
        request.executeAsync();
    }
}
