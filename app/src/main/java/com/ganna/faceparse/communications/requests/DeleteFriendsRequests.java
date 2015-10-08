package com.ganna.faceparse.communications.requests;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;

/**
 * Created by Ahmed on 08/10/2015.
 */
public class DeleteFriendsRequests {
    public DeleteFriendsRequests() {
    }
    public void makeDeleteRequest(AccessToken accessToken,String requestId,GraphRequest.Callback callback){
        GraphRequest request = GraphRequest.newDeleteObjectRequest(accessToken,requestId,callback);
        request.executeAsync();
    }
}
