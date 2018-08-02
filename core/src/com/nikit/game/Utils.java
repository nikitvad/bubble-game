package com.nikit.game;

import com.badlogic.gdx.physics.box2d.Body;

public class Utils {

    public static String getContactType(Body a, Body b) {
        if (a.getUserData() == null || b.getUserData() == null) {
            return Constants.CONTACT_TYPE_UNDEFINED;
        }

        if (a.getUserData().equals(Constants.OBJECT_TYPE_BALL) || b.getUserData().equals(Constants.OBJECT_TYPE_BALL)) {
            if (a.getUserData().equals(Constants.OBJECT_TYPE_EDGE) || b.getUserData().equals(Constants.OBJECT_TYPE_EDGE)) {
                if (a.getUserData().equals(Constants.OBJECT_TYPE_BALL)) {
                    return Constants.CONTACT_TYPE_BALL_EDGE;
                } else {
                    return Constants.CONTACT_TYPE_EDGE_BALL;
                }
            }
        }
        return Constants.CONTACT_TYPE_UNDEFINED;
    }
}
