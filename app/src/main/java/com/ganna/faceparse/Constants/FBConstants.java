package com.ganna.faceparse.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 08/10/2015.
 */
public class FBConstants {

    public static final List<String> permissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
    }};

    public static final String USER_FIELDS = "id,name,email";
    public static final String FIELDS_NAME_SPACE = "fields";
}
