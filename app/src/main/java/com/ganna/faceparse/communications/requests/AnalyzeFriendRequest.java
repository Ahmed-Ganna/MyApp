package com.ganna.faceparse.communications.requests;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;

/**
 * Created by Ahmed on 08/10/2015.
 */
public class AnalyzeFriendRequest {
    public AnalyzeFriendRequest() {
    }
    public void makeRequestAnalyst(AccessToken accessToken,String requestId,GraphRequest.Callback callback){
        GraphRequest request = GraphRequest.newGraphPathRequest(accessToken,requestId,callback);
        request.executeAsync();
    }
}
