package com.ganna.faceparse.data.models;

import com.ganna.faceparse.Constants.ParseConstants;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ahmed on 22/09/2015.
 */

@ParseClassName(ParseConstants.JOKES_CLASS)

public class Joke extends ParseObject {

    private int status =0;
    private boolean isInteracted =false;

    public void setLikes(){
        increment(ParseConstants.LIKES_COLUMN);
    }

    public int getLikes(){
        return getInt(ParseConstants.LIKES_COLUMN);
    }

    public String getImgUrl(){
        return getParseFile(ParseConstants.IMAGE_COLUMN).getUrl();
    }

    public void setDislikes(){
        increment(ParseConstants.DISLIKES_COLUMN);
    }

    public int getDislikes(){
        return getInt(ParseConstants.DISLIKES_COLUMN);
    }

    public void setStatus(int status){
        this.status =status;
    }

    public int getStatus(){
        return status;
    }

    public boolean isInteracted() {
        return isInteracted;
    }

    public void setIsInteracted(boolean isInteracted) {
        this.isInteracted = isInteracted;
    }
}
