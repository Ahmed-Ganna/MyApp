package com.ganna.faceparse.data.model;

import com.ganna.faceparse.Constants.ParseConstants;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Ahmed on 22/09/2015.
 */

@ParseClassName(ParseConstants.JOKE_CLASS)

public class Joke extends ParseObject {

    private int likeStatus ;
    private boolean isInteracted =false;
    private boolean isSaved = false;

    public boolean isSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public void setLikes(){
        increment(ParseConstants.LIKES_COLUMN);
    }

    public int getLikes(){
        return getInt(ParseConstants.LIKES_COLUMN);
    }

    public ParseFile getImg(){
        return getParseFile(ParseConstants.IMAGE_COLUMN);
    }

    public void setDislikes(){
        increment(ParseConstants.DISLIKES_COLUMN);
    }

    public int getDislikes(){
        return getInt(ParseConstants.DISLIKES_COLUMN);
    }

    public void setLikeStatus(int status){
        this.likeStatus =status;
    }

    public int getLikeStatus(){
        return likeStatus;
    }

    public boolean isInteracted() {
        return isInteracted;
    }

    public void setIsInteracted(boolean isInteracted) {
        this.isInteracted = isInteracted;
    }

    public String getTxt(){
        return getString(ParseConstants.TEXT_COLUMN);
    }

    public void setTxt(String txt){
        put(ParseConstants.TEXT_COLUMN,txt);
    }

    public void setAuthor(ParseUser user){
        put(ParseConstants.AUTHOR_COLUMN, user);
    }

    public ParseUser getAuthor(){
        return getParseUser(ParseConstants.AUTHOR_COLUMN);
    }
}
